package com.will.nio.v1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 这个是一个类似于聊天室的东西，启动server后，多启动几个client后，这几个client就可以相互通信了。
 * 
 * @author Will
 * @created at 2014-8-11 下午4:10:43
 */
public class NServer {

	// 用于检测所有的Channel状态的selector
	private Selector selector = null;
	static final int PORT = 30000;
	// 定义实现编码、解码的字符串集对象
	private Charset charse = Charset.forName("GBK");

	public void init() throws IOException {
		selector = Selector.open();
		// 通过open方法来打开一个未绑定的ServerSocketChannel是咧
		ServerSocketChannel server = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", PORT);
		// 将该ServerSocketChannel绑定到指定的IP地址
		server.bind(isa);
		// 设置serverSocket以非阻塞方式工作
		server.configureBlocking(false);
		// 将server注册到指定的selector对象
		server.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) {
			// 一次处理selector上的每个选择的SelectionKey
			for (SelectionKey sk : selector.selectedKeys()) {
				// 从selector上已选择的Kye集中删除正在处理的SelectionKey
				selector.selectedKeys().remove(sk);
				// 如果sk对应的Channel包含客户端的连接请求
				if (sk.isAcceptable()) {
					// 调用accept方法接收连接，产生服务器段的SocketChennal
					SocketChannel sc = server.accept();
					// 设置采用非阻塞模式
					sc.configureBlocking(false);
					// 将该SocketChannel注册到selector
					sc.register(selector, SelectionKey.OP_READ);
				}
				// 如果sk对应的Channel有数据需要读取
				if (sk.isReadable()) {
					// 获取该SelectionKey对银行的Channel，该Channel中有刻度的数据
					SocketChannel sc = (SocketChannel) sk.channel();
					// 定义备注执行读取数据源的ByteBuffer
					ByteBuffer buff = ByteBuffer.allocate(1024);
					String content = "";
					// 开始读取数据
					try {
						while (sc.read(buff) > 0) {
							buff.flip();
							content += charse.decode(buff);
						}
						System.out.println("读取的数据：" + content);
						// 将sk对应的Channel设置成准备下一次读取
						sk.interestOps(SelectionKey.OP_READ);
					}
					// 如果捕获到该sk对应的Channel出现了异常，表明
					// Channel对应的Client出现了问题，所以从Selector中取消
					catch (IOException io) {
						// 从Selector中删除指定的SelectionKey
						sk.cancel();
						if (sk.channel() != null) {
							sk.channel().close();
						}
					}
					// 如果content的长度大于0,则聊天信息不为空
					if (content.length() > 0) {
						// 遍历selector里注册的所有SelectionKey
						for (SelectionKey key : selector.keys()) {
							// 获取该key对应的Channel
							Channel targerChannel = key.channel();
							// 如果该Channel是SocketChannel对象
							if (targerChannel instanceof SocketChannel) {
								// 将读取到的内容写入该Channel中
								SocketChannel dest = (SocketChannel) targerChannel;
								dest.write(charse.encode(content));
							}
						}
					}
				}
			}
		}

	}
	
	public static void main(String [] args) throws IOException{
		new NServer().init();
	}

}
