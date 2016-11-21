package capgemini.orgchart;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.catalina.LifecycleException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * App is just a main method which starts a Tomcat instance, what can we do here? It's really an integration test
 */

 class TomcatRunner implements Runnable {
	

	public void run() {
		// TODO Auto-generated method stub
		
			try {
				App.main(null);
				
			} catch (LifecycleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
}
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    private Thread t;

    @Before
    public void setUp() {
    	t = new Thread(new TomcatRunner());
    	t.start();
    }
    
    /**
     * Test image page
     * @throws InterruptedException 
     */
    public void testAppReturnsImage() throws InterruptedException
    {
    	int maxCount = 10;
    	while(!App.isRunning() && maxCount-- > 0) {
    		Thread.sleep(1 * 1000);
    	}
    	
    	try {
			HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:11111/person/image?id=sarah.saunders@capgemini.com").openConnection();
			

			con.setRequestMethod("GET");
			
			assertEquals(200, con.getResponseCode());
			assertEquals("image/jpeg", con.getContentType());
    	
    	} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
		}
    	
    }
    
    /**
     * Test index page
     * @throws InterruptedException 
     */
    public void testAppReturnsIndex() throws InterruptedException
    {
    	int maxCount = 10;
    	while(!App.isRunning() && maxCount-- > 0) {
    		Thread.sleep(1 * 1000);
    	}
    	try {
			HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:11111/person/index").openConnection();
			
			con.setRequestMethod("GET");
			
			assertEquals(200, con.getResponseCode());
    	
    	} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
		}
    	
    }
}
