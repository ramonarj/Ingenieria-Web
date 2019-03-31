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
	private String password;
	private String roles; // split by ',' to separate roles
	private byte enabled;
	private String name;
	private String idfire;
	private String phone;
	private String mail;
	private String address;
	
	
	public boolean hasRole(String roleName) {
		return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equalsIgnoreCase(roleName));
	}
	
	// application-specific fields
	private List<Vote> votes = new ArrayList<>(); 
	private List<Question> questions = new ArrayList<>();
	private List<CGroup> groups = new ArrayList<>();
	
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
	
	@OneToMany(targetEntity=Vote.class)
	@JoinColumn(name="voter_id")
	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	@OneToMany(targetEntity=Question.class)
	@JoinColumn(name="author_id")
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}	
	
	@ManyToMany(targetEntity=CGroup.class, mappedBy="participants")
	public List<CGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<CGroup> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", roles=" + roles + ", enabled="
				+ enabled + "]";
	}
}