package es.ucm.fdi.iw.model;
import es.ucm.fdi.iw.model.User;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Turno {
	private long id;
	private String date;
	private List<User> users;
		
	@OneToMany(targetEntity=User.class)
	@JoinColumn(name="turno_id")
	public List<User> getUsers(){
		return users;
	}
	
	public void setUsers(List<User> u) {
		this.users = u;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	

	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
}