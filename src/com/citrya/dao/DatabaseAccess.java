package com.citrya.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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

	public String getTextValueOfImage(String response){
		Object obj1 =JSONValue.parse(response);  
		JSONObject jsonObject = (JSONObject) obj1;
		JSONArray responsearray = (JSONArray) jsonObject.get("responses");
		@SuppressWarnings("rawtypes")
		Iterator i =responsearray.iterator();
		String text= null;
		while (i.hasNext()) {
            JSONObject obj2 = (JSONObject) i.next();
             JSONObject obj3=(JSONObject)obj2.get("fullTextAnnotation");
             text=(String)obj3.get("text");
        }
		return text;
	}
	public String getInvoice(String text)
	{
		String invoiceNo = "";
		int i1 =text.lastIndexOf("Invoice No:");
		//Here based on the company we know the length of the invoice no or we can use Regular Expression
		//also to search for the invoice no; because for each invoice the format is different there is no pattern
		for(int j=i1+12;j<=i1+25;j++)
		{
			invoiceNo=invoiceNo+text.charAt(j);
		}
		return invoiceNo;
	}
}
