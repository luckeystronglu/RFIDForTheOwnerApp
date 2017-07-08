package com.foxconn.rfid.theowner.socket;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


/**
 * Created by Jeff on 2016/8/26.
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {

//        for (int i = 0; i < 1000; i++) {
//            Thread socketThread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try {
//                        //创建客户端连接器.
//                        NioSocketConnector connector = new NioSocketConnector();
//                        connector.getFilterChain().addLast("logger", new LoggingFilter());
//                        connector.getFilterChain().addLast("codec",
//                                new ProtocolCodecFilter(new AsyncSocketCodecFactory())); //设置编码过滤器
//                        connector.setHandler(new ClientHandler());//设置事件处理器
//                        ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 8899));//建立连接
//                        cf.awaitUninterruptibly();//等待连接创建完成
//
//                        for (int i = 0; i < 10000; i++) {
//                            SocketAppPacket packet = new SocketAppPacket(cf.getSession());
//                            LoginRequest.LoginRequestMessage.Builder requestMessage = LoginRequest.LoginRequestMessage.newBuilder();
//                            requestMessage.setAccount("jeff");
//                            requestMessage.setPassword("test");
//
//                            packet.setCommandData(requestMessage.build().toByteArray());
//                            cf.getSession().write(packet);//发送消息，中英文都有
//                        }
//                    } catch (Exception e) {
//                        String msgString = "Can't start the client. "
//                                + e.getLocalizedMessage();
//                        ServerForm.showLog(msgString);
//                    }
//                }
//            });
//            socketThread.start();
        //创建客户端连接器.
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new SocketProtocolCodecFactory())); //设置编码过滤器
        connector.setHandler(new SocketClientHandlerListener());//设置事件处理器
        ConnectFuture cf = connector.connect(new InetSocketAddress("172.105.0.238", 8899));//建立连接
        cf.awaitUninterruptibly();//等待连接创建完成

//            SocketAppPacket packet = new SocketAppPacket(cf.getSession());
//            LoginRequest.LoginRequestMessage.Builder requestMessage = LoginRequest.LoginRequestMessage.newBuilder();
//            requestMessage.setPhone("thinkgem");
//            requestMessage.setPhoneType(LoginRequestMessage.PhoneType.AndroidPhone);               
//            requestMessage.setPassword("admin");
//            requestMessage.setPhoneType(LoginRequest.LoginRequestMessage.PhoneType.AndroidPhone);
//
//            packet.setCommandId(0x0001);
//
//            packet.setCommandData(requestMessage.build().toByteArray());
//            cf.getSession().write(packet);//发送消息，中英文都有


    }
}
