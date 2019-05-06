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
public class Dia {
	private long id;
	private String fecha; 
	private Turno turno;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	

	@Override
	public String toString() {
		return "{id: '" + id + "'"
			+ ", start: '" + fecha + "T09:30:00'"
			+ ", end: '" + next(fecha, 1) + "T09:30:00'"
			+ ", name: '" + turno.getName() + "'}";
	}	
	
	public static String next(String fecha, int numDiasEntreTurnos) {
 		// 2019-05-12 => 2019-05-13 ; 2019-05-30 => 2019-06-01
		String[] parts = fecha.split("-");
		int day = Integer.parseInt(parts[2]);
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[0]);
		day += numDiasEntreTurnos;
		if (day == 31) {
			month ++;
			day = 1;
		}
		if (month == 13) {
			year ++;
			month = 1;
		}
		
		return String.format("%4d-%2d-%2d", year, month, day);
	}
}