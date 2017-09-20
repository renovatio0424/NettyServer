 import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
	public static void main(String[] args) throws Exception {

		//netty-all-4.1.51.Final.jar ���
		//test
	    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	    EventLoopGroup workerGroup = new NioEventLoopGroup();
	    System.out.println("test");
	    try{
	      ServerBootstrap b = new ServerBootstrap();
	      b.group(bossGroup, workerGroup)
	        .channel(NioServerSocketChannel.class)
	        .childHandler(new ChannelInitializer <SocketChannel>() {
	          @Override
	          protected void initChannel(SocketChannel ch) {
	            ChannelPipeline p = ch.pipeline();
	            p.addLast(new NettySeverHandler()); //1. ���ӵ� Ŭ���̾�Ʈ�κ��� ���ŵ� �����͸� ó���� �ڵ鷯 ����
	          }
	        })
	        .option(ChannelOption.SO_BACKLOG, 128)
	        .childOption(ChannelOption.SO_KEEPALIVE, true)
	        .childOption(ChannelOption.TCP_NODELAY, true);

	        ChannelFuture f = b.bind(8889).sync();
					System.out.println("���� ����!!");
	        f.channel().closeFuture().sync();

	    } finally {
	      workerGroup.shutdownGracefully();
	      bossGroup.shutdownGracefully();
	    }

	  }
}
