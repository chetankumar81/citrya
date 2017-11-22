package com.citrya.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseAccess {
	private Connection currentCon;

	public ArrayList<String> getInvoiceLables() {
		try
		{
			currentCon = new ClientSpecificDataConnectionManager().getConnection();
			String query = "select * from invoice";
			PreparedStatement statement = currentCon.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			ArrayList<String> ar = new ArrayList<String>();
			while (resultSet.next())
			{

				String label = resultSet.getString("invoice_label");
				ar.add(label);
			}
			statement.close();
			return ar;
		}
		catch (Exception ex) 
		{
			System.out.println("Unable to retrieve Invoice Lables : An Exception has occurred! " + ex);
		}
		finally 
		{
			if (currentCon != null) {
				try {
					currentCon.close();
				} catch (Exception e) {
				}

				currentCon = null;
			}
		}
		return null;
	}
}
