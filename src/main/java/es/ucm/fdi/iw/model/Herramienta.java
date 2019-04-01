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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

import es.ucm.fdi.iw.model.Turno;

@Entity
@NamedQueries({
	@NamedQuery(name="Herramienta.byLogin",
	query="SELECT h FROM Herramienta h "
			+ "WHERE h.login = :userLogin AND h.enabled = 1"),
	@NamedQuery(name="Herramienta.hasLogin",
	query="SELECT COUNT(h) "
			+ "FROM User h "
			+ "WHERE h.login = :userLogin")
})
public class Herramienta {
    @JsonView(Views.Public.class)    
	private long id;
    private String name;
    @JsonView(Views.Public.class)    
	private String login;	
	private byte enabled;
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
}
	
	
