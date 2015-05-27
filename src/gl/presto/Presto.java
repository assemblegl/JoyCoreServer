package gl.presto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Presto {
	private Connection connection;
	private String port;
	private String ip;
	private String passwd;
	private String username;
	
	static{
		try {
			Class.forName("com.facebook.presto.jdbc.PrestoDriver");
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
	}
	
	public Presto(String ip,String port,String username,String passwd){
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.passwd = passwd;				 
	}
	
	public ResultSet exec(String sql){
		Statement stmt;
		try {
			if(null == connection ||  connection.isValid(0)){
				connection = DriverManager.getConnection("jdbc:presto://"+ip+":"+port,username,passwd);
			}
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
