/**
 * Object representing the portal with properties related to the overall portal structure.
 */
function ApgPortal(config) {
	
	// public properties
	this.contextPath = (config && config.contextPath) ? config.contextPath : "";
	this.localName = (config && config.localName) ? config.localName : "";
	this.sessionTimeout = (config && config.sessionTimeout) ? config.sessionTimeout : 0;	// seconds
	
	// private properties
	this.__navVisible = true;
	this.__analyticsTrackingId = config.analyticsTrackingId;
	
	var _this = this;
	
	// logout before session timeout
	if (this.sessionTimeout && this.sessionTimeout > 0) {
		// automatically logout 10 seconds before the session expiration time
		var timeout = (this.sessionTimeOut > 10) ? ((this.sessionTimeout * 1000) - 10000) : this.sessionTimeout * 1000;
		setTimeout(function() {_this.expireSession();}, timeout);
	}
	
	// Google analytics
	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	ga('create', _this.__analyticsTrackingId, 'auto');
	ga('send', 'pageview');
}

/**
 * Show or hide the navigation bar.
 */
ApgPortal.prototype.setNavVisible = function(isVisible) {
	if (isVisible) {
		$('#portal-navbar').show();
	} else {
		$('#portal-navbar').hide();
	}
	this.__navVisible = isVisible;
}

ApgPortal.prototype.expireSession = function() {
	window.location = this.contextPath + "/sessionExpired";
}

/**
 * 
 * @param event {
 * 		category, action, label (optional), value (optional)
 * }
 */
ApgPortal.prototype.trackEvent = function(event) {
	if (event && event.category && event.action) {
		ga('send', 'event', event.category, event.action, event.label, event.value);
	}
}