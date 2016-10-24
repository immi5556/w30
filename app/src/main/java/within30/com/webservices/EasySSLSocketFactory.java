package within30.com.webservices;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This socket factory will create ssl socket that accepts self signed
 * certificate
 */
public class EasySSLSocketFactory implements LayeredSocketFactory 
{
	private SSLContext sslcontext = null;

	private static SSLContext createEasySSLContext() throws IOException 
	{
		try 
		{
			SSLContext context = SSLContext.getInstance("TLS");

			TrustManager[] trustAllCerts = { new X509TrustManagerTrustAllCertificates(null)};
			context.init(null, trustAllCerts, null);
			return context;
		} 
		catch (Exception e) 
		{
			throw new IOException(e.getMessage());
		}
	}

	private SSLContext getSSLContext() throws IOException 
	{
		if (this.sslcontext == null) 
		{
			this.sslcontext = createEasySSLContext();
		}
		return this.sslcontext;
	}

	/**
	 * @see org.apache.http.conn.scheme.SocketFactory#connectSocket(Socket,
	 *      String, int, InetAddress, int,
	 *      HttpParams)
	 */
	public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params)
								throws IOException, UnknownHostException, ConnectTimeoutException
	{
		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
		SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

		if ((localAddress != null) || (localPort > 0)) 
		{
			// we need to bind explicitly
			if (localPort < 0) 
			{
				localPort = 0; // indicates "any"
			}
			InetSocketAddress isa = new InetSocketAddress(localAddress,	localPort);
			sslsock.bind(isa);
		}

		sslsock.connect(remoteAddress, connTimeout);
		sslsock.setSoTimeout(soTimeout);
		return sslsock;
	}

	/**
	 * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
	 */
	public Socket createSocket() throws IOException 
	{
		return getSSLContext().getSocketFactory().createSocket();
	}

	/**
	 * @see org.apache.http.conn.scheme.SocketFactory#isSecure(Socket)
	 */
	public boolean isSecure(Socket socket) throws IllegalArgumentException 
	{
		return true;
	}

	/**
	 * @see LayeredSocketFactory#createSocket(Socket,
	 *      String, int, boolean)
	 */
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	// -------------------------------------------------------------------
	// Javadoc in org.apache.http.conn.scheme.SocketFactory says :
	// Both Object.equals() and Object.hashCode() must be overridden
	// for the correct operation of some connection managers
	// -------------------------------------------------------------------

	public boolean equals(Object obj) 
	{
		return ((obj != null) && obj.getClass().equals(EasySSLSocketFactory.class));
	}

	public int hashCode() 
	{
		return EasySSLSocketFactory.class.hashCode();
	}

	/**
	 * Trust all the certificates
	 * 
	 */
	static class X509TrustManagerTrustAllCertificates implements X509TrustManager 
	{
//		public X509Certificate[] getAcceptedIssuers()
//		{
//			return new X509Certificate[0];
//		}
//
//		public void checkClientTrusted(X509Certificate[] chain,	String authType) throws CertificateException 
//		{
//		}
//
//		public void checkServerTrusted(X509Certificate[] chain,	String authType) throws CertificateException
//		{
//		}
		
		// New Code
		private X509TrustManager standardTrustManager = null;  

	    /** 
	     * Constructor for EasyX509TrustManager. 
	     */  
	    public X509TrustManagerTrustAllCertificates(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException 
	    {  
	      super();  
	      TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
	      factory.init(keystore);  
	      TrustManager[] trustmanagers = factory.getTrustManagers();  
	      if (trustmanagers.length == 0) 
	      {  
	        throw new NoSuchAlgorithmException("no trust manager found");  
	      }  
	      this.standardTrustManager = (X509TrustManager) trustmanagers[0];  
	    }  

	    /** 
	     * @see X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
	     */  
	    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException 
	    {  
	      standardTrustManager.checkClientTrusted(certificates, authType);  
	    }  

	    /** 
	     * @see X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
	     */  
	    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException 
	    {  
	    // Clean up the certificates chain and build a new one.
	        // Theoretically, we shouldn't have to do this, but various web servers
	        // in practice are mis-configured to have out-of-order certificates or
	        // expired self-issued root certificate.
	        int chainLength = certificates.length;
	        if (certificates.length > 1) 
	        {
	          // 1. we clean the received certificates chain.
	          // We start from the end-entity certificate, tracing down by matching
	          // the "issuer" field and "subject" field until we can't continue.
	          // This helps when the certificates are out of order or
	          // some certificates are not related to the site.
	          int currIndex;
	          for (currIndex = 0; currIndex < certificates.length; ++currIndex) 
	          {
	            boolean foundNext = false;
	            for (int nextIndex = currIndex + 1;
	                           nextIndex < certificates.length;
	                           ++nextIndex) 
	            {
	              if (certificates[currIndex].getIssuerDN().equals(
	                            certificates[nextIndex].getSubjectDN())) 
	              {
	                foundNext = true;
	                // Exchange certificates so that 0 through currIndex + 1 are in proper order
	                if (nextIndex != currIndex + 1) 
	                {
	                  X509Certificate tempCertificate = certificates[nextIndex];
	                  certificates[nextIndex] = certificates[currIndex + 1];
	                  certificates[currIndex + 1] = tempCertificate;
	                }
	                break;
	            }
	            }
	            if (!foundNext) break;
	      }

	          // 2. we exam if the last traced certificate is self issued and it is expired.
	          // If so, we drop it and pass the rest to checkServerTrusted(), hoping we might
	          // have a similar but unexpired trusted root.
	          chainLength = currIndex + 1;
	          X509Certificate lastCertificate = certificates[chainLength - 1];
	          Date now = new Date();
	          if (lastCertificate.getSubjectDN().equals(lastCertificate.getIssuerDN())
	                  && now.after(lastCertificate.getNotAfter())) 
	          {
	            --chainLength;
	          }
	      } 

	    standardTrustManager.checkServerTrusted(certificates, authType);    
	    }  

	    /** 
	     * @see X509TrustManager#getAcceptedIssuers()
	     */  
	    public X509Certificate[] getAcceptedIssuers() 
	    {  
	      return this.standardTrustManager.getAcceptedIssuers();  
	    }   
	}
}