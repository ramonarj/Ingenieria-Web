package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Cambio;
import es.ucm.fdi.iw.model.Turno;
import es.ucm.fdi.iw.model.Dia;
import es.ucm.fdi.iw.model.Herramienta;

import java.util.List;
/**
 * User-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("user")
public class UserController {
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;

	//Cambia un usuario
	private void alterUser(User target, User changed, String pass2) {
		if (changed.getPassword() != null && changed.getPassword().equals(pass2)) {
			// save encoded version of password
			target.setPassword(passwordEncoder.encode(changed.getPassword()));
		}		
		target.setPhone(changed.getPhone());
		target.setMail(changed.getMail());
		target.setAddress(changed.getAddress());	
	}
	
	@PostMapping("/")
	@Transactional //¡¡PARA MODIFICAR LA BASE DE DATOS!!
	public String editSelf(
			@ModelAttribute User edited, 
			@RequestParam(required=false) String pass2,
			Model model, HttpSession session) {
		
		//Busca el usuario a editar
		User requester = (User)session.getAttribute("u");
		User target = entityManager.find(User.class, requester.getId());
		
		//Cambia el usuario y actualiza la sesión
		alterUser(target, edited, pass2);
		session.setAttribute("u",  target);
		
		return "perfil";
	}	
	
	
	
	@PostMapping("/{id}")
	@Transactional //¡¡PARA MODIFICAR LA BASE DE DATOS!!
	public String editOther(
			@ModelAttribute User edited, 
			@PathVariable long id,
			@RequestParam(required=false) String pass2,
			Model model, 
			HttpSession session) {
	
		//Busca el usuario a editar
		User requester = (User)session.getAttribute("u");
		User target = entityManager.find(User.class, id);
		
		if (! requester.hasRole("admin")) {			
			return "perfil";
		}

		//Cambia el usuario y actualiza la sesión
		alterUser(target, edited, pass2);
		session.setAttribute("u",  target);

		return "admin";
	}	
	
	@GetMapping(value="/photo/{id}")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException {		
		File f = localData.getFile("user", ""+id);
		InputStream in;
		if (f.exists()) {
			in = new BufferedInputStream(new FileInputStream(f));
		} else {
			in = new BufferedInputStream(getClass().getClassLoader()
					.getResourceAsStream("static/img/unknown-user.jpg"));
		}
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream os) throws IOException {
				FileCopyUtils.copy(in, os);
			}
		};
	}
	
	@PostMapping("/photo/{id}")
	public String postPhoto(@RequestParam("photo") MultipartFile photo,
			@PathVariable("id") String id, Model model, HttpSession session){
		User target = entityManager.find(User.class, Long.parseLong(id));
		model.addAttribute("user", target);
		
		// check permissions
		User requester = (User)session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				! requester.hasRole("ADMIN")) {			
			return "perfil";
		}
		
		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", id);
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
			} catch (Exception e) {
				log.info("Error uploading " + id + " ", e);
			}
			log.info("Successfully uploaded photo for {} into {}!", id, f.getAbsolutePath());
		}
		return "perfil";
	}
	
	@GetMapping("/cambiaTurno")
	public String cambiaTurno(Model model, HttpSession session, 
			@RequestParam String date1, @RequestParam String date2, @RequestParam long user2_id) {
		
		//Identificamos a los usuarios implicados
		User u = (User)session.getAttribute("u");
		User u2 = entityManager.find(User.class, user2_id);

		//Creamos el cambio
		Cambio c = new Cambio();
		c.setEstado("Propuesto");
		c.setUser1(u);
		c.setDia1(date1);
		c.setUser2(u2);
		c.setDia2(date2);
		
		//Lo añadimos a la base de datos
		entityManager.persist(c);
		entityManager.flush();
		
		log.info(u.getLogin() + "quiere cambiar su turno del dia " + date1 + " al día " + date2 + " a " + u2.getLogin());
		
		//Recargamos la info del horario
		RootController.incorporaHorario(model, session, entityManager);
		return "horario";
	}	
	
	@GetMapping("/getUsersToChange")
	public String getUsersToChange(Model model, HttpSession session, String fechaOrigen, String fechaDestino) 
	{
//		List<User> users = entityManager.createQuery("SELECT u FROM User u").getResultList();
//		//List<User> filteredUsers = new List<User>();
//		for(User u : users)
//		{
//			for(Dia d: u.getDiasLaborales())
//			{
//				if(d == d1)
//					//Salir del for
//				else if(d == dia2)
//					//Añadir a filteredUsers y salir
//				
//			}
//		}
//		
		
		return "horario";
	}
}