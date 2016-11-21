package capgemini.orgchart;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class ContextFactoryImpl implements ContextFactory {

	public LdapContext create(Hashtable<String, Object> env) throws NamingException {
		// TODO Auto-generated method stub
		return new InitialLdapContext(env, null);
	}

}
