package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EmailTest {

	private static final String[] TEST_EMAILS = {"ab@bc.com", "ab@c.org", 
			"hjdfh@fjdfh.com"};
	
	private static final String TEST_EMAIL = "jiji@hj.com";

	
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		
		email = new EmailConcrete();
	}
	
	@After
	public void tearDownEmailTest() throws Exception{
		
	}
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/*
	 * 1
	 * Test addBcc(String...) by comparing the size 
	 */
	@Test
	public void testAddBcc() throws Exception{
		
		email.addBcc(TEST_EMAILS);
		assertEquals(3, email.getBccAddresses().size());
	}
	
	
	/*
	 * 2
	 * Test addCc(String): add a Cc email to by using addCc(String) method
	 * Compare the string value from getCcAddresses with the expected email value
	 */
	@Test
	public void testAddCc() throws Exception{
		
		email.addCc(TEST_EMAIL);
		String expected = "jiji@hj.com";
		assertEquals(expected, email.getCcAddresses().get(0).toString());
	}
	

	/*
	 * 3.1
	 * Test addHeader
	 * Compare the value of the header with the expected result
	 */
	@Test
	public void testAddHeader() throws Exception{
		email.addHeader(HEADER_TEST, "testheader");
		String expected = "testheader";
		assertEquals(expected, email.headers.get("title"));
	}


	
	/*
	 * 3.2
	 * Test addHeader
	 * Check for exception when the name of the header is null
	 */
	@Test
	public void testAddHeaderNullKey() throws Exception{
		
		thrown.expectMessage("name can not be null or empty");
		email.addHeader("", "testheader");
		
	}
	
	/*
	 * 3.3
	 * Test addHeader
	 * Check for exception when the value of the header is null
	 */
	@Test
	public void testAddHeaderNullValue() throws Exception{
		thrown.expectMessage("value can not be null or empty");
		email.addHeader(HEADER_TEST, "");
		
	}

	
	/*
	 * 4
	 * Test addReplyTo
	 * Compare the sizes of expected and actual result
	 */
	@Test
	public void testAddReplyTo() throws Exception{
		
		email.addReplyTo(TEST_EMAIL, "nahrin");
		assertEquals(1, email.getReplyToAddresses().size());
	}
	
	/*
	 * 5.1
	 * Test buildMimeMessage
	 * Compare the value of getSubject with an expected value
	 */
	@Test
	public void test1buildMimeMessage() throws Exception{
		email.setHostName("localhost");
		email.setFrom(TEST_EMAIL);
		email.addTo("ac@gmail.com");
		email.addBcc("bj@gmail.com");
		email.addCc("nah@gmail.com");
		email.addHeader("header", "title");
		email.setSubject("subject");

		email.buildMimeMessage();
		MimeMessage message = email.getMimeMessage();
		message.saveChanges();
		
		assertEquals("subject", message.getSubject());
	}

	/*
	 * 5.2
	 * Test buildMimeMessage
	 * Check for exception when no From address is provided.
	 */
	@Test
	public void test2buildMimeMessage() throws Exception{
		
		email.setHostName("localhost");
		email.addHeader("header", "title");
		email.setSubject("subject");
		email.addTo("ac@gmail.com");
		email.addCc("nah@gmail.com");


		thrown.expectMessage("From address required");
		email.buildMimeMessage();
	}

	/*
	 * 5.3
	 * Test buildMimeMessage
	 * Check for exception when no receiver address is provided
	 */
	@Test
	public void test3buildMimeMessage() throws Exception{
		
		email.setHostName("localhost");
		email.setFrom(TEST_EMAIL);
		email.addHeader("header", "title");
		email.setSubject("subject");

		thrown.expectMessage("At least one receiver address required");
		email.buildMimeMessage();
	}
	

	/*
	 * 5.4
	 * Test buildMimeMessage
	 * Check for exception when the message already exists. 
	 */
	@Test
	public void test4buildMimeMessage() throws Exception{
		email.setHostName("localhost");
		email.setFrom(TEST_EMAIL);
		email.addTo("ac@gmail.com");
		email.addBcc("bj@gmail.com");
		email.addCc("nah@gmail.com");
		email.content = "Hello";

		email.buildMimeMessage();
		
		
		thrown.expectMessage("The MimeMessage is already built.");
		email.buildMimeMessage();

	
	}

	/*
	 * 6.1
	 * Test getHostName 
	 * Compare the value of getHostName with the expected result when hostName is not null
	 */
	@Test
	public void test1getHostName() throws Exception{
		
		email.setHostName("localhost");
		assertEquals("localhost", email.getHostName());
	}
	
	/*
	 * 6.2
	 * Test getHostName 
	 * Check when the hostName is null
	 */
	@Test
	public void test2getHostName() throws Exception{
		
		String expected = null;
		assertEquals(expected, email.getHostName());
	}

	/*
	 * 7
	 * Test getMailSession
	 * Check for exception when valid hostName is not provided
	 */

	@Test
	public void testgetMailSession() throws Exception{
		
	    thrown.expectMessage("Cannot find valid hostname for mail session");
	    email.getMailSession();
	   
	}


	/*
	 * 8
	 * Test getSentDate
	 * Check if getSentDate returns the current sent date
	 */
	@Test
	public void testgetSentDate() throws Exception{
		
		Date date1 = new Date();
	
		email.setSentDate(date1);
		
		assertEquals(date1, email.getSentDate());

	}


	
	/*
	 * 9
	 * Test getSocketConnectionTimeout
	 * Check if the method returns the value of socketConnectionTimeout
	 */
	@Test
	public void testgetSocketConnectionTimeout() throws Exception{
		
		int actual = email.getSocketConnectionTimeout();
		int expected = 	email.socketConnectionTimeout;

		assertEquals(expected, actual);
	}



	/*
	 * 10
	 * Test setFrom()
	 * Compare the value of the From address (string) with expected value
	 */
	@Test
	public void testSetFrom() throws Exception{
		
		email.setFrom(TEST_EMAIL);
		assertEquals("jiji@hj.com",email.getFromAddress().toString());
	}
	
}
