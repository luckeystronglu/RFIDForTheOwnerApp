
package com.foxconn.rfid.theowner.socket;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class SocketProtocolCodecFactory implements ProtocolCodecFactory {
	static final SocketDecoder dec = new SocketDecoder();
	static final SocketEncoder enc = new SocketEncoder();
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return dec;
	}

	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return enc;
	}

 

}
