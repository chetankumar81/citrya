package com.citrya.sosshop;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.citrya.dao.DatabaseAccess;

public class getInvoiceLables extends HttpServlet {
	private static final long serialVersionUID = 1L;   

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String> ar = new ArrayList<String>();
		ar = new DatabaseAccess().getInvoiceLables();
		response.getWriter().append("Served at: ").append(ar.get(0));
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
