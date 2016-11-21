package capgemini.orgchart;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * Default Ldap InitalDirContext creator
 * @author sasaunde
 *
 */
public class ContextFactoryImpl implements ContextFactory {

	public LdapContext create(Hashtable<String, Object> env) throws NamingException {
		return new InitialLdapContext(env, null);
	}

}
