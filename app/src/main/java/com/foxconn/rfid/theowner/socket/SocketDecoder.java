
package com.foxconn.rfid.theowner.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;




/**
 * 采用MINA的通信客户端
 * @author Tom Xu
 * @version Revision 1.0
 */
public class SocketDecoder extends CumulativeProtocolDecoder {

	@Override
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		in.setAutoExpand(false);

		if(in.remaining() < 4)
		{
			return false;
		}

		if (in.remaining() > 0) {//有数据时，读取前8字节判断消息长度al
			byte[] startFlagBytes = new byte[4];
			in.mark();//标记当前位置，以便reset
			in.get(startFlagBytes, 0, 4);//读取起始标识4字节
			//非法指令,丢弃
			if (startFlagBytes[0] != (byte)0xAA ||
					startFlagBytes[1] != (byte)0xAA ||
					startFlagBytes[2] != (byte)0xAA ||
					startFlagBytes[3] != (byte)0xAA) {
				in.clear();
				session.closeOnFlush();
//				LogUtil.e("SocketDecode","invalid package.");
				return false;
			}
			//读取数据包长度
			int packetLength = in.getInt();
			//需要读取数据包数据长度
			int needReadLength = packetLength - 4;
			if (needReadLength > in.remaining()) {//如果消息内容不够，则重置，相当于不读取size
				in.reset();
				return false;//父类接收新数据，以拼凑成完整数据
			} else {
				byte[] packetBytes = new byte[needReadLength];
				in.get(packetBytes, 0, needReadLength);
				//把字节转换为Java对象的工具类
				SocketAppPacket pack = convertToAppPacket(session, IoBuffer.wrap(packetBytes), packetLength);
				out.write(pack);
				if (in.remaining() > 0) {//如果读取内容后还粘了包，就让父类再重读一次，进行下一次解析
					return true;
				}
			}
		}

		return false;//处理成功，让父类进行接收下个包
	}

	private SocketAppPacket convertToAppPacket(IoSession session, IoBuffer packetIn, int packetSize) {

		int commandId = packetIn.getInt() ;
		
		SocketAppPacket packet = new SocketAppPacket(session);
		packet.setReceiveTime(System.currentTimeMillis());
		packet.setCommandId(commandId);

		byte[] commandData = new byte[packetSize - 8];
		packetIn.get(commandData);

		packet.setCommandData(commandData);

		return packet;
	}

	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {

	}
}
