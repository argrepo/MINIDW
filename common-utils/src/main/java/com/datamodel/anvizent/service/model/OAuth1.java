package com.datamodel.anvizent.service.model;

/*
 * OAuth1-based authentication
 * @author mahender.alaveni
 * 
 */
public class OAuth1
{

	private String consumerKey;
	private String consumerSecret;
	private String requestToken;
	private String requestTokenSecret;
	private String token;
	private String tokenSecret;
	private String signatureMethod;
	private String signature;
	private String timeStamp;
	private String nonce;
	private String version;
	private String verifier;
	private String scope;
	private String realm;
	private String requestURL;
	private String tokenURL;
	private String authURL;
	private String callbackURL;

	public String getConsumerKey()
	{
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey)
	{
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret()
	{
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret)
	{
		this.consumerSecret = consumerSecret;
	}

	public String getRequestToken()
	{
		return requestToken;
	}

	public void setRequestToken(String requestToken)
	{
		this.requestToken = requestToken;
	}

	public String getRequestTokenSecret()
	{
		return requestTokenSecret;
	}

	public void setRequestTokenSecret(String requestTokenSecret)
	{
		this.requestTokenSecret = requestTokenSecret;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getTokenSecret()
	{
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret)
	{
		this.tokenSecret = tokenSecret;
	}

	public String getSignatureMethod()
	{
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod)
	{
		this.signatureMethod = signatureMethod;
	}

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public String getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public String getNonce()
	{
		return nonce;
	}

	public void setNonce(String nonce)
	{
		this.nonce = nonce;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getVerifier()
	{
		return verifier;
	}

	public void setVerifier(String verifier)
	{
		this.verifier = verifier;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public String getRealm()
	{
		return realm;
	}

	public void setRealm(String realm)
	{
		this.realm = realm;
	}

	public String getRequestURL()
	{
		return requestURL;
	}

	public void setRequestURL(String requestURL)
	{
		this.requestURL = requestURL;
	}

	public String getTokenURL()
	{
		return tokenURL;
	}

	public void setTokenURL(String tokenURL)
	{
		this.tokenURL = tokenURL;
	}

	public String getAuthURL()
	{
		return authURL;
	}

	public void setAuthURL(String authURL)
	{
		this.authURL = authURL;
	}

	public String getCallbackURL()
	{
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL)
	{
		this.callbackURL = callbackURL;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authURL == null) ? 0 : authURL.hashCode());
		result = prime * result + ((callbackURL == null) ? 0 : callbackURL.hashCode());
		result = prime * result + ((consumerKey == null) ? 0 : consumerKey.hashCode());
		result = prime * result + ((consumerSecret == null) ? 0 : consumerSecret.hashCode());
		result = prime * result + ((nonce == null) ? 0 : nonce.hashCode());
		result = prime * result + ((realm == null) ? 0 : realm.hashCode());
		result = prime * result + ((requestToken == null) ? 0 : requestToken.hashCode());
		result = prime * result + ((requestTokenSecret == null) ? 0 : requestTokenSecret.hashCode());
		result = prime * result + ((requestURL == null) ? 0 : requestURL.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
		result = prime * result + ((signatureMethod == null) ? 0 : signatureMethod.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((tokenSecret == null) ? 0 : tokenSecret.hashCode());
		result = prime * result + ((tokenURL == null) ? 0 : tokenURL.hashCode());
		result = prime * result + ((verifier == null) ? 0 : verifier.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if( this == obj ) return true;
		if( obj == null ) return false;
		if( getClass() != obj.getClass() ) return false;
		OAuth1 other = (OAuth1) obj;
		if( authURL == null )
		{
			if( other.authURL != null ) return false;
		}
		else if( !authURL.equals(other.authURL) ) return false;
		if( callbackURL == null )
		{
			if( other.callbackURL != null ) return false;
		}
		else if( !callbackURL.equals(other.callbackURL) ) return false;
		if( consumerKey == null )
		{
			if( other.consumerKey != null ) return false;
		}
		else if( !consumerKey.equals(other.consumerKey) ) return false;
		if( consumerSecret == null )
		{
			if( other.consumerSecret != null ) return false;
		}
		else if( !consumerSecret.equals(other.consumerSecret) ) return false;
		if( nonce == null )
		{
			if( other.nonce != null ) return false;
		}
		else if( !nonce.equals(other.nonce) ) return false;
		if( realm == null )
		{
			if( other.realm != null ) return false;
		}
		else if( !realm.equals(other.realm) ) return false;
		if( requestToken == null )
		{
			if( other.requestToken != null ) return false;
		}
		else if( !requestToken.equals(other.requestToken) ) return false;
		if( requestTokenSecret == null )
		{
			if( other.requestTokenSecret != null ) return false;
		}
		else if( !requestTokenSecret.equals(other.requestTokenSecret) ) return false;
		if( requestURL == null )
		{
			if( other.requestURL != null ) return false;
		}
		else if( !requestURL.equals(other.requestURL) ) return false;
		if( scope == null )
		{
			if( other.scope != null ) return false;
		}
		else if( !scope.equals(other.scope) ) return false;
		if( signature == null )
		{
			if( other.signature != null ) return false;
		}
		else if( !signature.equals(other.signature) ) return false;
		if( signatureMethod == null )
		{
			if( other.signatureMethod != null ) return false;
		}
		else if( !signatureMethod.equals(other.signatureMethod) ) return false;
		if( timeStamp == null )
		{
			if( other.timeStamp != null ) return false;
		}
		else if( !timeStamp.equals(other.timeStamp) ) return false;
		if( token == null )
		{
			if( other.token != null ) return false;
		}
		else if( !token.equals(other.token) ) return false;
		if( tokenSecret == null )
		{
			if( other.tokenSecret != null ) return false;
		}
		else if( !tokenSecret.equals(other.tokenSecret) ) return false;
		if( tokenURL == null )
		{
			if( other.tokenURL != null ) return false;
		}
		else if( !tokenURL.equals(other.tokenURL) ) return false;
		if( verifier == null )
		{
			if( other.verifier != null ) return false;
		}
		else if( !verifier.equals(other.verifier) ) return false;
		if( version == null )
		{
			if( other.version != null ) return false;
		}
		else if( !version.equals(other.version) ) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "OAuth1 [consumerKey=" + consumerKey + ", consumerSecret=" + consumerSecret + ", requestToken=" + requestToken + ", requestTokenSecret=" + requestTokenSecret + ", token=" + token + ", tokenSecret=" + tokenSecret + ", signatureMethod=" + signatureMethod + ", signature=" + signature
				+ ", timeStamp=" + timeStamp + ", nonce=" + nonce + ", version=" + version + ", verifier=" + verifier + ", scope=" + scope + ", realm=" + realm + ", requestURL=" + requestURL + ", tokenURL=" + tokenURL + ", authURL=" + authURL + ", callbackURL=" + callbackURL + "]";
	}

}
