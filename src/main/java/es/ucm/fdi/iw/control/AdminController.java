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
	
	@GetMapping("/{id}")
	public String index(Model model, @PathVariable long id) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		model.addAttribute("users", entityManager.createQuery(
				"SELECT u FROM User u").getResultList());
		
		User u = entityManager.find(User.class, id);
		
		model.addAttribute("user", u);
		model.addAttribute("turnos", entityManager.createQuery(
				"SELECT t FROM Turno t").getResultList());
		
		return "admin";
	}
	
	
	@PostMapping("/adduser")
	@Transactional
	public String addUser(Model model, @RequestParam String login, @RequestParam String password, @RequestParam String name,
			@RequestParam String idfire, @RequestParam Turno turno)
	{
		User user = new User();
		user.setEnabled((byte)1);
		user.setLogin(login);
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles("USER");
		user.setName(name);
		user.setIdfire(idfire);
		user.setTurno(turno);
		entityManager.persist(user);
		entityManager.flush();
		
		return "admin";
	}
	
	/**
	 * Creates a map from a query, where the 1st column of results is used as key.
	 * @param queryName that returns unique long ids as 1st column
	 * @return a map using this id as key, and either
	 * 	- full rows, if each row has over 2 columns
	 *  - single values (those of the 2nd column), if each row has exactly 2 columns
	 */
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
	public String delUser(Model model,	@RequestParam long id) {
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
		return index(model, id);
	}		
}