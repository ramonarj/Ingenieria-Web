package es.ucm.fdi.iw.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import es.ucm.fdi.iw.model.Turno;
import es.ucm.fdi.iw.model.Herramienta;

@Entity
@NamedQueries({
	@NamedQuery(name="User.byLogin",
	query="SELECT u FROM User u "
			+ "WHERE u.login = :userLogin AND u.enabled = 1"),
	@NamedQuery(name="User.hasLogin",
	query="SELECT COUNT(u) "
			+ "FROM User u "
			+ "WHERE u.login = :userLogin")
})
public class User {
	private long id;
	private String login;	
	private String password;
	private String roles; // split by ',' to separate roles
	private byte enabled;
	private String name;
	private String idfire;
	private String phone;
	private String mail;
	private String address;
	
	private Turno turno;
	private List<Herramienta> tools;
	
	public boolean hasRole(String roleName) {
		return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equalsIgnoreCase(roleName));
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

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
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
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@OneToMany(targetEntity=Herramienta.class)
	@JoinColumn(name="user_id")
	public List<Herramienta> getTools(){
		return tools;
	}
	
	public void setTools(List<Herramienta> h) {
		this.tools = h;
	}
	
	@ManyToOne(targetEntity=Turno.class)
	public Turno getTurno() {
		return turno;
	}
	public void setTurno(Turno t) {
		this.turno = t;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", roles=" + roles + ", enabled="
				+ enabled + "]";
	}
}