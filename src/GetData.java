import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import echobot1.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/GetData")
public class GetData extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Get the Data from the Database when the page is loaded
		JSONArray companies = new JSONArray();

		try {
			Connection conn = MySQLConnection.getConnection();
			String GetQuery = "SELECT * FROM TrailProject";
			Statement GetStatement = conn.createStatement();
			ResultSet GetResult = GetStatement.executeQuery(GetQuery);
			while (GetResult.next()) {
				JSONObject GetCompanyDetails = new JSONObject();
				GetCompanyDetails.put("ID", GetResult.getInt("ID"));
				GetCompanyDetails.put("COMPANY_NAME", GetResult.getString("COMPANY_NAME"));
				GetCompanyDetails.put("LEGAL_ENTITY", GetResult.getString("LEGAL_ENTITY"));
				GetCompanyDetails.put("EMPLOYEES", GetResult.getInt("EMPLOYEES"));
				GetCompanyDetails.put("CAPITAL", GetResult.getInt("CAPITAL"));
				companies.put(GetCompanyDetails);
			}
			MySQLConnection.closeConnection();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(companies.toString());
			GetResult.close();
		} catch (Exception ex) {
			System.out.println("Error in Getting the Data");
			ex.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String Type = request.getParameter("Type");		
		if (Type.equals("Edit")) {
			//Get the information for modification of the exisiting company
			String Edit_Id = request.getParameter("Edit_Id");
			try {
				Connection conn = MySQLConnection.getConnection();
				String query = "SELECT * FROM TrailProject WHERE ID = " + Edit_Id;
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query);
				JSONObject ModificationData = new JSONObject();
				while (rs.next()) {
					ModificationData.put("ID", rs.getInt("ID"));
					ModificationData.put("COMPANY_NAME", rs.getString("COMPANY_NAME"));
					ModificationData.put("LEGAL_ENTITY", rs.getString("LEGAL_ENTITY"));
					ModificationData.put("EMPLOYEES", rs.getInt("EMPLOYEES"));
					ModificationData.put("CAPITAL", rs.getInt("CAPITAL"));
				}
				MySQLConnection.closeConnection();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(ModificationData.toString());
				rs.close();
			} catch (Exception ex) {
				System.out.println("Error in Getting the Data for Modification");
				ex.printStackTrace();
			}
		} else if(Type.equals("Delete")) {
			//Delete the the company details
			String Delete_ID = request.getParameter("Delete_ID");
			try {
				Connection conn = MySQLConnection.getConnection();
				PreparedStatement  DeleteStatement = conn.prepareStatement("DELETE FROM TrailProject WHERE ID = " + Delete_ID);
				DeleteStatement.executeUpdate();
				MySQLConnection.closeConnection();
				JSONObject DeletionData = new JSONObject();
				DeletionData.put("Status",200);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(DeletionData.toString());
			}catch (Exception ex) {
				System.out.println("Error during the deletion of the Data");
				ex.printStackTrace();
			}
			
		}
	}
}