package com.home.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;



// MAP TO THE DB TABLE
public class Flight {

	private int id;
	private String fromAirport;
	private String toAirport;
	private String departure;
	private String arrival;
	private String flightNumber;
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  

	public Flight() {
		super();
	}

	public Flight(String fromAirport, String toAirport, Date departure, Date arrival, String flightNumber) {
		super();
		this.fromAirport = fromAirport;
		this.toAirport = toAirport;
		this.flightNumber = flightNumber;
		
		if (departure != null) {
			this.departure = departure.toString();
		} else {
			this.departure = "";
		}
		
		if (arrival != null) {
			this.arrival = arrival.toString();
		} else {
			this.arrival = "";
		}

	}

	public Flight(int id, String fromAirport, String toAirport, Date departure, Date arrival, String flightNumber) {
		super();
		this.id = id;
		this.fromAirport = fromAirport;
		this.toAirport = toAirport;
		this.departure = departure.toString();
		this.arrival = arrival.toString();
		this.flightNumber = flightNumber;
	}
	
	// * FOR TESTING PURPOSES
	public Flight(String fromAirport, String toAirport, String flightNumber) {
		super();
		this.departure = null;
		this.arrival = null;
		this.fromAirport = fromAirport;
		this.toAirport = toAirport;
		this.flightNumber = flightNumber;
	}
	
	/*
	 * TRY OVERRIDE CONSTRUCTOR WITH STRING FOR DATE?
	 * 
	 * try to write getters for date after converting them to string
	 */

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getFromAirport() {
		return fromAirport;
	}

	public void setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
	}

	public String getToAirport() {
		return toAirport;
	}

	public void setToAirport(String toAirport) {
		this.toAirport = toAirport;
	}

	public String getDeparture() {
		return departure;
	}
	
//	public String getDepartureAsString() {
//		if (departure != null) {
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			String dep = df.format(departure);
//			return dep;
//		}
//		return null;
//		
//	}

	public void setDeparture(Date departure) {
		this.departure = df.format(departure);
	}

	public String getArrival() {
		return arrival;
	}
	
//	public String getArrivalAsString() {
//		if (arrival != null) {
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			String arr = df.format(arrival);
//			return arr;
//		}
//		return null;
//	}

	public void setArrival(Date arrival) {
		this.arrival = df.format(arrival);
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", fromAirport=" + fromAirport + ", toAirport=" + toAirport + ", departure="
				+ departure + ", arrival=" + arrival + ", flightNumber=" + flightNumber + "];" + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(arrival, departure, flightNumber, fromAirport, id, toAirport);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		return Objects.equals(arrival, other.arrival) && Objects.equals(departure, other.departure)
				&& Objects.equals(flightNumber, other.flightNumber) && Objects.equals(fromAirport, other.fromAirport)
				&& id == other.id && Objects.equals(toAirport, other.toAirport);
	}
	
	
	

}