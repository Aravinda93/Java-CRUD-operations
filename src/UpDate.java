
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import echobot1.MySQLConnection;

/**
 * Servlet implementation class UpDate
 */
@WebServlet("/UpDate")
public class UpDate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			StringBuffer UpdatingData = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				UpdatingData.append(line);
			}
			JSONObject UpdatingDataJson = new JSONObject(UpdatingData.toString());
			
			//Prepare the Update statement and update the values
			Connection conn = MySQLConnection.getConnection();
			PreparedStatement UpdateStatement = conn.prepareStatement("UPDATE TrailProject SET COMPANY_NAME = ?, LEGAL_ENTITY = ?, EMPLOYEES = ?, CAPITAL = ? WHERE ID = ?");
			UpdateStatement.setString(1, UpdatingDataJson.getJSONObject("CompanyDetails").getString("FirmName"));
			UpdateStatement.setString(2, UpdatingDataJson.getJSONObject("CompanyDetails").getString("LegalEntity"));
			UpdateStatement.setInt(3, UpdatingDataJson.getJSONObject("CompanyDetails").getInt("Employees"));
			UpdateStatement.setInt(4, UpdatingDataJson.getJSONObject("CompanyDetails").getInt("ShareCapital"));
			UpdateStatement.setInt(5, UpdatingDataJson.getJSONObject("CompanyDetails").getInt("EditIDSave"));
			UpdateStatement.executeUpdate();
			MySQLConnection.closeConnection();
			
			JSONObject UpdateResponse = new JSONObject();
			UpdateResponse.put("Status", 200);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(UpdateResponse.toString());

		} catch (Exception ex) {
			System.out.println("Error during the updating of the Data");
			ex.printStackTrace();
		}
	}

}
