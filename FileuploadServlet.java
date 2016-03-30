package com.jaga;

import java.io.IOException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;

//@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)
public class FileuploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   

	// database connection settings
    private String dbURL = "jdbc:mysql://localhost:3306/jaga";
    private String dbUser = "root";
    private String dbPass = "root";	
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		  String firstName = request.getParameter("firstName");
		        String lastName = request.getParameter("lastName");
		        
		         
		        InputStream inputStream = null; // input stream of the upload file
		         
	        // obtains the upload file part in this multipart request
	       Part filePart = request.getPart("photo");
	        if (filePart != null) {
		            // prints out some information for debugging
	            System.out.println(filePart.getName());
		            System.out.println(filePart.getSize());
		            System.out.println(filePart.getContentType());
		             
		            // obtains input stream of the upload file
		            inputStream = filePart.getInputStream();
		        }
		         
		        Connection conn = null; // connection to the database
		        String message = null;  // message will be sent back to client
		         
		        try {
	            // connects to the database
		            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
		 
		            // constructs SQL statement
		           String sql = "INSERT INTO contacts1111 (first_name, last_name, photo) values (?, ?, ?)";
		            
		       //     String sql = "SELECT photo FROM persons11 WHERE first_name=? AND last_name=?";
			        
		            PreparedStatement statement = conn.prepareStatement(sql);
		            statement.setString(1, firstName);
		            statement.setString(2, lastName);
		             
		            if (inputStream != null) {
	                // fetches input stream of the upload file for the blob column
		                statement.setBytes(3, IOUtils.toByteArray(inputStream));
		                
		               // statement.setBlob(3, inputStream);
	                	//   statement.setBinaryStream(3, inputStream);
	               
		                }
		 
		            // sends the statement to the database server
		            int row = statement.executeUpdate();
		            if (row > 0) {
		                message = "File uploaded and saved into database";
		            }
		        } catch (SQLException ex) {
		            message = "ERROR: " + ex.getMessage();
		            ex.printStackTrace();
	       } finally {
		            if (conn != null) {
		                // closes the database connection
	                try {
		                    conn.close();
		                } catch (SQLException se) {
		                    se.printStackTrace();
		                }
		       	} 
		            request.setAttribute("Message", message);
	          
	            // forwards to the message page
		             request.getServletContext().getRequestDispatcher("/Message.jsp").forward(request, response);
		          // getServletContext().getRequestDispatcher("/Message.jsp").forward(request, response);
	        }
		
		
	}
}
