package capgemini.orgchart;

import java.io.IOException;
import java.io.Writer;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Access corporate directory via LDAP to retrieve person details and image
 * 
 * @author sasaunde
 *
 */
public class Directory extends HttpServlet {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger("Directory");

    
    private LdapContext ctx;
    
    
    public Directory(LdapContext ctx) {
   	 
    	this.ctx = ctx;
		
    }
	
	
    private Person findPerson(String firstName, String surname) throws NamingException {
    	 String searchFilter = "(&(objectclass=person)(sn=*" + surname + "*)(givenName=*"+firstName+"*))";//"(&(member-of=cn=dlukcsdintegration))";

         SearchControls searchControls = new SearchControls();
         searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

         NamingEnumeration<SearchResult> results = ctx.search(LdapConnector.getInstance().getLdapSearchBase(), searchFilter, searchControls);

         SearchResult searchResult = null;
         if(results.hasMoreElements()) {
              searchResult = (SearchResult) results.nextElement();

             //make sure there is not another item available, there should be only 1 match
             if(results.hasMoreElements()) {
                 log.warn("Matched multiple users for the name: " + firstName + " " + surname);
                 
             }
         } else {
         	log.info("No results");
         }
         
         return new Person(searchResult);
    }
    
    public byte[] findImage(String email) throws NamingException {

        String searchFilter = "(&(objectclass=person)(userprincipalname=*" + email + "*))";//"(&(member-of=cn=dlukcsdintegration))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = ctx.search(LdapConnector.getInstance().getLdapSearchBase(), searchFilter, searchControls);

        SearchResult searchResult = null;
        if(results.hasMoreElements()) {
             searchResult = (SearchResult) results.nextElement();

            //make sure there is not another item available, there should be only 1 match
            if(results.hasMoreElements()) {
                log.warn("Matched multiple users for the principal: " + email);
                
            }
        } else {
        	throw new NamingException("No results");
        }
		if(searchResult.getAttributes().get("thumbnailPhoto") != null) {
        return (byte[]) searchResult.getAttributes().get("thumbnailPhoto").get();
		} else 
		{
			throw new NamingException("No image");
		}
    }
    
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String firstName = request.getParameter("fname");
		String surName = request.getParameter("lname");
		Writer w = response.getWriter();

		try {
			Person person = this.findPerson(firstName, surName);
			ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
			w.write(writer.writeValueAsString(person));
		} catch (NamingException e) {
			log.error(e);
			w.write("{ error: '"+ e.getMessage()+"'}");
		}
		
		w.flush();
	}
}
