import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettySeverHandler extends ChannelInboundHandlerAdapter {
	// 1. 입력된 데이터를 처리하는 이벤트 핸들러 상속

	private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// 2. 데이터 순신 이벤트 처리 메서드. 클라이언트로부터 데이터의 수신이 이루어졌을때 네티가 자동으로 호출하는 이벤트 메소드
		Log("-------channel read--------");
		Channel incoming = ctx.channel();
//		Log("msg: " + (String)msg);
		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());


		//자기 자신 외에 다른 사람들에게 보내기
		for(Channel channel : channels){
			Log("channel:" + channel.remoteAddress());
			if(channel != incoming){
				Log("send message: " + readMessage);
				channel.writeAndFlush(msg);
			}
		}
		Log(ctx.channel().remoteAddress() + " [message: " + readMessage + "]");
		Log("-------channel read--------");

		//에코 서버 코드
//		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
		// 3. 수신된 데이터를 가지고 있는 네티의 바이트 버퍼 객체로 부터 문자열 객체를 읽어온다.
//		ctx.write(msg);
		// 4.ctx는 채널 파이프라인에 대한 이벤트를 처리한다. 여기서는 서버에 연결된 클라이언트 채널로 입력받은 데이터를 그대로
		// 전송한다.


	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Log("-------handler added--------");
		super.handlerAdded(ctx);
		Channel incoming = ctx.channel();
		for(Channel channel : channels){
			ctx.write("[SERVER] - " + incoming.remoteAddress() + " has joined!\n");
		}
		channels.add(ctx.channel());
		Log("-------handler added--------");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Log("-------handler removed--------");
		super.handlerRemoved(ctx);
		Channel incoming = ctx.channel();
		for(Channel channel : channels){
			ctx.write("[SERVER] - " + incoming.remoteAddress() + " has lefted!\n");
		}
		channels.remove(ctx.channel());
		Log("-------handler removed--------");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
//		Log("channel Read Complete");
		// 6. channelRead 이벤트의 처리가 완료된 후 자동으로 수행되는 이벤트 메서드
		Log("-------channel read complete--------");
		ctx.flush();
		Log("-------channel read complete--------");
		// 7. 채널 파이프 라인에 저장된 버퍼를 전송
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Log("exception Caught");
		cause.printStackTrace();
		ctx.close();
	}

	public void Log(String log){
		System.out.println("서버| " + log);
	}
}
