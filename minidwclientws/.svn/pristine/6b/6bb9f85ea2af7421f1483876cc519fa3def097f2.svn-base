package minidwclientws;
import org.apache.commons.lang.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class MagentoServiceOAuth1{
	
	String consumerKey;
	String consumerSecret;
	String callbackURL;
	String scope;
	MagentoThreeLeggedOAuth1 magentoThreeLeggedOAuth1;
	public MagentoServiceOAuth1(String consumerKey,String consumerSecret,String callbackURL,String scope ,MagentoThreeLeggedOAuth1 magentoThreeLeggedOAuth1){
		this.consumerKey=consumerKey;
		this.consumerSecret=consumerSecret;
		this.callbackURL=callbackURL;
		this.scope=scope;
		this.magentoThreeLeggedOAuth1 = magentoThreeLeggedOAuth1;
	}
	
	public OAuthService  getOAuthService()
	{
		OAuthService service;
		if(StringUtils.isNotBlank(scope)){
			service = new ServiceBuilder()
					.provider(magentoThreeLeggedOAuth1)
					.apiKey(consumerKey)
					.apiSecret(consumerSecret)
					.callback(callbackURL)
					.scope(scope)
					.debug()
					.build();
		}else{
			service = new ServiceBuilder()
					.provider(magentoThreeLeggedOAuth1)
					.apiKey(consumerKey)
					.apiSecret(consumerSecret)
					.callback(callbackURL)
					.debug()
					.build();
		}
		return service;
	}
	
	public String getAuthorizationUrl(OAuthService oAuthService , Token token) {
		String authorizationUrl = oAuthService.getAuthorizationUrl(token);
		return authorizationUrl;
	}
	
	public Token getRequestToken(OAuthService oAuthService) {
		return oAuthService.getRequestToken();
	}
	
	public Token getAccessToken(OAuthService oAuthService,Token requestToken , String autharizationVerifier)
	{
		Verifier verifier = new Verifier(autharizationVerifier);
		Token accessToken = oAuthService.getAccessToken(requestToken, verifier);
		return accessToken;
	}
}
