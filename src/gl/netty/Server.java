package gl.netty;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import org.apache.log4j.Logger;

import gl.global.Context;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class Server {
	private static Logger logger = Logger.getLogger(Server.class);	
	private int Sport;
	private boolean ssl;
	private String Pport;
	private String Pip;
	private String Ppasswd;
	private String Pusername;
	
	public Server(int Sport,boolean ssl,String Pip,String Pport,String Pusername,String Ppasswd){
		this.Sport = Sport;
		this.ssl = ssl;
		this.Pip = Pip;
		this.Pport = Pport;
		this.Pusername = Pusername;
		this.Ppasswd = Ppasswd;
	}
	
    public int run()  {
        // Configure SSL.
        final SslContext sslCtx;
        if (ssl) {
        	printInfo("create ssl");
            SelfSignedCertificate ssc;
			try {
				ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} catch (CertificateException e) {				
				printError(e.getMessage());
				return Context.error;
			} catch(SSLException e){
				printError(e.getMessage());
				return Context.error;
			}
            
        } else {
        	printInfo("no ssl");
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	printInfo("Server,create server");
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ServerInitializer(sslCtx,Pip,Pport,Pusername,Ppasswd));
            b.bind(Sport).sync().channel().closeFuture().sync();           							
        } catch (InterruptedException e) {				
			printError(e.getMessage());
			return Context.error;
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        
        return Context.success;
    }
    
	public void printError(String Content){		
		String ec=this.getClass().getName()+","+Content;				
		System.out.println(ec);
		logger.error(ec);
	}
	
	public void printInfo(String Content){
		String ec=this.getClass().getName()+","+Content;		
		System.out.println(ec);
		logger.info(ec);
	}

}
