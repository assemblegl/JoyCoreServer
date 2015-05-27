package gl.main;

import org.apache.log4j.Logger;

import gl.netty.Server;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class);	

	public static void main(String[] args) {
		if(args.length < 6){
			print();
			System.exit(1);;
		}
		
		boolean ssl;
		
		//int Sport,boolean ssl,String Pip,String Pport,String Pusername,String Ppasswd
		if("1".equals(args[1])){
			ssl = true;
		}else{
			ssl = false;
		}
		
		int sport = 0;
		try{
			sport = Integer.valueOf(args[0]);
		}catch(NumberFormatException e){			
			
		}		
		
		Server server = new Server(sport,ssl,args[2],args[3],args[4],args[5]);	
		server.run();					
		System.exit(0);
	}
	
	public static void print(){
		System.out.println(" Sport ssl Pip Pport Pusername Ppasswd");
	}
	
}
