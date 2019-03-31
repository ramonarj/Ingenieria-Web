package es.ucm.fdi.iw.control;

import java.security.Principal;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	//Página de login (la que sale por defecto)
	@GetMapping("/")
	public String menu(Model model) {
		return "login";
	} 
	
	//Página de inicio (se accede desde el login)
	@GetMapping("/login")
	public String login(Model model)
	{
		return "login";
	} 
	
	//Página de horarios (se accede desde el inicio)
	@GetMapping("/horario")
	public String horario(Model model) {
		return "horario";
	} 
	
	//Página de equipo (se accede desde el inicio)
	@GetMapping("/equipo")
	public String equipo(Model model) {
		return "equipo";
	} 
	
	//Página de chat (se accede desde el inicio)
	@GetMapping("/chat")
	public String chat(Model model, HttpServletRequest request) {
		model.addAttribute("socketUrl", request.getRequestURL().toString()
				.replaceFirst("[^:]*", "ws")
				.replace("chat", "ws"));
		return "chat";
	} 
		

	@GetMapping("/admin")
	public String admin(Model model, Principal principal) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		
		log.info("let us all welcome this admin, {}", principal.getName());
		
		return "index";
	}
	
	@GetMapping("/error")
	public String error(Model model) {
		return "error";
	}
}
