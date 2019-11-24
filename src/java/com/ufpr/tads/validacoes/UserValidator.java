/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.validacoes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ufpr.tads.bean.Login;
import com.ufpr.tads.dao.UserDao;
import net.sf.json.JSONObject;

import java.lang.Object;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
/**
 * Servlet implementation class UserValidator
 */
@WebServlet("/UserValidator")
public class UserValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserValidator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> hm = new HashMap<String, String>();
		String login = request.getParameter("login");
		String password = request.getParameter("senha");
		String msg;
		
		Login loginBD = new Login();
		loginBD.setNome(login);
		loginBD.setSenha(password);
		
		UserDao userdao;
		userdao = new UserDao();
		try {
			Login feito = userdao.getLogin(loginBD);//vai mandar o usario e senha se encontrar devolve os mesmos...
			if(feito.getNome().equals(login) && feito.getSenha().equals(password)){
				msg = "Login correto";
			}else{
				msg = "Login errado";
			}
			hm.put("message", msg);
			//Cada chave do HashMap vira uma chave do JSON
			//JSONObject json = JSONObject.fromObject(hm);
			JSONObject json = JSONObject.fromObject(hm);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
