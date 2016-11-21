package capgemini.orgchart;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

/**
 * Provide LDAP connection information
 * @author sasaunde
 *
 */
public class LdapConnector {

	private static LdapConnector instance;

	private String ldapSearchBase; 

	private LdapContext context;
	
	
    /**
	 * Pull properties from ldap.properties file on the classpath to create Ldap context
	 * 
	 * @throws NamingException
	 */
	private LdapConnector(ContextFactory factory) throws NamingException {

		Hashtable<String, Object> env;

		Properties p = new Properties();
		String configFile = "ldap.properties";

		try {
			InputStream in = getClass().getResourceAsStream(configFile);
			if (in != null) {
				p.load(in);
			} else {
				log.warn("No file found");
			}
		} catch (Exception e) {
			log.error(e);
			throw new NamingException("Cannot get configuration properties as no ldap.config file found");
		}

		String ldapUsername = p.getProperty("ldap.user");
		String ldapPassword = p.getProperty("ldap.password");
		String ldapAdServer = p.getProperty("ldap.server");
		ldapSearchBase = p.getProperty("ldap.searchbase");
		
		log.debug("Username " + ldapUsername + ", pwd " + ldapPassword + ", server  " + ldapAdServer);
		
		env = new Hashtable<String, Object>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		if (ldapUsername != null) {
			env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
		}
		if (ldapPassword != null) {
			env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
		}
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapAdServer);

		// ensures that image attribute values
		// will be returned as a byte[] instead of a String
		env.put("java.naming.ldap.attributes.binary", "thumbnailPhoto");

		context = factory.create(env);
	}

	public static LdapConnector getInstance() throws NamingException {
		return getInstance(new ContextFactoryImpl());
	}
	
	/**
	 * Create and return singleton
	 * @return
	 * @throws NamingException
	 */
	public static synchronized LdapConnector getInstance(ContextFactory factory) throws NamingException {
		if (instance == null){// || !factory.getClass().getName().equals(contextFactoryClassName)) {

			instance = new LdapConnector(factory);

		} 
		return instance;
	}

	private static Logger log = Logger.getLogger("LdapConnector");

	/**
	 * Get context
	 * @return
	 */
	public LdapContext getContext() {
		return context;

	}

	/**
	 * Provide Capgemini search base
	 * @return
	 */
	public String getLdapSearchBase() {
		return ldapSearchBase;
	}

}
