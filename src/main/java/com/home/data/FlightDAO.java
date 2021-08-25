package com.home.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import com.home.beans.Flight;

/*
 * ------- NOTES:
 * REUSABLE CRUD OPERATIONS
 * create flights, retrieve, update, delete flights
 * 
 * - add flight
 * - find all flights
 * - find flight by id
 * - find flight by flight number
 * - delete by flight number 
 * - update flight
 * - delete by flight number
 * 
 */

// dao.addFlight(new Flight("ABC", "CBA", Date.valueOf("2021-11-11"), Date.valueOf("2022-02-02"), "myNum"));

public class FlightDAO implements FlightDAOInterface, AutoCloseable {

	private static final String url = "jdbc:mysql://localhost:3306/flight_schema" + "?serverTimezone=UTC";
	private static final String username = "root";

	private String tableName = "flight_data2";

	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load the driver.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {

		FlightDAO dao = new FlightDAO();
		dao.addFlight(new Flight("ABC", "CBA", Date.valueOf("2021-11-11"), Date.valueOf("2022-02-02"), "bbbbb"));
//		dao.updateFlight(new Flight("BBB", "XXX", Date.valueOf("2021-11-11"), Date.valueOf("2022-02-02"), "asas"));
//		System.out.println(dao.findFlightByFlightNumber("vip222"));
//		System.out.println(dao.findAllFlights());

//		System.out.println( dao.findFlightById(2));
//		dao.deleteByFlightNumber("vip222");
//		System.out.println(dao.findAllFlights());
		
		

	}

	// =================================================================================
	// ------------------------- CONNECTION ------------------------
	// =================================================================================
//	private Connection connection;
//
//	// constructor calls connect()
//	public FlightDAO() throws Exception {
//		connect();
//	}
//
	@Override
	public void close() throws Exception {
//		if (connection != null && !connection.isClosed()) {
//			this.connection.close();
//		}
	}
//
//	public void connect() throws Exception {
//
//		this.connection = DriverManager.getConnection(url, user, password);
//	}

	
	
	
	// =================================================================================
	// ------------------------ CRUD OPERATIONS --------------------
	// =================================================================================

	// ------------- ADD FLIGHT INFORMATION
	@Override
	public void addFlight(Flight flight) throws SQLException {
		addFlight_tableName(flight, this.tableName);
	}
	
	public void addFlight_tableName(Flight flight, String tableName) throws SQLException {

		Connection connection = DriverManager.getConnection(url, username, password);
		try {

			connection.setAutoCommit(false); // tx begins

			String sql = "INSERT INTO " + tableName + "(from_airport, to_airport, departure, arrival, flight_number) "
					+ "VALUES(?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(sql);

			java.util.Date dateDep = sdf1.parse(flight.getDeparture());
			java.sql.Date sqlDateDep = new java.sql.Date(dateDep.getTime());
			
//			System.out.println("DEPARTURE in addFlight(): " + dateConversion(sqlDateDep));

			java.util.Date dateArr = sdf1.parse(flight.getArrival());
			java.sql.Date sqlDateArr = new java.sql.Date(dateArr.getTime());

			stmt.setString(1, flight.getFromAirport());
			stmt.setString(2, flight.getToAirport());
			stmt.setDate(3, dateConversion(sqlDateDep));
			stmt.setDate(4, dateConversion(sqlDateArr));
			stmt.setString(5, flight.getFlightNumber());

			// check if 1 row was affected
			int rows = stmt.executeUpdate();

			if (rows == 1) {
				connection.commit();
				System.out.println("Successfully added new flight!");
			} else {
				throw new SQLException("The flight insert failed");
			}

		} catch (SQLException | ParseException e) {
			e.printStackTrace();
			connection.rollback();
		}

	}

	// -------------- RETRIEVE ALL FLIGHTS INFORMATION
	@Override
	public List<Flight> findAllFlights() throws SQLException {

		List<Flight> flightsInfo = new LinkedList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			String sql = "SELECT id, from_airport, to_airport, departure, arrival, flight_number " + "FROM " + tableName;

			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery(); // all results that came back from db

			// while there is incoming data -> convert result set into Flight object
			// and add to LinkedList
			while (resultSet.next()) {
				
				Flight flight = new Flight(resultSet.getInt("id"), resultSet.getString("from_airport"),
						resultSet.getString("to_airport"), resultSet.getDate("departure"), resultSet.getDate("arrival"),
						resultSet.getString("flight_number"));

				// add to the list
				flightsInfo.add(flight);
//				System.out.println("TYPE OF ARRIVAL DATA: " + flight.getArrival().getClass().getName());
			}
			return flightsInfo;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	// -------------- RETRIEVE INFORMATION BY ID
	@Override
	public Flight findFlightById(int id) throws SQLException {
		return findFlightById_tableName(id, this.tableName);
	}
	
	public Flight findFlightById_tableName(int id, String tableName) throws SQLException {

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			String sql = "SELECT id, from_airport, to_airport, departure, arrival, flight_number "
					+ "FROM " + tableName + " where id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery(); // all results that came back from db
			resultSet.next();
			
			// convert data into correct one
//			Date depppDate = resultSet.getDate("departure");
//			Date newDepppDate = dateConversion(depppDate);
//			
//			Date arrrDate = resultSet.getDate("arrival");
//			Date newArrrDate = dateConversion(arrrDate);
			
			return new Flight(resultSet.getInt("id"), resultSet.getString("from_airport"),
					resultSet.getString("to_airport"), resultSet.getDate("departure"), resultSet.getDate("arrival"),
					resultSet.getString("flight_number"));

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	// -------------- RETRIEVE INFORMATION BY FLIGHT NUMBER
	@Override
	public List<Flight> findFlightByFlightNumber(String flightNum) throws SQLException {
		
		List<Flight> flightsInfo = new LinkedList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			String sql = "SELECT id, from_airport, to_airport, departure, arrival, flight_number "
					+ "FROM " + tableName + " where flight_number = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, flightNum);
			
			ResultSet resultSet = statement.executeQuery(); 

			while (resultSet.next()) {

				Flight flight = new Flight(resultSet.getInt("id"), resultSet.getString("from_airport"),
						resultSet.getString("to_airport"), 
						resultSet.getDate("departure"), resultSet.getDate("arrival"),
						resultSet.getString("flight_number"));

				flightsInfo.add(flight);
			}
			System.out.println("INSIDE OF FINDFLIGHT..() " + flightsInfo);
			return flightsInfo;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

//		try (Connection connection = DriverManager.getConnection(url, username, password)) {
//
//			String sql = "SELECT id, from_airport, to_airport, departure, arrival, flight_number "
//					+ "FROM flight_data where flight_number = ?";
//
//			PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setString(1, flightNum);
//
//			ResultSet resultSet = statement.executeQuery(); // all results that came back from db
//			resultSet.next();
//			return new Flight(resultSet.getInt("id"), resultSet.getString("from_airport"),
//					resultSet.getString("to_airport"), resultSet.getDate("departure"), resultSet.getDate("arrival"),
//					resultSet.getString("flight_number"));
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}

	}

	// -------------- DELETE INFORMATION BY FLIGHT NUMBER
	@Override
	public void deleteByFlightNumber(String flightNum) throws SQLException {
		deleteByFlightNumber_tableName(flightNum, this.tableName);
	}

	public void deleteByFlightNumber_tableName(String flightNum, String tableName) throws SQLException {

		Connection connection = DriverManager.getConnection(url, username, password);

		try {

			connection.setAutoCommit(false); // tx begins

			String sql = "DELETE FROM "+ tableName +" WHERE flight_number = ?";

			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setString(1, flightNum);

			int rows = stmt.executeUpdate();

			// if operation was successful
			if (rows == 1) {
				connection.commit();
				System.out.println("Flight was successfully deleted!");
			} else {
				throw new SQLException("The flight deletion failed");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
		}
//		 finally {
//			try {
//				System.out.println("Resetting default commit behavior");
//				connection.setAutoCommit(true);
//			} catch (SQLException e) {
//				System.out.println("Couldn't reset auto-commit " + e.getMessage());
//			}
//		}

	}

	// ------------------ UPDATE FLIGHT INFORMATION BY FLIGHT NUMBER
	@Override
	public void updateFlight(Flight flight) throws SQLException {
		updateFlight_tableName(flight, this.tableName);
	}
	
	public void updateFlight_tableName(Flight flight, String tableName) throws SQLException {

		Connection connection = DriverManager.getConnection(url, username, password);

		try {

			connection.setAutoCommit(false); // tx begins

			String sql = "UPDATE " + tableName + " SET from_airport = ?, to_airport = ?, " + "departure = ?, arrival = ? "
					+ "WHERE flight_number = ?";

			PreparedStatement stmt = connection.prepareStatement(sql);

			java.util.Date dateDep = sdf1.parse(flight.getDeparture());
			java.sql.Date sqlDateDep = new java.sql.Date(dateDep.getTime());

			java.util.Date dateArr = sdf1.parse(flight.getArrival());
			java.sql.Date sqlDateArr = new java.sql.Date(dateArr.getTime());
			

			stmt.setString(1, flight.getFromAirport());
			stmt.setString(2, flight.getToAirport());
			stmt.setDate(3, dateConversion(sqlDateDep));
			stmt.setDate(4, dateConversion(sqlDateArr));
			stmt.setString(5, flight.getFlightNumber());

			int rows = stmt.executeUpdate();

			// if operation was successful
			if (rows == 1) {
				connection.commit();
				System.out.println("Flight was successfully updated!");
			} else {
				throw new SQLException("The flight deletion failed");
			}

		} catch (SQLException | ParseException e) {
			e.printStackTrace();
			connection.rollback();
		}


	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	// add 1 day
	private java.sql.Date dateConversion(Date depD) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cD = Calendar.getInstance();

		try {
			cD.setTime(sdf.parse(depD.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		cD.add(Calendar.DAY_OF_MONTH, 1);
		// Date after adding the days to the given date
		String newDepDate = sdf.format(cD.getTime());
		
		java.util.Date dateArr;
		java.sql.Date sqlDateArr = null;
		
		try {
			dateArr = sdf1.parse(newDepDate);
			sqlDateArr = new java.sql.Date(dateArr.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return sqlDateArr;
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
	
	private static final String password = psw.getPassword();
}

//=================================================================================
//=================================================================================

interface FlightDAOInterface {

	// create
	public void addFlight(Flight flight) throws SQLException;

	// retrieve all
	public List<Flight> findAllFlights() throws SQLException;

	// retrieve by id
	public Flight findFlightById(int id) throws SQLException;

	// retrieve by flight number
	public List<Flight> findFlightByFlightNumber(String flightNum) throws SQLException;

	// delete by flight number
	public void deleteByFlightNumber(String flightNum) throws SQLException;

	// update
	public void updateFlight(Flight flight) throws SQLException;

}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
