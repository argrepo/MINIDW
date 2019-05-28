package minidwclientws;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class TestOauth1
{
public static void main(String[] args)
{
	String requestUrl = "https://stage.levelninesports.com/oauth/initiate";
	String  tokenURL = "https://stage.levelninesports.com/oauth/token";
	String authURL = "https://stage.levelninesports.com/oauth/devops/oauth_authorize?oauth_token=";
	String consumerKey = "86afe6ccea6605e202f8782cb7eb97c4";
	String	consumerSecret ="8cba045a70438727eb5715d97f5c5062";
	String	callbackURL ="http://localhost:2013/minidw/adt/package/webServiceConnection/webServiceOAuth1Authenticationcallback";
	String scope = "";
	MagentoThreeLeggedOAuth1 MagentoThreeLeggedOAuth1 = new MagentoThreeLeggedOAuth1(requestUrl, tokenURL, authURL);
	MagentoServiceOAuth1 magentoServiceOAuth1 = new MagentoServiceOAuth1(consumerKey, consumerSecret,callbackURL,scope,MagentoThreeLeggedOAuth1);
	
	OAuthService oAuthService = magentoServiceOAuth1.getOAuthService();
	Token requestToken = new Token("6987d0d6a6474d566402ba35a0237fb0", "1f39440f6917512ea3055754fa432a23");
	
	String autharizationVerifier = "cdc3c46b33ec050c7990f1a13ea197ed";
	
	Token accessToken = magentoServiceOAuth1.getAccessToken(oAuthService, requestToken, autharizationVerifier);
	
	System.out.println(accessToken.getToken());
	
	
}
}
