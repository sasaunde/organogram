package capgemini.orgchart;

import java.io.File;

import javax.naming.NamingException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;
import org.junit.Before;

/**
 * Start the embedded Tomcat instance on port 11111 and listen for input
 * 
 * Takes port from system property directory.port, if not available or property not an int defaults to 11111
 *
 */

public class App {
	 
	private static Logger log = Logger.getLogger("App");

	private static Boolean running = Boolean.FALSE;
	
	public static boolean isRunning() {
		return running.booleanValue();
	}
	
	public static void main(String[] args) throws LifecycleException {
		Tomcat tomcat = new Tomcat();
		
		int PORT = 11111;
		if(System.getProperty("directory.port") != null) {
			try {
				PORT = Integer.parseInt(System.getProperty("directory.port"));
			} catch (NumberFormatException ignored) {}
		}
		tomcat.setPort(PORT);

		Context ctx = tomcat.addContext("/person", new File(".").getAbsolutePath());

		Tomcat.addServlet(ctx, "static", new StaticServlet());

		ctx.addServletMapping("/index", "static");

		try {
			Tomcat.addServlet(ctx, "directory", new Directory(LdapConnector.getInstance().getContext()));

			Tomcat.addServlet(ctx, "image", new ImageServlet());

			ctx.addServletMapping("/dir", "directory");
			ctx.addServletMapping("/image", "image");


		} catch (NamingException e) {
			log.error(e);
		}

		tomcat.start();
		
		synchronized(running) {
			running = Boolean.TRUE;
		}
		
		tomcat.getServer().await();
	}
}
