package com.citrya.sosparse;

import com.citrya.dao.DatabaseAccess;

public class ParseData {
	private String shopname;

	public ParseData(String shopname)
	{
		this.shopname = shopname;
	}

	public String[] getResult(String text)
	{
		String[] result = new String[3]; //for storing all 3 values

		ParseMatchData parsedata = new DatabaseAccess().getmatchdetails(shopname);
		String[] lines = text.split("\n");
		if(parsedata.normalparse == 1){
			for (int i1=0;i1<lines.length;i1++)
			{
				//printing line and line number
				//System.out.println("line no: "+i1+" and line: "+lines[i1]);
				//invoice number.
				String[] tempinvseq = parsedata.billnoparse.split("##");
				for (int j=0;j<tempinvseq.length-1;j++)
				{
					if (lines[i1].contains(tempinvseq[j]))
					{
						System.out.println("inv lines[i1]"+lines[i1]);
						String[] temp = lines[i1].split(" ");
						result[0] = temp[temp.length-1];
					}
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
				if ((lines[i1]).contains(tempamountseq[0])){
					System.out.println("cash lines[i1]"+lines[i1]);

					result[2] =lines[i1+new Integer(tempamountseq[2]).intValue()];
				}
			}	
		}else if(parsedata.normalparse == 2){
			for (int i1=0;i1<lines.length;i1++)
			{
				//This is backrefernce grouping in regular expression;
				//What it did is first pick the line which matches this regular expression and then store the result what you want
				//in that line as group 1 and get that group 1 using $1 as a second parameter in replaceAll method

				//invoice number.
				if(lines[i1].matches(parsedata.billnoparse)){
					result[0] = lines[i1].replaceAll(parsedata.billnoparse,"$1"); 
				}

				//date
				if(lines[i1].matches(parsedata.dateparse)){
					result[1] = lines[i1].replaceAll(parsedata.dateparse,"$1"); 
				}

				// amount
				String[] tempamountseq = parsedata.amounparse.split("&");
				if ((lines[i1]).equals(tempamountseq[0])) {
					if(parsedata.shopname.equals("shoppers stop")){
						String[] temp = tempamountseq[2].split("/");
						if(!Character.isLetter(lines[i1+new Integer(temp[0]).intValue()].charAt(0))){
							result[2] = lines[i1+new Integer(temp[0]).intValue()];
						}	
						else{
							result[2] = lines[i1+new Integer(temp[1]).intValue()];
							System.out.println("cash lines["+i1+"]"+lines[i1]);
						}
							
					}else{
						System.out.println("cash lines["+i1+"]"+lines[i1]);
						result[2] =lines[i1+new Integer(tempamountseq[2]).intValue()];
					}
				}
			}
		}
		return result;
	}
}
