package pers.jues.network.JsonMina;

import java.io.StringReader;
import java.net.SocketAddress;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

public class MyJsonFilter extends IoFilterAdapter {

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		if (!(message instanceof String)) {
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
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		Object message = writeRequest.getMessage(); 
		if (!(message instanceof JsonObject)) {
			return;
		}
		JsonObject jobj = (JsonObject)message;
		String text = jobj.toString();
		WriteRequest request = new DefaultWriteRequest(text,writeRequest.getFuture(),writeRequest.getDestination());
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
