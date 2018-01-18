package pers.jues.network.JsonMina;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextEndCodecDecoder implements ProtocolDecoder {
	private Charset charset = Charset.forName("UTF-8");

	private static String CONTEXT = MyTextEndCodecDecoder.class.getName() + ".context";

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		IoBuffer buff = getContext(session);
		decodeAuto(buff, in, out);
	}

	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	private IoBuffer getContext(IoSession session) {
		IoBuffer buff = (IoBuffer) session.getAttribute(CONTEXT);
		if (buff == null) {
			buff = IoBuffer.allocate(100).setAutoExpand(true);
			session.setAttribute(CONTEXT, buff);
		}
		return buff;
	}

	private void decodeAuto(IoBuffer buff, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException {
		boolean mark = false;
		while (in.hasRemaining()) {
			byte b = in.get();
			switch (b) {
			case (byte) 0x00:
				mark = true;
				break; //
			default:
				buff.put(b);
			}

			if (mark) {
				buff.flip();
				try {
					out.write(buff.getString(charset.newDecoder()));
				} finally {
					buff.clear();
				}
				//
				mark = false;
			}
		}
	}
}
