package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@NamedQueries({
	@NamedQuery(name="Turno.byLogin",
	query="SELECT t FROM Turno t "
			+ "WHERE t.login = :userLogin AND t.enabled = 1"),
	@NamedQuery(name="Turno.hasLogin",
	query="SELECT COUNT(t) "
			+ "FROM Turno t "
			+ "WHERE t.login = :userLogin")
})
public class Turno {
    @JsonView(Views.Public.class)    
	private long id;
    @JsonView(Views.Public.class)    
	private String login;	
	private byte enabled;
	private String name;
	private String idfire;
	
	private List<User> users;
	
	//@OneToMany(targetEntity=User.class)
	//@JoinColumn(name)
	//public List<User> getUsers(){
		//return users;
	//}
	
	//public void setUserToTurn(List<User> users) {
		//this.users = users;
	//}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	

	@Column(unique=true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public byte getEnabled() {
		return enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdfire() {
		return idfire;
	}

	public void setIdfire(String idfire) {
		this.idfire = idfire;
	}
}