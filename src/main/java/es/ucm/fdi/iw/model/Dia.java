package es.ucm.fdi.iw.model;
import es.ucm.fdi.iw.model.Turno;

import java.text.SimpleDateFormat;  
import java.util.Date; 
import java.text.DateFormat;
import java.text.ParseException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
public class Dia {
	private static final Logger log = LogManager.getLogger(Dia.class);
	
	private long id;
	private String fecha; 
	private Turno turno;
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	@ManyToOne(targetEntity=Turno.class)
	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

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
		try {
			return "{id: '" + id + "'"
				+ ", start: '" + fecha + "T09:30:00'"
				+ ", end: '" + next(fecha, 1) + "T09:30:00'"
				+ ", name: '" + turno.getName() + "'}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fecha;
	}	
	
	public static String next(String fecha, int numDiasEntreTurnos) throws Exception {
 		// 2019-05-12 => 2019-05-13 ; 2019-05-30 => 2019-06-01		
		String[] parts = fecha.split("-");
		int day = Integer.parseInt(parts[2]);
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[0]);
		int d = day;
		day += numDiasEntreTurnos;
		if (day == 31) {
			month ++;
			day = day - d;
		}
		if (month == 13) {
			year ++;
			month = 1;
		}
		
		String dateString = String.format("%4d-%2d-%2d", year, month, day);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(dateString);  
		String str = df.format(date);
		
		return str;
	}
}