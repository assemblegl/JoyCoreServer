package gl.netty;

import gl.global.Context;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.math.BigInteger;
import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
	
	 @Override
	    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
	        // Wait until the length prefix is available.
	        if (in.readableBytes() < 9) {
	            return;
	        }

	        in.markReaderIndex();

	        // Check the magic number.
	        int magicNumber = in.readUnsignedByte();
	        if (magicNumber != 'J') {
	            in.resetReaderIndex();
	            throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
	        }
	        
	        // check version
	        int msgVersion = in.readInt();
	        if(msgVersion != Context.msgVersion){
	        	System.out.println("error!break get version:"+msgVersion+",myVersion:"+Context.msgVersion);
	        	return;
	        }
	        System.out.println("get version:"+msgVersion+",myVersion:"+Context.msgVersion);
	        
	        // Wait until the whole data is available.
	        int dataLength = in.readInt();
	        if (in.readableBytes() < dataLength) {
	            in.resetReaderIndex();
	            return;
	        }

	        // Convert the received data into a new BigInteger.
	        byte[] decoded = new byte[dataLength];
	        in.readBytes(decoded);

	        out.add(new String(decoded));
	    }

}