package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.CascadeType;

import es.ucm.fdi.iw.model.User;


@Entity
@NamedQueries({
	@NamedQuery(name="Cambio.all",
	query="SELECT c FROM Cambio c"),
	@NamedQuery(name="Cambio.proposedOnes",
	query="SELECT c FROM Cambio c "
			+ "WHERE c.estado = 'Propuesto' "),
	@NamedQuery(name="Cambio.acceptedOnes",
	query="SELECT c FROM Cambio c "
			+ "WHERE c.estado = 'Aceptado' "),
	@NamedQuery(name="Cambio.deniedOnes",
	query="SELECT c FROM Cambio c "
			+ "WHERE c.estado = 'Denegado' ")//Para los cambios que han sido acordados/no
})
public class Cambio{
	private long id;

	private String estado; // Estado de la petición: Propuesto, Aceptado, Denegado
	
	private User user1;	// Usuario 1 que pide el cambio
	private Dia dia1;	//Dia a cambiar del usuario 1
	
	private User user2;	//Usuario 2 por el que se va a cambiar
	private Dia dia2;	// Dia a cambiar del usuario 2
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@ManyToOne(targetEntity=User.class)
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	@ManyToOne(targetEntity=Dia.class)
	public Dia getDia1() {
		return dia1;
	}
	public void setDia1(Dia dia1) {
		this.dia1 = dia1;
	}
	
	@ManyToOne(targetEntity=User.class)
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
	@ManyToOne(targetEntity=Dia.class)
	public Dia getDia2() {
		return dia2;
	}
	public void setDia2(Dia dia2) {
		this.dia2 = dia2;
	}
}

