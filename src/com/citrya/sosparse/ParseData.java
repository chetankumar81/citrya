package com.citrya.sosparse;

import java.sql.Connection;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.citrya.dao.DatabaseAccess;

public class ParseData {
	private Connection currentCon;
	private String shopname;
	
	public ParseData(String shopname)
	{
		this.shopname = shopname;
	}
	
	public String[] getResult(String text)
	{
		String[] result = new String[3]; //for storing all 3 values
		/*Object obj1 =JSONValue.parse(response);  
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
		//result[0]= text;*/
		
		ParseMatchData parsedata = new DatabaseAccess().getmatchdetails(shopname);
		String returnvalue="";
		String[] lines = text.split("\n");
		for (int i1=0;i1<lines.length;i1++)
		{
			//printing line and line number
			//System.out.println("line no: "+i1+" and line: "+lines[i1]);
			//invoice number.
			String[] tempinvseq = parsedata.billnoparse.split("##");
			
			if ((lines[i1].contains(tempinvseq[0]))||(lines[i1].contains(tempinvseq[1]))||(lines[i1].contains(tempinvseq[2])))
			{
				System.out.println("inv lines["+i1+"]"+lines[i1]);
				String[] temp = lines[i1].split(" ");
				result[0] = temp[temp.length-1];
			}
					
			//date
			String[] tempdateseq = parsedata.dateparse.split("&");
			if ((lines[i1]).contains(tempdateseq[0])) {
				System.out.println(" date lines[i1]"+lines[i1]);
			if (tempdateseq[1].equalsIgnoreCase("1"))
					{
				String[] temp = lines[i1].split(" ");
				if (temp.length==4)
				
				result[1] = temp[temp.length-3]+"-"+temp[temp.length-2];
				else
					result[1] = temp[temp.length-2];
					}
			else if(tempdateseq[1].equalsIgnoreCase("3")){
				result[1] = lines[i1];
			}
			}
			
			// amount
			
			String[] tempamountseq = parsedata.amounparse.split("&");
			
			if ((lines[i1]).contains(tempamountseq[0])) {
				System.out.println("cash lines[i1]"+lines[i1]);
			
				result[2] =lines[i1+new Integer(tempamountseq[2]).intValue()];
				
			}
		}
		return result;
		
	}
}
