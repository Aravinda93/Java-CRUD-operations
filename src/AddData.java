import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import echobot1.MySQLConnection;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

/**
 * Servlet implementation class AddData
 */
@WebServlet("/AddData")
public class AddData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	//Add the new company data into the DB
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//Read the input data from the AngularJS
			StringBuffer jb = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}			
			JSONObject jsonObject =  new JSONObject(jb.toString());
			
			//Prepare and Execute the statement with Query
			Connection conn =  MySQLConnection.getConnection();
			PreparedStatement AddStatement = conn.prepareStatement("INSERT INTO TrailProject (COMPANY_NAME, LEGAL_ENTITY, EMPLOYEES, CAPITAL) values(?, ?, ?, ?)"); 
			AddStatement.setString(1, jsonObject.getJSONObject("CompanyDetails").getString("FirmName")); 
			AddStatement.setString(2, jsonObject.getJSONObject("CompanyDetails").getString("LegalEntity")); 
			AddStatement.setInt(3, jsonObject.getJSONObject("CompanyDetails").getInt("Employees"));
			AddStatement.setInt(4, jsonObject.getJSONObject("CompanyDetails").getInt("ShareCapital"));
			AddStatement.executeUpdate();
			AddStatement.close(); 
			MySQLConnection.closeConnection();
		} catch (Exception ex) {
			System.out.println("Error in Adding the Data to DB");
		    ex.printStackTrace();
		}
	}

}
