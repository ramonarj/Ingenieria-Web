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
import es.ucm.fdi.iw.model.Turno;
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

	
	@GetMapping("/{id}")
	@Transactional
	public String getUser(@PathVariable long id, Model model, HttpSession session) throws Exception {
		User u = entityManager.find(User.class, id);
		model.addAttribute("user", u);
		
		//Enviar mensaje al admin
		iwSocketHandler.sendText("ramon", "AVISO: " + u.getLogin() + " esta mirando el menu");
		return "inicio";
	}
	
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
}