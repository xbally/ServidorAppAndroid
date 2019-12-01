package com.ufpr.tads.dao;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;

import com.ufpr.tads.bd.ConnectionFactory;
import com.ufpr.tads.bean.Login;
public class UserDao {
	
	private final String stmtGetLogin = "SELECT * FROM usuario WHERE nome=? AND senha=?";
	private final String stmtConfirmaVotoBD = "UPDATE usuario SET filme=?, diretor=?, votou=? WHERE nome=?";
	private final String stmtCheckVoto = "SELECT votou FROM usuario WHERE nome=?";
	
	public Login getLogin(Login login) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
		updateUserToken(login, (int)Math.random()*101);
		Login usuarioRetornado = getUserByLoginAndPassword(login.getNome(), login.getSenha());
		if(usuarioRetornado == null) {
			usuarioRetornado = new Login("Inexistente");
		}
        return usuarioRetornado;
    }

	public Login getUserByLoginAndPassword(String login, String password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String sql = "SELECT usuario, votou, token, senha, nome, voto_filme, voto_diretor from users where login=? and password=?;";
		try(Connection conn = ConnectionFactory.getConnection()) {
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setString(1,login);
	        stmt.setString(2,password);
	
	        try(ResultSet res = stmt.executeQuery()) {
		        if(res.next()) {
		        	Login user = new Login(res.getInt("usuario"), res.getInt("votou"), res.getInt("token"), res.getString("senha")
		        			, res.getString("nome"), res.getString("voto_filme"), res.getString("voto_diretor"));
		        	return user;
		        }
	        }
		} catch (Exception e) {
			throw new RuntimeException("Erro ao consultar cadastro de usuario. Origem="+e.getMessage());
		}
	
        return null;
    }
	
    public void updateUserToken(Login usuario, int token)  throws SQLException {
    	String login = usuario.getNome();
    	String senha = usuario.getSenha();
        String sql = "UPDATE users SET token=? WHERE nome=? AND senha=?;";
        try(Connection conn = ConnectionFactory.getConnection()) {
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setString(1, String.valueOf(token));
	        stmt.setString(2, login);
	        stmt.setString(3, senha);
	
	        stmt.executeUpdate();
        } catch (Exception e) {
        	throw new RuntimeException("Erro ao gerar Token de usu�rio. Origem="+e.getMessage());
		}
    }
	
	public Login confirmaVoto(Login confirmaVoto) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		Connection con = null;
        PreparedStatement stmt = null;
        
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtConfirmaVotoBD, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, confirmaVoto.getFilme());
            stmt.setString(2, confirmaVoto.getDiretor());
            stmt.setInt(3, 1);
            stmt.setString(4, confirmaVoto.getNome());
            
            stmt.executeUpdate();//grava no banco
            con.commit();
            
            confirmaVoto.setVotou(1);
            return confirmaVoto;
            
        }catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){ex1.printStackTrace();  System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao confirmar voto no banco de dados. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
        
        
	}

	public int checkVoto(Login login) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		Connection con = null;
        PreparedStatement stmt = null;
        int checkVoto;
        ResultSet rs = null;
        
        try {
            con = new ConnectionFactory().getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtCheckVoto, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, login.getNome());
            rs = stmt.executeQuery();
            rs.next();
            checkVoto = rs.getInt("votou");
            return checkVoto;
            
        }catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){ex1.printStackTrace();  System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new RuntimeException("Erro ao confirmar voto no banco de dados. Origem="+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar stmt. Ex="+ex.getMessage());};
            try{con.close();;}catch(Exception ex){ex.printStackTrace();  System.out.println("Erro ao fechar conexão. Ex="+ex.getMessage());};
        }
	}
	
}
