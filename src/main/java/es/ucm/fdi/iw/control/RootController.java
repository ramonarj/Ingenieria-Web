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
//		User u = (User)session.getAttribute("u");
//		model.addAttribute("user", u);
//		
//		iwSocketHandler.sendText("ramon", "AVISO: " + u.getLogin() + " esta mirando el menu");
		return "inicio";
	} 
	
	//Página de horarios (se accede desde el inicio)
	@GetMapping("/horario")
	public String horario(Model model) {	
		return "horario";
	} 
	
	//Página de perfil (se accede desde el inicio)
	@GetMapping("/perfil")
	public String perfil(Model model) {
		return "perfil";
	} 
	
	@GetMapping("/equipo")
	public String equipo(Model model, HttpSession session) {
		model.addAttribute("users", entityManager.createQuery(
				"SELECT u FROM User u").getResultList());
		
		return "equipo";
	}
	
	//Página de chat (se accede desde el inicio)
	@GetMapping("/chat")
	public String chat(Model model, HttpServletRequest request) {
		model.addAttribute("users", entityManager.createQuery(
				"SELECT u FROM User u").getResultList());
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
