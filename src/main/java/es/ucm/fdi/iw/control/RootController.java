package es.ucm.fdi.iw.control;

import java.security.Principal;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.User; ///USER
import es.ucm.fdi.iw.model.Turno; ///TURNO

@Controller
public class RootController {
	
	@Autowired 
	private EntityManager entityManager;
	
	
	private static final Logger log = LogManager.getLogger(RootController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;
	
	//Página de inicio (se accede desde el login)
	
	//Página de login (la que sale por defecto)
	@GetMapping(path= {"/", "/login"})
	public String menu(Model model) {

		return "login";
	} 
	
	@GetMapping("/inicio")
	public String inicio(Model model) {	// HttpSession session
		return "inicio";
	} 
	
	//Página de horarios (se accede desde el inicio)
	@GetMapping("/horario")
	 public String horario(Model model, HttpSession session) {
		
		incorporaHorario(model, session, entityManager);
		return "horario";
	} 
	
	//Para llamarlo desde otros controladores
	 protected static void incorporaHorario(Model model, HttpSession session, EntityManager em) {
		
		model.addAttribute("users", em.createNamedQuery("User.all").getResultList());
		
		User u = (User)session.getAttribute("u");
		u = em.find(User.class, u.getId());
		
		model.addAttribute("calendario", u.laboralesEnBonito());
	} 
	 
	//Para llamarlo desde otros controladores
		 protected static void updateChanges(Model model, HttpSession session, EntityManager em) {
			session.setAttribute("cambiosPropuestos", em.createNamedQuery("Cambio.proposedOnes").getResultList());
			session.setAttribute("cambiosAceptados", em.createNamedQuery("Cambio.acceptedOnes").getResultList());
			session.setAttribute("cambiosDenegados", em.createNamedQuery("Cambio.deniedOnes").getResultList());
			
			session.setAttribute("cambiosHerramientasPropuestos", em.createNamedQuery("CambioTool.proposedOnes").getResultList());
			session.setAttribute("cambiosHerramientasAceptados", em.createNamedQuery("CambioTool.acceptedOnes").getResultList());
			session.setAttribute("cambiosHerramientasDenegados", em.createNamedQuery("CambioTool.deniedOnes").getResultList());
		} 
	
	//Página de perfil (se accede desde el inicio)
	@GetMapping("/perfil")
	public String perfil(Model model) {
		return "perfil";
	} 
	
	
	@GetMapping("/equipo")
	 public String equipo(Model model, HttpSession session) {
		
		incorporaEquipo(model, session, entityManager);
		return "equipo";
	} 
	
	protected static void incorporaEquipo(Model model, HttpSession session, EntityManager em) {
		model.addAttribute("users", em.createNamedQuery("User.all").getResultList());
		User requester = (User)session.getAttribute("u");
		User u = em.find(User.class, requester.getId());
		model.addAttribute("user", u);
		
		model.addAttribute("herramientas", em.createQuery(
				"SELECT h FROM Herramienta h").getResultList());
		
	}
	
	//Página de chat (se accede desde el inicio)
	@GetMapping("/chat")
	public String chat(Model model, HttpServletRequest request) {
		model.addAttribute("users", entityManager.createNamedQuery("User.all").getResultList());
		return "chat";
	} 
	
	@GetMapping("/admin")
	public String admin(Model model, Principal principal) {		
		return "admin";
	}
	
	@GetMapping("/error")
	public String error(Model model) {
		return "error";
	}
}
