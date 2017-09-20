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
	// 1. �Էµ� �����͸� ó���ϴ� �̺�Ʈ �ڵ鷯 ���

	private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// 2. ������ ���� �̺�Ʈ ó�� �޼���. Ŭ���̾�Ʈ�κ��� �������� ������ �̷�������� ��Ƽ�� �ڵ����� ȣ���ϴ� �̺�Ʈ �޼ҵ�
		Log("-------channel read--------");
		Channel incoming = ctx.channel();
//		Log("msg: " + (String)msg);
		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());


		//�ڱ� �ڽ� �ܿ� �ٸ� ����鿡�� ������
		for(Channel channel : channels){
			Log("channel:" + channel.remoteAddress());
			if(channel != incoming){
				Log("send message: " + readMessage);
				channel.writeAndFlush(msg);
			}
		}
		Log(ctx.channel().remoteAddress() + " [message: " + readMessage + "]");
		Log("-------channel read--------");

		//���� ���� �ڵ�
//		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
		// 3. ���ŵ� �����͸� ������ �ִ� ��Ƽ�� ����Ʈ ���� ��ü�� ���� ���ڿ� ��ü�� �о�´�.
//		ctx.write(msg);
		// 4.ctx�� ä�� ���������ο� ���� �̺�Ʈ�� ó���Ѵ�. ���⼭�� ������ ����� Ŭ���̾�Ʈ ä�η� �Է¹��� �����͸� �״��
		// �����Ѵ�.


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
		// 6. channelRead �̺�Ʈ�� ó���� �Ϸ�� �� �ڵ����� ����Ǵ� �̺�Ʈ �޼���
		Log("-------channel read complete--------");
		ctx.flush();
		Log("-------channel read complete--------");
		// 7. ä�� ������ ���ο� ����� ���۸� ����
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Log("exception Caught");
		cause.printStackTrace();
		ctx.close();
	}

	public void Log(String log){
		System.out.println("����| " + log);
	}
}
