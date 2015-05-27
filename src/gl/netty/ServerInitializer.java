package gl.netty;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class ServerInitializer extends ChannelInitializer<SocketChannel>{
	private static Logger logger = Logger.getLogger(ServerInitializer.class);
	
	private String Pport;
	private String Pip;
	private String Ppasswd;
	private String Pusername;
	private  SslContext sslCtx;


    public ServerInitializer (SslContext sslCtx,String Pip,String Pport,String Pusername,String Ppasswd){
    	this.sslCtx = sslCtx;
    	this.Pip = Pip;
		this.Pport = Pport;
		this.Pusername = Pusername;
		this.Ppasswd = Ppasswd;
    }
    
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        // Enable stream compression (you can remove these two if unnecessary)
        //pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        //pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // Add the number codec first,
        pipeline.addLast(new MsgDecoder());
        pipeline.addLast(new MsgEncoder());
        
        //b logic
        pipeline.addLast(new ServerHandler(Pip,Pport,Pusername,Ppasswd));
    }

}
