package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.ucm.fdi.iw.model.User;

@Entity
public class Auto {	
	private long id;
	private String type;
	private User user;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	@ManyToOne(targetEntity=User.class)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}