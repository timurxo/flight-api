package com.home.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.Controllers.FlightController;
import com.home.beans.Flight;

@WebServlet(urlPatterns = "/flight")
public class GetOneFlightServlet extends HttpServlet{
	
	private List<Flight> flightNumber;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.addHeader("Access-Control-Allow-Origin", "*");

		ObjectMapper mapper = new ObjectMapper();
		
		// GET PARAMETER FROM CLIENT
		String paramString = req.getParameter("flightNumber");
		System.out.println("---> searching DB by flight number: " + paramString);
		
		String json = mapper.writeValueAsString(new FlightController().getFlightByFlightNumber(paramString));
		
		
		System.out.println("@@@@@@@@@@ SENDING BACK TO CLIENT @@@@@@@@@@");
		System.out.println("---> sending: " + json);
		
		resp.getWriter().print(json); 
		resp.setContentType("application/json");
	}

	

}
