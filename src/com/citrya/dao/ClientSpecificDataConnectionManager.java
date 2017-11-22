package com.citrya.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ClientSpecificDataConnectionManager {
	Connection con;
	static String url;

	public  Connection getConnection() throws SQLException
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			try
			{           
				con = DriverManager.getConnection("jdbc:mysql://localhost:" + "3306" + "/" + "citryaintern" + "?autoReconnect=false", "root", "");
			}	

			catch (SQLException ex)
			{
				System.out.println("SQLException" +ex.getCause());
				throw ex;
			}
		}

		catch(ClassNotFoundException e)
		{
			System.out.println("ClassNotFoundException"+e.getCause());
		}

		return con;
	}
}
