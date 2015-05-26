package gl.presto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Presto {
	private Connection connection;
	
	static{
		try {
			Class.forName("com.facebook.presto.jdbc.PrestoDriver");
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
	}
	
	public Presto(String ip,String port,String user,String pass){
		try {
			connection = DriverManager.getConnection("jdbc:presto://"+ip+":"+port,user,pass);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
	}
	
	public ResultSet exec(String sql){
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} 		
		
	}	
	
	public void close(){
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {				
				System.out.println(e.getMessage());
			}
		}
	}
	

}
