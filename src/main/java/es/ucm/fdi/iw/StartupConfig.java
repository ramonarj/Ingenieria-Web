package es.ucm.fdi.iw;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import es.ucm.fdi.iw.control.RootController;
import es.ucm.fdi.iw.model.Turno;
import es.ucm.fdi.iw.model.User;

/**
 * This code will execute when the application first starts.
 * 
 * @author mfreire
 */
@Component
public class StartupConfig {
	
	private static final Logger log = LogManager.getLogger(RootController.class);
	
	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private ServletContext context;
	
	@EventListener(ContextRefreshedEvent.class)
	@Transactional
	public void contextRefreshedEvent() {
		String debugProperty = env.getProperty("es.ucm.fdi.debug");
		context.setAttribute("debug", debugProperty != null 
				&& Boolean.parseBoolean(debugProperty.toLowerCase()));
		log.info("Setting global debug property to {}", 
				context.getAttribute("debug"));
		
		// see http://www.ecma-international.org/ecma-262/5.1/#sec-15.9.1.15
		// and https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
	
		SimpleDateFormat formatter = new SimpleDateFormat(
				"YYYY-MM-DD'T'HH:mm:ss.sssZ");		
		context.setAttribute("dateFormatter", formatter);
		
		log.info("Application is ready to load. Initializing user workdays...");
		@SuppressWarnings("unchecked")
		List<User> all = (List<User>)entityManager
				.createNamedQuery("User.all")
				.getResultList();
		
		Date date = new Date(System.currentTimeMillis()); 
		String sDate = formatter.format(date);
		
		String[] dates = sDate.split("-");		
		int year = Integer.parseInt(dates[0]);		
		year++;
		
		for (User u : all) {
			Turno t = u.getTurno();
			String todo = t.getStart();			
			String[] partes = todo.split("-");			
			if (Integer.parseInt(partes[0]) != year - 1) {
				// si el año ha cambiado, modifico el turno
				String comienzo = todo.substring(4, todo.length() - 1);
				t.setStart(year + comienzo);
				t.setEnd(year + comienzo);
			}		
		}				
		
		for (User u : all) {
			// genero dias laborables pra este turno, usuario y año
			u.createDiasLaborales(u.getTurno().getStart(), entityManager);
		}
	}
}