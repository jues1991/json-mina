package pers.jues.network.JsonMina;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

public class MyTextEndFilter extends IoFilterAdapter {
	private static Logger logger = Logger.getLogger(MyTextEndFilter.class);

	private Charset charset = Charset.forName("UTF-8");
	private static String CONTEXT = MyTextEndFilter.class.getName() + ".context";

	public MyTextEndFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		if (!(message instanceof IoBuffer)) {
			logger.debug(message.getClass().getSimpleName());
			return;
		}

		IoBuffer in = (IoBuffer) message;
		IoBuffer buff = getContext(session);
		while (in.hasRemaining()) {
			byte b = in.get();
			//
			if (0x00 == b) {
				buff.flip();
				//
				String text = buff.getString(charset.newDecoder());
				nextFilter.messageReceived(session, text);
				//
				buff.clear();
				continue;
			}
			//
			buff.put(b);
		}
		//
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(nextFilter, session, writeRequest);
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		Object message = writeRequest.getMessage();
		if (!(message instanceof String)) {
			logger.debug(message.getClass().getSimpleName());
			return;
		}
		String text = (String) message;
		IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
		buff.putString(text, charset.newEncoder());
		buff.put((byte) 0x00);
		buff.flip();
		WriteRequest request = new DefaultWriteRequest(buff, writeRequest.getFuture(), writeRequest.getDestination());
		//
		nextFilter.filterWrite(session, request);
	}

	private IoBuffer getContext(IoSession session) {
		IoBuffer buff = (IoBuffer) session.getAttribute(CONTEXT);
		if (buff == null) {
			buff = IoBuffer.allocate(100).setAutoExpand(true);
			session.setAttribute(CONTEXT, buff);
		}
		return buff;
	}

}
