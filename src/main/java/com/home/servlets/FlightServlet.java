package com.home.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.sql.Date;
import java.util.Date;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.Controllers.FlightController;
import com.home.beans.Flight;
import com.home.data.FlightDAO;


@WebServlet(urlPatterns = "/flights")
public class FlightServlet extends HttpServlet {


	private static List<List<Flight>> flightDataToSendList; // new List<List<Flight>>();

	/* SEND DATA FROM DATABASE TO CLIENT */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(new FlightController().getAllFlights());
		
		
		System.out.println("############### SENDING BACK TO CLIENT ###########################");
		System.out.println(json);
		
		resp.getWriter().print(json); 
		resp.setContentType("application/json");
		
	}

	// READING JSON FROM CLIENT AND STORING IT IN DATABASE
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		ObjectMapper mapper = new ObjectMapper();
		Flight flight = mapper.readValue(req.getInputStream(), Flight.class);
		System.out.println("doPost ---> " + flight);
		
		System.out.println("Trying to add new flight...");
		new FlightController().addFlight(flight);

	}

	// UPDATE FLIGHT BY FLIGHT NUMBER
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("Updating stuff");
		Flight flight = mapper.readValue(req.getInputStream(), Flight.class);
		System.out.println("RECEIVE DATA TO UPDATE: " + flight);
		new FlightController().updateFlight(flight);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("Deleting stuff");
		Flight flight = mapper.readValue(req.getInputStream(), Flight.class);
		System.out.println("DELETE BY # " + flight.getFlightNumber());
		new FlightController().deleteFlight(flight.getFlightNumber());
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
 * 
 */
//
//
//
//
