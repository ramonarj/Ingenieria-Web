package es.ucm.fdi.iw;

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
		context.setAttribute("dateFormatter", 
				new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss.sssZ"));
		
		log.info("Application is ready to load. Initializing user workdays...");
		List<User> all = entityManager.createNamedQuery("User.all").getResultList();
		for (User u : all) {
			u.createDiasLaborales("2019-05-12", entityManager);
		}
	}
}