package pers.jues.network.JsonMina;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class JsonMina {
	private static Logger logger = Logger.getLogger(JsonMina.class);
	NioSocketAcceptor acceptor = null;

	public boolean start(int port) {
		try {
			acceptor = new NioSocketAcceptor();
			//
			acceptor.getFilterChain().addLast("logger",new LoggingFilter());
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextEndCodecFactory()));
			//acceptor.getSessionConfig().setReadBufferSize(2048);
			//acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
			acceptor.setHandler(new MyHandler());
			acceptor.setReuseAddress(true);
			acceptor.bind(new InetSocketAddress(port));
			logger.info("start success port: " + port);
		} catch (Exception e) {
			logger.error("start faill: ", e);
			e.printStackTrace();
			//
			return false;
		}

		//
		return true;
	}

}