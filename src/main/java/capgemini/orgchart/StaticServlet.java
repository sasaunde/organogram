package capgemini.orgchart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * Serve static HTML from the jar file
 * 
 * @author sasaunde
 *
 */
public class StaticServlet extends HttpServlet {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * If reqest parameter getDefault = true, returns static PNG from JAR file
	 * 
	 * Otherwise returns application HTML page
	 * 
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		OutputStream out = response.getOutputStream();

		InputStream in;
		if ("true".equals(request.getParameter("getDefault"))) {
			// Asking for default image
			response.setContentType("image/png");
			in = getClass().getResourceAsStream("/person.png");

		} else {
			// Return index page
			in = getClass().getResourceAsStream("/chart.html");
		}
		try {

			IOUtils.copy(in, out);
		} finally {
			out.close();
			in.close();
		}

	}
}
