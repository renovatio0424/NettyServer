import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	public class MsgSender extends Thread {
		String msg = "메시지";
		ChannelHandlerContext ctx;
		
		public MsgSender(ChannelHandlerContext ctx){
			this.ctx = ctx;
		}
		public void run(){
			
			ByteBuf messageBuffer = Unpooled.buffer();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			while(true){
				try {
					msg = in.readLine();
					System.out.println(msg);
					messageBuffer.writeBytes(msg.getBytes());
					this.ctx.writeAndFlush(messageBuffer);
					this.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws IOException {
		Log("channel Active");

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		Log("channel Read");

		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

		StringBuilder builder = new StringBuilder();
		builder.append("수신한 메시지 [");
		builder.append(readMessage);
		builder.append("]");

		Log(builder.toString());

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		Log("channel Read Complete");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Log("exception Caught");
		cause.printStackTrace();
		ctx.close();
	}
	
	public void Log(String log){
		System.out.println("서버| " + log);
	}
	
	
}
