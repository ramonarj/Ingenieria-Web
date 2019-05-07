package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.ucm.fdi.iw.control.UserController;

//TODO:
//1. CHAT CON WEBSOCKET
//2. BASE DE DATOS PERSISTENTE -> DONE
//3. INTERFAZ DEL ADMIN 
//4. SEGURIDAD
//5. LIMAR TODO

@Entity
@NamedQueries({
	@NamedQuery(name="User.all",
	query="SELECT u FROM User u"),
	@NamedQuery(name="User.byLogin",
	query="SELECT u FROM User u "
			+ "WHERE u.login = :userLogin AND u.enabled = 1"),
	@NamedQuery(name="User.hasLogin",
	query="SELECT COUNT(u) "
			+ "FROM User u "
			+ "WHERE u.login = :userLogin")
})
public class User {
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
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
	private String rankfire;
	private String driver;
	
	private Turno turno;
	private List<Dia> diasLaborales = new ArrayList<>();
	
	@OneToMany(targetEntity=Dia.class)
	@JoinColumn(name="currela_id")
	public List<Dia> getDiasLaborales() {
		return diasLaborales;
	}
	
	public String laboralesEnBonito() {
		
		List<String> turnosBonitos = new ArrayList<>();
		
		for (Dia d : diasLaborales) turnosBonitos.add(d.toString());
		
		return "[" + String.join(",", turnosBonitos) + "]";
	}
	
	public void setDiasLaborales(List<Dia> diasLaborales) {
		this.diasLaborales = diasLaborales;
	}
	
	public void createDiasLaborales(String startDate, EntityManager em) {
		
		if ( ! diasLaborales.isEmpty()) return;
		
		Dia dIni = new Dia();
		dIni.setFecha(startDate);
		dIni.setTurno(turno);
		dIni.setCurrela(this);
		diasLaborales.add(dIni);
		em.persist(dIni);
		
		for(int i = 1; i < 10; i++) {
			Dia d = new Dia();
			d.setFecha(d.next(diasLaborales.get(i - 1).getFecha(), 4));
			d.setTurno(turno);
			diasLaborales.add(d);
			em.persist(d);
		}
	}
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

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
	
	public String getRankfire() {
		return rankfire;
	}

	public void setRankfire(String rankfire) {
		this.rankfire = rankfire;
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