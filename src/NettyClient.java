import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyClient {
	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
//							p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
//					        p.addLast(new ObjectEncoder());
							p.addLast(new NettyClientHandler());
					       
						}
					});

			ChannelFuture f = b.connect("localhost", 8889).sync(); 

			Channel channel = f.channel();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				// CheckChannelStatus(channel);
				String msg = in.readLine();
				ByteBuf messageBuffer = Unpooled.buffer();
				messageBuffer.writeBytes(msg.getBytes());
				channel.writeAndFlush(messageBuffer.retain());
			}
			// f.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}

	}

	private static void Log(String string) {
		// TODO Auto-generated method stub
		System.out.println("Å¬¶ó| " + string);
	}

	private static void CheckChannelStatus(Channel channel) {
		if (channel.isActive()) {
			Log("is Active");
		}

		if (channel.isRegistered()) {
			Log("is Registered");
		}

		if (channel.isOpen()) {
			Log("is open");
		}

		if (channel.isWritable()) {
			Log("is writable");
		}
	}
}
