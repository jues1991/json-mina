package pers.jues.network.JsonMina;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public class MyJsonFilter extends IoFilterAdapter {

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		// super.messageReceived(nextFilter, session, message);
		if (!(message instanceof String)) {
			nextFilter.messageReceived(session, message);
			return;
		}

		//
		String text = (String) message;
		JsonObject jobj = null;
		//
		try (JsonReader jsonReader = Json.createReader(new StringReader(text))) {
			jobj = jsonReader.readObject();
			//
			
		}finally {
			if ( null != jobj ) {
				nextFilter.messageReceived(session, jobj);
			}
		}

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
