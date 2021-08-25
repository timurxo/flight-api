package com.home.Controllers;

import java.util.List;
import com.home.beans.Flight;
import com.home.data.FlightDAO;

public class FlightController {

	public List<Flight> getAllFlights() {

		// populate list with data from database
		List<Flight> flightList = null;

		try (FlightDAO dao = new FlightDAO()) {
			flightList = dao.findAllFlights();
			System.out.println("SENDING ALL FLIGHTS DATA...");
		} catch (Exception e) {
			e.printStackTrace();
		}

//		System.out.println("FLIGHTLIST CLASS NAME: " + flightList.getClass().getName());

		return flightList;
	}
	
	
	public List<Flight> getFlightByFlightNumber(String flightNumber) {
		
		List<Flight> flightByFlightNum = null;

		try (FlightDAO dao = new FlightDAO()) {
			flightByFlightNum = dao.findFlightByFlightNumber(flightNumber);
			System.out.println("Found data for flight with FN#: " + flightByFlightNum);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return flightByFlightNum;
	}

	public void addFlight(Flight flight) {

		System.out.println(" ------ RECEIVED FROM CLIENT: " + flight);

		try (FlightDAO dao = new FlightDAO()) {
			dao.addFlight(flight);
			System.out.println("CREATED FLIGHT!");
		} catch (Exception e) {
			System.out.println("FAILED TO ADD NEW FLIGHT!");
			e.printStackTrace();
		}


	}
	
	public void updateFlight(Flight flight) {
		try (FlightDAO dao = new FlightDAO()) {
			dao.updateFlight(flight);
			System.out.println("FLIGHT UPDATED!");
		} catch (Exception e) {
			System.out.println("FAILED TO UPDATE NEW FLIGHT!");
			e.printStackTrace();
		}
	}
	
	public void deleteFlight(String flight) {
		try (FlightDAO dao = new FlightDAO()) {
			dao.deleteByFlightNumber(flight);
			System.out.println("FLIGHT DELETED!");
		} catch (Exception e) {
			System.out.println("FAILED TO DELETE NEW FLIGHT!");
			e.printStackTrace();
		}
	}
	

}




/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
