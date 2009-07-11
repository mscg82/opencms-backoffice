import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SimpleMail
{
	private String mailhost = "smtp.gmail.com";

	private Session mailSession;
	private Message message;
	private Transport transport;

	private class MyAuthenticator extends Authenticator{

		private String username;
		private String password;

		public MyAuthenticator(){
			this("giuseppemiscione@gmail.com", "mscgpp");
		}

		public MyAuthenticator(String username, String password) {
			setUsername(username);
			setPassword(password);
		}

		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/* (non-Javadoc)
		 * @see javax.mail.Authenticator#getPasswordAuthentication()
		 */
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(getUsername(), getPassword());
		}

	}

	private void setMessage(String htmlText, String text, File attachment, Address sender, Address recipient,
			String subject, Address replyTo[]) throws MessagingException{

		// Create the root multipart
		Multipart rootMultipart = new MimeMultipart("mixed");

		// Create a body part to house the multipart/alternative Part
		MimeBodyPart contentPartRoot = new MimeBodyPart();

		// Create the content multipart
		Multipart bodyMultiPart = new MimeMultipart("alternative");

		contentPartRoot.setContent(bodyMultiPart);

		// Add the root body part to the root multipart
		rootMultipart.addBodyPart(contentPartRoot);

		MimeBodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setText(text);

		MimeBodyPart htmlBodyPart = new MimeBodyPart();
		htmlBodyPart.setContent(htmlText, "text/html; charset=UTF-8");
		htmlBodyPart.setHeader("Content-Transfer-Encoding", "8bit");

		bodyMultiPart.addBodyPart(textBodyPart);
		bodyMultiPart.addBodyPart(htmlBodyPart);


		if(attachment != null ) {
			BodyPart attachBodyPart = new MimeBodyPart();

			DataSource 	 source = new FileDataSource(attachment);

			attachBodyPart.setDataHandler(new DataHandler(source));
			attachBodyPart.setFileName(source.getName());

			rootMultipart.addBodyPart(attachBodyPart);
		}

		message.setHeader("Content-Type" , rootMultipart.getContentType() );
		message.setHeader("MIME-Version" , "1.0" );
		message.setHeader("X-Mailer", "Recommend-It Mailer V2.03c02");

		message.setContent(rootMultipart);

		setMessageParameters(sender, recipient, subject, replyTo);
	}

	private void setMessageParameters(Address sender, Address recipient, String subject, Address replyTo[]) throws MessagingException {

		message.setSentDate(new Date());

		message.setFrom(sender);
		message.setRecipient(Message.RecipientType.TO, recipient);
		message.setSubject(subject);
		message.setReplyTo(replyTo);
		message.saveChanges();
	}

	public synchronized void sendMail(String subject, String htmlBody, String simpleBody,
			String sender, String recipient, File attachment) throws Exception {

		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");
		props.setProperty("mail.mime.charset", "UTF-8");

		MyAuthenticator auth = new MyAuthenticator();
		mailSession = Session.getDefaultInstance(props, null);
		mailSession.setDebug(true);

		message = new MimeMessage(mailSession);
		transport = mailSession.getTransport("smtp");

		Address senderAddr = new InternetAddress(sender);
		Address recipientAddr = new InternetAddress(recipient);


		setMessage(htmlBody, simpleBody, attachment, senderAddr, recipientAddr, subject, new Address[]{senderAddr});

		//Transport.send(message);

		try{
			transport.connect(auth.getUsername(), auth.getPassword());
			transport.sendMessage(message, message.getAllRecipients());
		} finally{
			transport.close();
		}
	}


	public static void main(String args[])
	{
		try{
			SimpleMail mailutils = new SimpleMail();
			String htmlBody =
				"<html>\n" +
				"    <head><title>Test invio mail</title></head>\n" +
				"    <body>Questa &egrave; una <b>mail</b> di prova.</body>\n" +
				"</html>";
			mailutils.sendMail("test", htmlBody, "Questa è una mail di prova",
				"giuseppemiscione@gmail.com", "giuseppemiscione@libero.it", new File("Curriculum.doc"));
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
