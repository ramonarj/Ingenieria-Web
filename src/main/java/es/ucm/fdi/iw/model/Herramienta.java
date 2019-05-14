package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.ucm.fdi.iw.model.User;

@Entity
public class Herramienta {	
	private long id;
	private String type;
	private User user;
		
	@ManyToOne(targetEntity=User.class)
	public User getUser() {
		return user;
	}
	public void setUser(User u) {
		this.user = u;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	
	
	public void setType(String t) {
		this.type = t;
	}
	
	public String getType() {
		return this.type;
	}
}
	
	
