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
import es.ucm.fdi.iw.model.Herramienta;


@Entity
@NamedQueries({
	@NamedQuery(name="CambioTool.all",
	query="SELECT c FROM CambioTool c"),
	@NamedQuery(name="CambioTool.proposedOnes",
	query="SELECT c FROM CambioTool c "
			+ "WHERE c.estado = 'Propuesto' "),
	@NamedQuery(name="CambioTool.acceptedOnes",
	query="SELECT c FROM CambioTool c "
			+ "WHERE c.estado = 'Aceptado' "),
	@NamedQuery(name="CambioTool.deniedOnes",
	query="SELECT c FROM CambioTool c "
			+ "WHERE c.estado = 'Denegado' ")//Para los cambios que han sido acordados/no
})
public class CambioTool{
	
	private long id;

	private String estado; // Estado de la petición: Propuesto, Aceptado, Denegado
	
	private User user1;	// Usuario 1 que pide el cambio
	private Herramienta tool1;
	
	private User user2;	//Usuario 2 por el que se va a cambiar
	private Herramienta tool2;
	
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
	
	@ManyToOne(targetEntity=Herramienta.class)
	public Herramienta getTool1() {
		return tool1;
	}
	public void setTool1(Herramienta tool1) {
		this.tool1 = tool1;
	}
	
	@ManyToOne(targetEntity=User.class)
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
	
	@ManyToOne(targetEntity=Herramienta.class)
	public Herramienta getTool2() {
		return tool2;
	}
	public void setTool2(Herramienta tool2) {
		this.tool2 = tool2;
	}
	
}