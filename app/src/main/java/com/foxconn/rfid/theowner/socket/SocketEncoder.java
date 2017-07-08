package com.foxconn.rfid.theowner.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;



 
public class SocketEncoder implements ProtocolEncoder {

	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void encode(IoSession session, Object obj, ProtocolEncoderOutput output)
			throws Exception {

		SocketAppPacket responsePacket = (SocketAppPacket) obj;

		byte[] startFlagBytes = new byte[4];
		startFlagBytes[0] = (byte)0xAA;
		startFlagBytes[1] = (byte)0xAA;
		startFlagBytes[2] = (byte)0xAA;
		startFlagBytes[3] = (byte)0xAA;

		int packetLength = responsePacket.getCommandData().length + 8;

		IoBuffer buffer = IoBuffer.allocate(0).setAutoExpand(true);
		buffer.put(startFlagBytes);
		buffer.putInt(packetLength);
		buffer.putInt(responsePacket.getCommandId());
		buffer.put(responsePacket.getCommandData());
		buffer.flip();
		output.write(buffer);
		output.flush();
	}
}
