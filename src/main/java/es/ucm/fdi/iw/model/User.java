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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import es.ucm.fdi.iw.model.Turno;
import es.ucm.fdi.iw.model.Herramienta;

//TODO:
//1. CHAT CON WEBSOCKET
//2. BASE DE DATOS PERSISTENTE -> DONE
//3. INTERFAZ DEL ADMIN 
//4. SEGURIDAD
//5. LIMAR TODO

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
	private String rankfire;
	private String driver;
	
	private Turno turno;
	private List<Turno> diasLaborales;
	
	@OneToMany(targetEntity=User.class)
	@JoinColumn(name="turno_id")
	public List<Turno> getDiasLaborales() {
		return diasLaborales;
	}

	public void setDiasLaborales(List<Turno> diasLaborales) {
		this.diasLaborales = diasLaborales;
	}
	
	public void createDiasLaborales(String startDate) {
		//diasLaborales = new ArrayList<Turno>();
		
		//Lo limpiamos solo de momento
		diasLaborales.clear();
		//Primero anadimos el primr turno que tenemos en la lista
		diasLaborales.add(turno);
		
		//"2019-06-01T09:30:00" Formato de fecha
		
		//Cogemos el comienzo y el fin del turno para iterar sobre ello
		String start = turno.getStart();
		String end = turno.getEnd();

		//Despues anadimos otros 29 turnos, para que en total sean 30, sumando 4 dias a los siguientes
		for(int i = 0; i < 29; i++) {
			Turno auxT = new Turno();
			
			//Todos los turnos van a tener la misma ID y Nombre
			auxT.setId(turno.getId());
			auxT.setName(turno.getName());
			
			//START DATE
			String Startyear = start.substring(0,4);
			String Startmonth = start.substring(5,7);
			String Startday = start.substring(8,10);
			String Starthour = start.substring(10, start.length());
			
			//Parseamos a entero para sumar
			int intStartDay = Integer.parseInt(Startday);
			intStartDay = intStartDay + 4;
			
			//Si nuestro dia supera el 30 DE MOMENTO lo ponemos a dia 1 y cambiamos de mes
			if(intStartDay > 30) {
				intStartDay = 1;
				int intStartMonth = Integer.parseInt(Startmonth);
				intStartMonth = intStartMonth + 1;
				
				//Para establecer el formato que acepta el calendario
				if(intStartMonth > 9)
					Startmonth = Integer.toString(intStartMonth);
				else
					Startmonth = "0" + Integer.toString(intStartMonth);
			}
			
			//Aceptarse al formato de dias
			if(intStartDay > 9) 
				Startday = Integer.toString(intStartDay);
			else 
				Startday = "0" + Integer.toString(intStartDay);
			
			//Aqui debemos sumar el año si nos hemos pasado, pero para probar no hace falta de momento
			
			//Finalmente establecemos el dia de inicio como este nuevo dia creado
			start = Startyear + "-" + Startmonth + "-" + Startday + Starthour;
			
			//Hacemos exactamente lo mismo con la fecha de fin del evento
			//END DATE
			String Endyear = end.substring(0,4);
			String Endmonth = end.substring(5,7);
			String Endday = end.substring(8,10);
			String Endhour = end.substring(10, end.length());
			
			int intEndDay = Integer.parseInt(Endday);
			intEndDay = intEndDay + 4;
			
			if(intEndDay > 30) {
				intEndDay = 1;
				int intEndMonth = Integer.parseInt(Endmonth);
				intEndMonth = intEndMonth + 1;
				
				if(intEndMonth > 9)
					Endmonth = Integer.toString(intEndMonth);
				else
					Endmonth = "0" + Integer.toString(intEndMonth);
			}
			
			if(intEndDay > 9) 
				Endday = Integer.toString(intEndDay);
			else 
				Endday = "0" + Integer.toString(intEndDay);
			
			//Falta añadir la suma del año
			
			end = Endyear + "-" + Endmonth + "-" + Endday + Endhour;
			
			//Finalmente los anadimos al auxiliar y lo metemos en la lista
			auxT.setStart(start);
			auxT.setEnd(end);
		
			diasLaborales.add(auxT);
		
		}
	}
	
	//Metodo para debuguear en la consola
	public void showDiasLaborales() {
		for(int i = 0; i < diasLaborales.size(); i++) {
			System.out.println("////------------/////" + diasLaborales.get(i).getName() + "////------------/////"  + diasLaborales.get(i).getStart() + 
						"////------------/////" + diasLaborales.get(i).getEnd() + "////------------/////");
				
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