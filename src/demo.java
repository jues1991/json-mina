import org.apache.log4j.Logger;

public class demo {
	private static Logger logger = Logger.getLogger(demo.class);
	
	
	public boolean listen( int port ) {
		logger.info("listen");
		logger.debug("listen");
		logger.warn("listen");
		//
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		demo m = new demo();
		//
		m.listen(60000);
		
	}

}
