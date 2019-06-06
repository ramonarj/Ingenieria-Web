package es.ucm.fdi.iw.control;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
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
import es.ucm.fdi.iw.model.Cambio;
import es.ucm.fdi.iw.model.CambioTool;
import es.ucm.fdi.iw.model.Dia;
import es.ucm.fdi.iw.model.Turno;
import es.ucm.fdi.iw.model.Herramienta;
import es.ucm.fdi.iw.model.Auto;

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
	
	@GetMapping("/delTool")
	public String delTool(Model model, HttpSession session) {
		
		session.setAttribute("herramientas", entityManager.createQuery(
				"SELECT h FROM Herramienta h").getResultList());
		
		return "delTool";
	}
	
	@GetMapping("/assignTool")
	public String assignTool(Model model, HttpSession session) {
		
		session.setAttribute("users", entityManager.createNamedQuery("User.all").getResultList());
		
		session.setAttribute("herramientas", entityManager.createQuery(
				"SELECT h FROM Herramienta h").getResultList());
		
		return "assignTool";
	}
	
	@GetMapping("/switchTool")
	public String switchTool(Model model, HttpSession session) {
		
		session.setAttribute("users", entityManager.createNamedQuery("User.all").getResultList());
		
		return "switchTool";
	}
	
	@GetMapping("/delUser")
	public String delUser(Model model) {
		return "delUser";
	}
	
	@GetMapping("/manageChanges")
	public String manageChanges(Model model, HttpSession session) {
		RootController.updateChanges(model, session, entityManager);
		return "manageChanges";
	}
	
	@GetMapping("/addAuto")
	public String addAuto(Model model, HttpSession session) {		
		return "addAuto";
	}
	
	@GetMapping("/delAuto")
	public String delAuto(Model model, HttpSession session) {	
		session.setAttribute("autos", entityManager.createQuery("SELECT a FROM Auto a").getResultList());
		return "delAuto";
	}
	
	@GetMapping("/assignDriver")
	public String assignDriver(Model model, HttpSession session) {
		
		session.setAttribute("users", entityManager.createNamedQuery("User.all").getResultList());
		
		return "assignDriver";
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
	
	@GetMapping("/getToolsOfUserToSwitch")
	public String getToolsOfUserToSwitch(Model model, HttpSession session, @RequestParam long user1_id, @RequestParam  long user2_id) 
	{		
		//1.VEMOS SI NOSOTROS TENEMOS ESE DÍA	
		User user1= entityManager.find(User.class, user1_id);
		session.setAttribute("user1",  user1); //Lo añadimos a la sesión
		
		User user2 = entityManager.find(User.class, user2_id);
		session.setAttribute("user2",  user2); //Lo añadimos a la sesión
	
		return "switchTool";
	}
	
	@PostMapping("/resolveToolsSwitch")
	@Transactional
	public String resolveToolsSwitch(Model model, HttpSession session, 
			HttpServletRequest req, @RequestParam long tool1_id, @RequestParam long tool2_id)
	{
		User u1 = (User)session.getAttribute("user1");
		u1 = entityManager.find(User.class, u1.getId());
		User u2 = (User)session.getAttribute("user2");
		u2 = entityManager.find(User.class, u2.getId());
		
		Herramienta h1 = entityManager.find(Herramienta.class, tool1_id);
		Herramienta h2 = entityManager.find(Herramienta.class, tool2_id);
		
		u1.getTools().add(h2);
		u2.getTools().add(h1);
		
		u1.getTools().remove(h1);
		u2.getTools().remove(h2);
		
		return "switchTool";
	}
	
	@PostMapping("/resolveChange")
	@Transactional
	public String resolveChange(Model model, HttpSession session, 
			@RequestParam long cambio_id, HttpServletRequest req)
	{
		Cambio c = entityManager.find(Cambio.class, cambio_id);
		//Vemos qué botón hemos pulsado
		if(req.getParameter("acceptButton") != null) 
		{
			c.setEstado("Aceptado");
			User u1 = entityManager.find(User.class, c.getUser1().getId());
			User u2 = entityManager.find(User.class, c.getUser2().getId());
//			Dia d1 = entityManager.find(Dia.class, c.getDia1().getId());
//			Dia d2 = entityManager.find(Dia.class, c.getDia2());
			
			//Cambio de día
			//Se quita el antiguo...
			u1.getDiasLaborales().add(c.getDia2());
			u2.getDiasLaborales().add(c.getDia1());
			
			//Y se pone el nuevo
			u1.getDiasLaborales().remove(c.getDia1());
			u2.getDiasLaborales().remove(c.getDia2());
			entityManager.flush();
		}
		else if(req.getParameter("denyButton") != null)
			c.setEstado("Denegado");
		RootController.updateChanges(model, session, entityManager);
		return "manageChanges";
	}
	
	@PostMapping("/resolveToolChange")
	@Transactional
	public String resolveToolChange(Model model, HttpSession session, 
			@RequestParam long cambio_id, HttpServletRequest req)
	{
		CambioTool c = entityManager.find(CambioTool.class, cambio_id);
		//Vemos qué botón hemos pulsado
		if(req.getParameter("acceptButton") != null) 
		{
			c.setEstado("Aceptado");
			User u1 = entityManager.find(User.class, c.getUser1().getId());
			User u2 = entityManager.find(User.class, c.getUser2().getId());
//			Dia d1 = entityManager.find(Dia.class, c.getDia1().getId());
//			Dia d2 = entityManager.find(Dia.class, c.getDia2());
			
			//Cambio de día
			//Se quita el antiguo...
			u1.getTools().add(c.getTool2());
			u2.getTools().add(c.getTool1());
			
			//Y se pone el nuevo
			u1.getTools().remove(c.getTool1());
			u2.getTools().remove(c.getTool2());
			entityManager.flush();
		}
		else if(req.getParameter("denyButton") != null)
			c.setEstado("Denegado");
		RootController.updateChanges(model, session, entityManager);
		return "manageChanges";
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
	public String addToolToDB(Model model, @RequestParam String type)
	{
		Herramienta herramienta = new Herramienta();	
		herramienta.setType(type);
		
		entityManager.persist(herramienta);
		
		return "admin";
	}
	
	@PostMapping("/delToolFromDB")
	@Transactional
	public String delToolFromDB(Model model, @RequestParam long tool_id)
	{
		Herramienta h = entityManager.find(Herramienta.class, tool_id);	
		entityManager.remove(h);
		
		return "admin";
	}
	
	
	@PostMapping("/assignToolToDB")
	@Transactional
	public String assignToolToDB(Model model, @RequestParam String herramienta_id,
			@RequestParam String user_id)
	{
		
		User u = entityManager.find(User.class, Long.parseLong(user_id)); 
		
		Herramienta h = entityManager.find(Herramienta.class, Long.parseLong(herramienta_id)); 
		
		Herramienta herramienta = new Herramienta();	
		herramienta.setType(h.getType());
		herramienta.setUser(u);
		
		if(h.getUser() != null)
			h.getUser().removeTool(herramienta);
				
		u.addNewTool(herramienta);
		
		entityManager.remove(h);
		
		entityManager.persist(herramienta);
		
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
	
	
	/*AQUI ESTÁ LA PARTE DE GESTIÓN VEHICULOS*/
	@PostMapping("/addAutoToDB")
	@Transactional
	public String addAutoToDB(Model model, @RequestParam String type)
	{
		Auto a = new Auto();	
		a.setType(type);
		
		entityManager.persist(a);
		
		return "admin";
	}
	
	@PostMapping("/delAutoFromDB")
	@Transactional
	public String delAutoFromDB(Model model, @RequestParam long auto_id)
	{
		Auto a = entityManager.find(Auto.class, auto_id);	
		entityManager.remove(a);
		
		return "admin";
	}
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/assignDriverToDB")
	@Transactional
	public String assignDriverToDB(Model model, HttpSession session,
			@RequestParam long user_id)
	{
		for(User u: (List<User>)session.getAttribute("users")) {
			User user = entityManager.find(User.class, u.getId());
			if (user.getId() == user_id)
				user.setDriver("SI");
			else
				user.setDriver("NO");
		}
		
		entityManager.flush();
		
		return "admin";
	}
}