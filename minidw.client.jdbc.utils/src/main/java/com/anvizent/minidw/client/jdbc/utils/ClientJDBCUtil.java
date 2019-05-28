package com.anvizent.minidw.client.jdbc.utils;

import static com.anvizent.minidw.client.jdbc.utils.CommonUtils.getConnectionUrl;
import static com.anvizent.minidw.client.jdbc.utils.CommonUtils.removePlaceHoldersIfAny;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * ClientJDBCUtil
 *
 */
public class ClientJDBCUtil
{
	@SuppressWarnings("restriction")
	public static Connection getClientDataBaseConnection(int connectorID, String serverIPAndPort, String userName, String password, String driverName, String protocal, boolean sslAuth, String sslParams) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Connection connection = null;
		String driver = driverName;
		String url = null;
		if( StringUtils.isNotBlank(serverIPAndPort) && StringUtils.startsWith(serverIPAndPort, "{") )
		{
			url = getConnectionUrl(serverIPAndPort, protocal);
		}
		else
		{
			url = removePlaceHoldersIfAny(protocal) + serverIPAndPort;
		}
		try
		{
			if( protocal.contains(Constants.DataBaseDriverType.MYSQL_DB_TYPE) )
			{
				if( sslAuth )
				{
					if( sslParams == null )
					{
						throw new IllegalArgumentException("SSL ca,key and client file paths not available.");
					}

					String[] sslFiles = sslParams.split(",");

					String trustStore = sslFiles[0] + "/"+Constants.SSL.TRUST_STORE;
					String keyStore = sslFiles[0] + "/"+Constants.SSL.KEY_STORE;
					String storePassword = Constants.SSL.PASSWORD; 

					String sslClientKeyFilePath = sslFiles[1];
					String sslClientCertFilePath = sslFiles[2];
					String sslServerCaFilePath = sslFiles[3];

					trustStore = (trustStore.indexOf('\\') != -1) ? trustStore.replaceAll("\\\\", "/") : trustStore;
					keyStore = (keyStore.indexOf('\\') != -1) ? keyStore.replaceAll("\\\\", "/") : keyStore;

					sslClientKeyFilePath = (sslClientKeyFilePath.indexOf('\\') != -1) ? sslClientKeyFilePath.replaceAll("\\\\", "/") : sslClientKeyFilePath;
					sslClientCertFilePath = (sslClientCertFilePath.indexOf('\\') != -1) ? sslClientCertFilePath.replaceAll("\\\\", "/") : sslClientCertFilePath;
					sslServerCaFilePath = (sslServerCaFilePath.indexOf('\\') != -1) ? sslServerCaFilePath.replaceAll("\\\\", "/") : sslServerCaFilePath;
					
					File sslClientKeyFile = new File(sslClientKeyFilePath);
					if(!sslClientKeyFile.exists()){
						throw new  Exception("SSL Client Key File does not exist.");
					}
					File sslClientCertFile  = new File(sslClientCertFilePath);
					if(!sslClientCertFile.exists()){
						throw new  Exception("SSL Client cert File does not exist.");
					}
					File sslServerCaFile  = new File(sslServerCaFilePath );
					if(!sslServerCaFile.exists()){
						throw new  Exception("SSL server ca File does not exist.");
					}
					
	                String certifaicate = "mysqlServerCACer" + RandomStringUtils.randomAlphanumeric(4) + CommonUtils.currentTime();
					
					String importCert = " -import -alias " + certifaicate + "  -file " + sslServerCaFilePath + " -keystore " + trustStore + " -storepass " + storePassword + " -noprompt";
					sun.security.tools.keytool.Main.main(importCert.trim().split("\\s+"));
					
					if( SystemUtils.IS_OS_WINDOWS )
					{
						String openSSLPath = System.getenv("OPEN_SSL_PATH");
						if( StringUtils.isBlank(openSSLPath) )
						{
							throw new IllegalArgumentException("OPEN_SSL_PATH Environment path not available in windows.");
						}
						String ssl = openSSLPath + "\\openssl pkcs12 -export -in " + sslClientCertFilePath + " -inkey " + sslClientKeyFilePath + " -passout pass:" + storePassword + " -out " + keyStore;
						Runtime.getRuntime().exec(ssl);
					}
					else
					{
						String ssl = "openssl pkcs12 -export -in " + sslClientCertFilePath + " -inkey " + sslClientKeyFilePath + " -passout pass:" + storePassword + " -out " + keyStore;
						Runtime.getRuntime().exec(ssl);
					}
					
					Thread.sleep(10000);
					
					File keyStoreFile = new File(keyStore);
					if(!keyStoreFile.exists()){
						throw new  Exception("Key Store File does not exist.Wrong ca or key or cert file uploaded.Please check proper file uploading.");
					}
					File trustStoreFile = new File(trustStore);
					if(!trustStoreFile.exists()){
						throw new  Exception("Trust Store File does not exist.Wrong ca or key or cert file uploaded.Please check proper file uploading.");
					}
					
					String importkeystore = " -importkeystore -srckeystore " + keyStore + " -srcstoretype pkcs12   -srcstorepass " + storePassword + " -destkeystore " + keyStore + " -deststoretype JKS -deststorepass " + storePassword + " -noprompt";
					sun.security.tools.keytool.Main.main(importkeystore.trim().split("\\s+"));
					
					String sslTrustKeyStoreFilePaths = "useSSL=true&requireSSL=true&clientCertificateKeyStoreUrl=file:" + keyStore + "&clientCertificateKeyStorePassword=" + storePassword + "&trustCertificateKeyStoreUrl=file:" + trustStore + "&trustCertificateKeyStorePassword=" + storePassword;

					if( !url.contains("?") )
					{
						url += "?";
					}
					else
					{
						url += "&";
					}
					if( StringUtils.isNotBlank(sslTrustKeyStoreFilePaths) ){
						url += sslTrustKeyStoreFilePaths;
					}
				}
			}
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e.getMessage());
		}

		if( url != null )
		{

			if( driver != null && url != null )
			{
				Class.forName(driver).newInstance();
				if( StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password) )
				{
					connection = DriverManager.getConnection(url, userName, password);
				}
				else if( StringUtils.isNotEmpty(userName) )
				{
					connection = DriverManager.getConnection(url, userName, "");
				}
				else
				{
					connection = DriverManager.getConnection(url);
				}

			}
		}
		return connection;
	}

}
