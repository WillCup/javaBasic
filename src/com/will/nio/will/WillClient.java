package com.will.nio.will;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.nio.socket.MyRequestObject;
import com.will.nio.socket.MyResponseObject;
import com.will.nio.socket.SerializableUtil;

public class WillClient {

	private final static Logger logger = Logger.getLogger(WillClient.class.getName());
	
	static ObjectMapper mapper = new ObjectMapper();
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 100; i++) {
			final int idx = i;
			new Thread(new MyRunnable(idx)).start();
		}
	}
	
	private static final class MyRunnable implements Runnable {
		
		private final int idx;

		private MyRunnable(int idx) {
			this.idx = idx;
		}

		public void run() {
			SocketChannel socketChannel = null;
			try {
				socketChannel = SocketChannel.open();
				SocketAddress socketAddress = new InetSocketAddress("localhost", 10000);
				socketChannel.connect(socketAddress);
				Map map = new HashMap();
				map.put("hello", "hi");
				sendData(socketChannel, map);
				
				ConfigInfo info = receiveData(socketChannel);
				logger.log(Level.INFO, "We have received ");
			} catch (Exception ex) {
				logger.log(Level.SEVERE, null, ex);
			} finally {
				try {
					socketChannel.close();
				} catch(Exception ex) {}
			}
		}

		private void sendData(SocketChannel socketChannel, Map map) throws IOException {
			byte[] bytes = mapper.writeValueAsBytes(map);
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			socketChannel.write(buffer);
			socketChannel.socket().shutdownOutput();
		}

		private ConfigInfo receiveData(SocketChannel socketChannel) throws IOException {
			ConfigInfo info = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			try {
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
				byte[] bytes;
				int count = 0;
				while ((count = socketChannel.read(buffer)) >= 0) {
					buffer.flip();
					bytes = new byte[count];
					buffer.get(bytes);
					baos.write(bytes);
					buffer.clear();
				}
				bytes = baos.toByteArray();
				info = mapper.readValue(bytes, ConfigInfo.class);
				socketChannel.socket().shutdownInput();
			} finally {
				try {
					baos.close();
				} catch(Exception ex) {}
			}
			return info;
		}
	}
}
