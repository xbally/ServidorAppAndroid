/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.bean;

import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public class Login implements Serializable {
	
	private int usuario;
	private int votou;
	private String senha;
	private String nome;
	private String filme;
	private String diretor;
	
	public Login(){}
	
	public Login(String nome, String senha){
		this.nome = nome;
		this.senha = senha;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public int getVotou() {
		return votou;
	}

	public void setVotou(int votou) {
		this.votou = votou;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFilme() {
		return filme;
	}

	public void setFilme(String filme) {
		this.filme = filme;
	}

	public String getDiretor() {
		return diretor;
	}

	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}


	
	
	
	
}
