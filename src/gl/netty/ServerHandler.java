package gl.netty;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import gl.presto.Presto;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ServerHandler extends SimpleChannelInboundHandler<String>{	
	private static Logger logger = Logger.getLogger(ServerHandler.class); 	
	private Presto presto;
	
	public ServerHandler(String Pip,String Pport,String Pusername,String Ppasswd){		
		this.presto = new Presto(Pip,Pport,Pusername,Ppasswd); 
	}
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    	String res = "";
		try{
			printInfo("sql:"+msg+".");	    	 	
			ResultSet rs = presto.exec(msg);			
			res = analyseRs(rs);
			
		}catch(SQLException e){
			res += e.getMessage();			
		}catch(InterruptedException e){
			res+=e.getMessage();
		}finally{
			printInfo(res);
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

    private String analyseRs(ResultSet rs) throws SQLException,InterruptedException{
    	if(null == rs) Thread.sleep(500);   	
    	if(null == rs) return "rs is null";
    	
    	ResultSetMetaData rmd = rs.getMetaData();
    	int columnNum=rmd.getColumnCount();    	
    	String res="";
    	
    	//循环枚举出所有字段名
		for(int i=0;i<columnNum;i++){			
			if(i != 0){
				res+=",";
			}
			
	          //第一个字段名从1开始
	    	   res += rmd.getColumnName(i+1);	    	   	    	   
		}
    	
		//字段名输出完成换行
    	res+="\n";
		
    	try {
			while (rs.next()) {				
			
				for(int i=0;i<columnNum;i++){			
					if(i != 0){
						res+=",";
					}
					
			          //第一个字段名从1开始
			    	   res += rs.getString(i+1);	    	   	    	   
				}
				res+="\n";
								
			}
			
			rs.close();
		} catch (SQLException e) {			
			e.printStackTrace();
			return e.getMessage();
		} 
    	
    	return res;
  	
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
