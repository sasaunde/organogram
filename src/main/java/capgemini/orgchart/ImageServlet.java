package capgemini.orgchart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Retrieves corporate directory images
 * 
 * @author sasaunde
 *
 */
public class ImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger("ImageServlet");

	/**
	 * Looks for request parameter ID containing email address of a person, and tries to retrieve
	 * their directory image based on the ID.
	 * 
	 * If not found, defaults to png from JAR file
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get image by querying LDAP with ID from request, return byte stream
		String email = request.getParameter("id");
		OutputStream o = response.getOutputStream();

		if (email != null && !"".equals(email.trim())) {
			Directory directory;
			try {
				directory = new Directory(LdapConnector.getInstance().getContext());

				byte[] image = directory.findImage(email);

				response.setContentType("image/jpeg");

				o.write(image);

			} catch (NamingException e) {
				
				log.debug(e);
				
				response.setContentType("image/png");
				InputStream in = getClass().getResourceAsStream("/person.png");

				IOUtils.copy(in, o);

			}
		} else {
			o.write(("Lookup value not supplied in request parameter id").getBytes());
		}
		o.flush();
		o.close();
	}
}
