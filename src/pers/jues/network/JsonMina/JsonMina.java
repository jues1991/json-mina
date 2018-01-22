package pers.jues.network.JsonMina;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class JsonMina {
	private static Logger logger = Logger.getLogger(JsonMina.class);
	NioSocketAcceptor acceptor = null;

	public boolean start(int port) {
		try {
			acceptor = new NioSocketAcceptor();

			// log filter
			acceptor.getFilterChain().addLast("log", new LoggingFilter());

			// spalit package
			acceptor.getFilterChain().addLast("text", new MyTextEndFilter());

			// json parse
			acceptor.getFilterChain().addLast("json", new MyJsonFilter());

			// read buff
			acceptor.getSessionConfig().setReadBufferSize(2048);

			// free(no data) time
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 2 * 60 * 1000);

			//
			acceptor.setHandler(new MyHandler());

			// address reuse
			acceptor.setReuseAddress(true);

			// bind address
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
