package com.edlore.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class MainTest {

	private static String GET_USER_ID = "select UserId from Box_User_Details where UserName = ?";
	public static void main(String[] args) {
		System.out.println(ConnectionFactory.getConnection());
		
		getUserId("p8admin");
	}
	
	static String getUserId(String userName)
	{
		 String userId = null;
  	   Connection connection = null;
  	   PreparedStatement statement = null;
  	   ResultSet resultSet = null;
  	   
  	   connection =   ConnectionFactory.getConnection();
  	   System.out.println("Enter into Box connection");
  	  try {
  		 System.out.println("with in block-----");
			statement =  connection.prepareStatement(GET_USER_ID);
			
			System.out.println("statement is --------"+ statement);
			statement.setString(1, userName);
			resultSet = statement.executeQuery();
			resultSet.next();
			userId = resultSet.getString(1);
			System.out.println("User name is :: "+ userName +"  User Id is :: "+userId);
		} catch (SQLException e) {
			System.out.println("with in sql exception");
			e.printStackTrace();
		}
	return userId;
	}
}
