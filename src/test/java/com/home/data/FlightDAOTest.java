package com.home.data;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.sql.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.home.beans.Flight;

// ------- 	TEST METHODS	---------

public class FlightDAOTest {

	private static final String url = "jdbc:mysql://localhost:3306/flight_schema" + "?serverTimezone=UTC";
	private static final String username = "root";

	private static  List<Flight> actualList = new LinkedList<>();
	private static FlightDAO dao = new FlightDAO();
	private static String testTableName = "flight_dao_test";

	// runs once before all tests
	@BeforeClass
	public static void setupBeforeClass() throws SQLException, ClassNotFoundException {

		Class.forName("com.mysql.cj.jdbc.Driver");

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			String sql = "create table " + testTableName + " (\n" + "	id INT AUTO_INCREMENT,\n"
					+ "	from_airport VARCHAR(50),\n" + "	to_airport VARCHAR(50),\n" + "	departure DATE,\n"
					+ "	arrival DATE,\n" + "	flight_number VARCHAR(50), PRIMARY KEY (id) \n" + ");";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		}

		String sqlString = " insert into " + testTableName
				+ "(from_airport, to_airport, departure, arrival, flight_number) "
				+ "values ('abc', 'bca', null, null, 'test')";

		// create row
		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			PreparedStatement statement = connection.prepareStatement(sqlString);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		}


	}

	// executes before each test
	@Before
	public void setup() throws SQLException {


	}

	// ---------------- FIND FLIGHT BY FLIGHT NUMBER --------
	@Test
	public void test1() throws SQLException {
		actualList = dao.findFlightByFlightNumber("59779-799");

		List<Flight> expectedList = new LinkedList<>();
		expectedList
				.add(new Flight(1, "PKR", "ZAR", Date.valueOf("2020-11-25"), Date.valueOf("2020-12-25"), "59779-799"));

		assertEquals(expectedList, actualList);


	}

	// ---------------- ADD FLIGHT ----------
	@Test
	public void test2() throws SQLException {

		// add new
		Flight flight = new Flight("testing1", "testing2", Date.valueOf("2020-11-25"), Date.valueOf("2020-12-25"),
				"testing3");

		dao.addFlight_tableName(flight, testTableName);

		int count = 0;

		// check number of rows
		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			Statement stmt = connection.createStatement();
			String query = "select count(*) from " + testTableName;
			ResultSet rs = stmt.executeQuery(query);
			// Retrieving the result
			rs.next();
			count = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();

		}

		assertEquals(2, count);

	}

	// ----------------- UPDATE FLIGHT --------
	@Test
	public void test3() throws SQLException {

		Flight flight = new Flight(1, "testing2", "testing1", Date.valueOf("2020-11-25"), Date.valueOf("2020-12-25"),
				"test");

		dao.updateFlight_tableName(flight, testTableName);

		// expected
		Flight flightUpd = new Flight(1, "testing2", "testing1", Date.valueOf("2020-11-25"), Date.valueOf("2020-12-25"),
				"test");

		List<Flight> myExpectedList = new LinkedList<>();
		myExpectedList.add(flightUpd);

		List<Flight> myActualList = new LinkedList<>();
		myActualList.add(dao.findFlightById_tableName(1, testTableName));

		assertEquals(myExpectedList, myActualList);

	}

	// ---------------- DELETE FLIGHT ----------
	@Test
	public void test4() throws SQLException {

		dao.deleteByFlightNumber_tableName("test", testTableName);

		int count = 0;

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			Statement stmt = connection.createStatement();
			String query = "select count(*) from " + testTableName;
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			count = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();

		}

		assertEquals(1, count);

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	// executes after each test
	@After
	public void tearDown() {
		// TODO Auto-generated method stub

	}

	// runs once after all tests
	@AfterClass
	public static void tearDownAfterClass() {
		// drop table

		try (Connection connection = DriverManager.getConnection(url, username, password)) {

			String sql = "drop table " + testTableName;

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

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

//
//
//
//
//
//
