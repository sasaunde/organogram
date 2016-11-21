package capgemini.orgchart;

import static org.junit.Assert.*;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class LdapConnectorTest extends Mockito {

	@SuppressWarnings("unchecked")
	@Test
	public void testLdapPullsProperties() {
		try {
			
			LdapContext mockContext = mock(InitialLdapContext.class);
			
			ContextFactory mockContextFactory = mock(ContextFactory.class);
			when(mockContextFactory.create(any(Hashtable.class))).thenReturn(mockContext);
			
			LdapConnector conn = LdapConnector.getInstance(mockContextFactory);
			
			ArgumentCaptor<Hashtable> env = ArgumentCaptor.forClass(Hashtable.class);
			
			verify(mockContextFactory).create(env.capture());
			
			assertEquals("ldapusername", env.getValue().get(Context.SECURITY_PRINCIPAL));
			assertEquals("ldappwd", env.getValue().get(Context.SECURITY_CREDENTIALS));
			assertEquals("ldap://url:999", env.getValue().get(Context.PROVIDER_URL));
			
			assertEquals("OU=UK,OU=Employees,DC=Comics", conn.getLdapSearchBase());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@After 
	public void tearDown() {
		LdapConnector.tearDown();
	}
	
}
