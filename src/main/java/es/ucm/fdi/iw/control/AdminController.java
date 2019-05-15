package es.ucm.fdi.iw.control;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Turno;
import es.ucm.fdi.iw.model.Herramienta;

/**
 * Admin-only controller
 * @author mfreire
 */
@Controller()
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log = LogManager.getLogger(AdminController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		session.setAttribute("users", entityManager.createNamedQuery("User.all").getResultList());
		
		session.setAttribute("turnos", entityManager.createQuery(
				"SELECT t FROM Turno t").getResultList());
		
		
		return "admin";
	}
	
	@GetMapping("/addUser")
	public String addUser(Model model) {
		return "addUser";
	}
	
	@GetMapping("/addTool")
	public String addTool(Model model, HttpSession session) {
		
		session.setAttribute("users", entityManager.createNamedQuery("User.all").getResultList());
		
		return "addTool";
	}
	
	@GetMapping("/delUser")
	public String delUser(Model model) {
		return "delUser";
	}
	
	@GetMapping("/manageChanges")
	public String manageChanges(Model model, HttpSession session) {
		session.setAttribute("cambiosPropuestos", entityManager.createNamedQuery("Cambio.proposedOnes").getResultList());
		session.setAttribute("cambiosAceptados", entityManager.createNamedQuery("Cambio.acceptedOnes").getResultList());
		session.setAttribute("cambiosDenegados", entityManager.createNamedQuery("Cambio.deniedOnes").getResultList());
		return "manageChanges";
	}
	
	
	
	
	@PostMapping("/addUserToDB")
	@Transactional
	public String addUserToDB(Model model, @RequestParam String login, @RequestParam String password, @RequestParam String name,
			@RequestParam String idfire, @RequestParam long turno_id)
	{
		User user = new User();
		user.setEnabled((byte)1);
		user.setLogin(login);
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles("USER");
		user.setName(name);
		user.setIdfire(idfire);		
		Turno t = entityManager.find(Turno.class, turno_id);
		user.setTurno(t);
		user.createDiasLaborales(t.getStart(), entityManager);
		
		entityManager.persist(user);
		entityManager.flush();
		
		return "admin";
	}
	
	@PostMapping("/delUserFromDB")
	@Transactional
	public String delUserFromDB(Model model, @RequestParam long user_id)
	{
		User u = entityManager.find(User.class, user_id);
		entityManager.remove(u);
		return "admin";
	}
	
	/**
	 * Creates a map from a query, where the 1st column of results is used as key.
	 * @param queryName that returns unique long ids as 1st column
	 * @return a map using this id as key, and either
	 * 	- full rows, if each row has over 2 columns
	 *  - single values (those of the 2nd column), if each row has exactly 2 columns
	 */
	
	/*Añadimos aqui dentro una herramienta nueva a la base de datos y
	 *  se la asignamos a una persona <3*/
	
	@PostMapping("/addToolToDB")
	@Transactional
	public String addToolToDB(Model model, @RequestParam String type, 
			@RequestParam String user_id)
	{
		
		User u = entityManager.find(User.class, Long.parseLong(user_id));
		
		Herramienta herramienta = new Herramienta();	
		herramienta.setUser(u);
		herramienta.setType(type);

		entityManager.persist(herramienta);
		//entityManager.flush();

		
		return "admin";
	}
	
	
	private Map<Long, Object> countsToMap(String queryName) {
		Map<Long, Object> m = new HashMap<>();
		@SuppressWarnings("unchecked")
		List<Object[]> results = entityManager.createNamedQuery(queryName).getResultList();
		for (Object[] row : results) {
			if (row.length == 2) {
				m.put((Long)row[0], row[1]);
			} else {
				m.put((Long)row[0], row);
			}
		}
		log.info("CountsToMap for {} returned {} rows", queryName, m.size());
		return m;
	}

	
	@PostMapping("/toggleuser")
	@Transactional
	public String delUser(Model model,	@RequestParam long id, HttpSession session) {
		User target = entityManager.find(User.class, id);
		if (target.getEnabled() == 1) {
			// disable
			File f = localData.getFile("user", ""+id);
			if (f.exists()) {
				f.delete();
			}
			target.setEnabled((byte)0); // disable user
		} else {
			// enable
			target.setEnabled((byte)1);
		}
		return index(model, session);
	}		
}