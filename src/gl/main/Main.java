package gl.main;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gl.netty.Server;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class);	
	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", "8322"));
	
	public static void main(String[] args) {
				
		logger.debug("main run,port:"+PORT+",ssl:"+SSL);
		ApplicationContext ctx = new ClassPathXmlApplicationContext("gl/conf/applicationContext.xml");							
		Server server = (Server)ctx.getBean("server");	
		server.run();					
		System.exit(0);
	}
	
}
