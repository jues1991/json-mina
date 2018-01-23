package pers.jues.network.JsonMina;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

public class MyJsonFilter extends IoFilterAdapter {
	private static Logger logger = Logger.getLogger(MyJsonFilter.class);

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		if (!(message instanceof String)) {
			logger.debug(message.getClass().getSimpleName());
			return;
		}

		//
		String text = (String) message;
		JsonObject jobj = null;
		//
		logger.debug(text);
		//
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(text));
			jobj = jsonReader.readObject();
			//

		} catch (JsonParsingException e) {
			logger.warn(e.getMessage());
			return;
		}

		//
		nextFilter.messageReceived(session, jobj);

	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		Object message = writeRequest.getMessage();
		if (!(message instanceof JsonObject)) {
			logger.debug(message.getClass().getSimpleName());
			return;
		}
		JsonObject jobj = (JsonObject) message;
		String text = jobj.toString();
		
		logger.debug(text);
		
		WriteRequest request = new DefaultWriteRequest(text, writeRequest.getFuture(), writeRequest.getDestination());
		//
		nextFilter.filterWrite(session, request);

	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(nextFilter, session, writeRequest);
	}

	public MyJsonFilter() {
		// TODO Auto-generated constructor stub
	}

}
