package capgemini.orgchart;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class DirectoryTest extends Mockito {

	NamingEnumeration<SearchResult> results;
	LdapContext mockContext;
	SearchResult mockResult;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws NamingException {

		mockContext = mock(LdapContext.class);
		
		results = mock(NamingEnumeration.class);
		mockResult = mock(SearchResult.class);
		

		when(results.hasMoreElements()).thenReturn(true);
		when(results.nextElement()).thenReturn(mockResult);
		
		Attributes mockAttributes= mock(Attributes.class);
		when(mockResult.getAttributes()).thenReturn(mockAttributes);
		
		Attribute nameAttr = mock(Attribute.class);
		when(nameAttr.toString()).thenReturn("displayName : Saunders, Sarah");
		Attribute phoneAttr = mock(Attribute.class);
		when(phoneAttr.toString()).thenReturn("phone : 00000");
		Attribute emailAttr = mock(Attribute.class);
		when(emailAttr.toString()).thenReturn("userPrincipalName : sarah.saunders@capgemini.com");
		when(mockAttributes.get("displayName")).thenReturn(nameAttr);
		when(mockAttributes.get("userPrincipalName")).thenReturn(emailAttr);
		when(mockAttributes.get("msRTCSIP-Line")).thenReturn(phoneAttr);
		
		
		when(mockContext.search(any(String.class), any(String.class), any(SearchControls.class))).thenReturn(results);
		
	}
	
	@After 
	public void tearDown() {
		LdapConnector.tearDown();
	}
	
	@Test
	public void testPersonReturnedFromLdap() throws Exception {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		ByteArrayOutputStream resultHolder = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(resultHolder);
		
		when(request.getParameter("fname")).thenReturn("Sarah");
		when(request.getParameter("lname")).thenReturn("Saunders");

		when(response.getWriter()).thenReturn(writer);
		
		ContextFactory mockContextFactory = mock(ContextFactory.class);
		when(mockContextFactory.create(any(Hashtable.class))).thenReturn(mockContext);
		
		// Initialise the mock context instance
		LdapConnector.getInstance(mockContextFactory);
		
		new Directory(mockContext).service(request, response);
		writer.flush();
		
		verify(request, times(1)).getParameter("fname");
		verify(request, times(1)).getParameter("lname");
		
		verify(results, times(2)).hasMoreElements();
		verify(results, times(1)).nextElement();
		verify(results, times(0)).next();
		verify(mockResult, times(3)).getAttributes();
		
		// Get JSON and read
		ObjectReader reader = new ObjectMapper().reader();
		
		JsonNode json = reader.readTree(resultHolder.toString());
		
		assertEquals("Saunders, Sarah", json.get("name").asText());
		assertEquals("00000", json.get("phone").asText());
		
		assertEquals("/image?id=sarah.saunders@capgemini.com", json.get("imageLocation").asText());
	}
}
