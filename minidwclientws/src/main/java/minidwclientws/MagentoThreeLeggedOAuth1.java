package minidwclientws;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class MagentoThreeLeggedOAuth1 extends DefaultApi10a {
	
	private String requestUrl;
	private String tokenURL;
	private String authorizationUrl;
	
	public MagentoThreeLeggedOAuth1(String requestUrl,String tokenURL,String authorizationUrl) {
		this.requestUrl=requestUrl;
		this.tokenURL=tokenURL;
		this.authorizationUrl=authorizationUrl;
	}
	
	@Override
	public String getRequestTokenEndpoint() {
		return  requestUrl;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return tokenURL;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return authorizationUrl + requestToken.getToken();
	}

}