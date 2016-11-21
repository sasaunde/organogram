package capgemini.orgchart;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

public interface ContextFactory {

	public LdapContext create(Hashtable<String, Object> env) throws NamingException;
	
}
