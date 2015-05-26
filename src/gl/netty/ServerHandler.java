package gl.netty;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import gl.presto.Presto;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ServerHandler extends SimpleChannelInboundHandler<String>{	
	private static Logger logger = Logger.getLogger(ServerHandler.class); 
		
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    	String res = "";
		try{
			System.out.println("msg:"+msg+".");
	    	Presto joyp = new Presto("42.62.84.100","8080","root",null);   	
			ResultSet rs = joyp.exec(msg);			
			
			if(null == rs){
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){
					res+=e.getMessage();
				}
			}
			
			if(null != rs){	
				try {
					while (rs.next()) {
						res+=rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+"\n";
						//System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3));
						}
					rs.close();
				} catch (SQLException e) {			
					e.printStackTrace();
					res+=e.getMessage();
				} 
			}else{				
				res+="error! ResultSet is null";
			}			
			
		}finally{
			final ChannelFuture f = ctx.writeAndFlush(res);
	        f.addListener(new ChannelFutureListener() {
	            @Override
	            public void operationComplete(ChannelFuture future) {
	                assert f == future;
	                ctx.close();
	            }
	        });
		}    	
                
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {        
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
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
