package com.jaga;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		 String url = "jdbc:mysql://localhost:3306/jaga";
	        String user = "root";
	        String password = "root";
	 
	      //  String filePath = "C:\\Users\\jagadeesh\\Pictures\\Saved Pictures\\raju.jpg";
	 
	        try {
	        	try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  Connection conn = DriverManager.getConnection(url, user, password);
	   	 
     String sql = "SELECT photo FROM contacts1111 WHERE first_name=?";
     PreparedStatement statement = conn.prepareStatement(sql);
     statement.setString(1, request.getParameter("firstName"));
     //statement.setString(2, "Pillela");

     ResultSet result = statement.executeQuery();
     if (result.next()) {
       
         byte[] imgData = result.getBytes("photo");//Here r1.getBytes() extract byte data from resultSet 
        
       /*  //to save Image file
         response.setContentType("image/jpg");
        
         System.out.println("image Data"+imgData);
       response.setHeader("expires", "0");
     */  
      //  OutputStream      outputStream= response.getOutputStream();
         
       
         
         
       //we can use ServletOutputSream insted of OutputStream
         ServletOutputStream      outputStream= response.getOutputStream();
          
         outputStream.write(imgData);
         outputStream.flush();
         outputStream.close();

         }else {
				PrintWriter out=response.getWriter();
         	out.println("image not found for given Name"+request.getParameter("firstName"));
         return;
         	
			}
	
	

} catch (SQLException ex) {
   ex.printStackTrace();
} 
	}

	

}
