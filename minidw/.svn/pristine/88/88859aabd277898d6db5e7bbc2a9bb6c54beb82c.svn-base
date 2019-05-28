var apgRetail = apgRetail || {};
apgRetail.services = apgRetail.services || {};
apgRetail.model = apgRetail.model || {};

apgRetail.model.PosServiceConfig = function() {
	this.cnpCreditSaleUrl = null;
	this.cnpCreditReturnUrl = null;
	this.cnpCreditAuthUrl = null;
	this.creditAuthUrl = null;
	this.findUrl = null;
	this.findFacilityUrl = null;
	this.debitSaleUrl = null;
	this.debitReturnUrl = null;
	this.creditSaleUrl = null;
	this.creditReturnUrl = null;
	this.pingUrl = null;
	this.signUrl = null;
	this.signPath = null;
	this.timeout = 10000;
	this.timeoutForService = 90000;
	this.debug = false;
	this.pingBeforeRequest = true;
};


apgRetail.model.PosServiceResponse = function() {
	this.errors = [];
	this.success = true;
};


/*apgRetail.model.PosServiceResponse.prototype.hasErrors = function() {
	return this.errors && this.errors.length > 0;
};*/

apgRetail.model.PosServiceError = function() {
	this.code = null;
	this.message = null;
	this.text = null;
};


/**
 * param - config (PosServiceConfig)
 */
apgRetail.services.PosService = function(config) {
	this._config = null;
	
	if (config instanceof apgRetail.model.PosServiceConfig) {
		this._config = config;
	} else {
		this._config = new apgRetail.model.PosServiceConfig();
		this._initialize();
	}
};

apgRetail.services.PosService.prototype._initialize = function() {
	var urlStr = apgportal.contextPath + "/ajax/apg-retail/getRetailServiceClientConfiguration";
	var _this = this;
	$.ajax({
		datatype : "json",
		type : "GET",
		url : urlStr,
		success : function(clientConfiguration) {
			// Configuration is returned by controller
			_this._config.cnpCreditSaleUrl = clientConfiguration.apgRetailCnpCreditSaleUrl;
			_this._config.cnpCreditReturnUrl = clientConfiguration.apgRetailCnpCreditReturnUrl;
			_this._config.cnpCreditAuthUrl = clientConfiguration.apgRetailCnpCreditAuthUrl;
			_this._config.creditAuthUrl = clientConfiguration.apgRetailCreditAuthUrl;
			_this._config.findUrl = clientConfiguration.apgRetailFindUrl;
			_this._config.findFacilityUrl = clientConfiguration.apgRetailFindFacilityUrl;
			_this._config.debitSaleUrl = clientConfiguration.apgRetailDebitSaleUrl;
			_this._config.debitReturnUrl = clientConfiguration.apgRetailDebitReturnUrl;
			_this._config.creditSaleUrl = clientConfiguration.apgRetailCreditSaleUrl;
			_this._config.creditReturnUrl = clientConfiguration.apgRetailCreditReturnUrl;
			_this._config.pingUrl = clientConfiguration.apgRetailPingUrl;
			_this._config.signUrl = clientConfiguration.apgRetailSignUrl;
			_this._config.signPath = clientConfiguration.apgRetailSignPath;
			_this._config.pingBeforeRequest = clientConfiguration.apgRetailPingBeforeRequest;
		}
	});
};

/**
 * Perform a POST request to the APG Retail service.
 * 
 * @param url
 * @param requestData
 * @param successCallback
 * @param errorCallback
 */

apgRetail.services.PosService.prototype._postData = function(url, requestData, successCallback, errorCallback) {
	$.ajax({
		url : url,
		type : 'POST',
		dataType : 'jsonp',
		timeout: this._config.timeoutForService,
		success : function(serviceResponse) {
			if (successCallback) {
				//for credit return and debit setting the card type
				if(typeof serviceResponse.cardNbr  !== "undefined" && serviceResponse.cardNbr != null){
					var cardType = apgRetail.services.PosService.prototype.getCardDetails(serviceResponse.cardNbr);
					serviceResponse.cardType = cardType;
				} else {
					if(typeof serviceResponse.findTransactionList  !== "undefined" && serviceResponse.findTransactionList != null){
						serviceResponse.findTransactionList.forEach(function (findTransaction)
						{
							var cardType = apgRetail.services.PosService.prototype.getCardDetails(findTransaction.maskedCardNbr);
							findTransaction.cardType = cardType;
						});	
					}
				}
				
				successCallback(serviceResponse);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			if (errorCallback) {
				var serviceResponse = new apgRetail.model.PosServiceResponse();
				var err = new apgRetail.model.PosServiceError();
				err.message = 'Unable to connect to pin pad. Please check to make sure your device is connected.';
				err.code = '';
				serviceResponse.success = false;
				serviceResponse.errors.push(err);
				errorCallback(serviceResponse);
			}
		},
		data : requestData
	});
};

apgRetail.services.PosService.prototype.getCardDetails = function(cardNbr) {
	var firstFourDigits ;
	var cardNumberLength;
	
	if(cardNbr != null)
		firstFourDigits = cardNbr.substr(0,4);
	
	var cardTypeCode = {typeCode: "", type: ""};
	
	if (firstFourDigits >= "3000" && firstFourDigits <= "3059") {     
		cardTypeCode.typeCode = "DCB";
		cardTypeCode.type = "DCB";
	} else if (firstFourDigits >= "3600" && firstFourDigits <= "3699") {
		cardTypeCode.typeCode = "DCB";
		cardTypeCode.type = "DCB";
	} else if (firstFourDigits >= "3800" && firstFourDigits <= "3889") {
		cardTypeCode.typeCode = "DCB";
		cardTypeCode.type = "DCB";
	} else if (cardNbr == "371449635398431") {
		cardTypeCode.typeCode = "CSH";
		cardTypeCode.type = "CSH";
	} else if (firstFourDigits >= "3400" && firstFourDigits <= "3499") {
		cardTypeCode.typeCode = "AMX";
		cardTypeCode.type = "Amex";
	} else if (firstFourDigits >= "3700" && firstFourDigits <= "3799") {
		cardTypeCode.typeCode = "AMX";
		cardTypeCode.type = "Amex";
	} else if (firstFourDigits == "2014" || firstFourDigits == "2149") {
		cardTypeCode.typeCode = "CSH";
		cardTypeCode.type = "CSH";
	} else if (firstFourDigits >= "3088" && firstFourDigits <= "3094") {
		cardTypeCode.typeCode = "JCB";
		cardTypeCode.type = "JCB";
	} else if (firstFourDigits >= "3096" && firstFourDigits <= "3102") {
		cardTypeCode.typeCode = "JCB";
		cardTypeCode.type = "JCB";
	} else if (firstFourDigits >= "3112" && firstFourDigits <= "3159") {
		cardTypeCode.typeCode = "JCB";
		cardTypeCode.type = "JCB";
	} else if (firstFourDigits >= "3158" && firstFourDigits <= "3359") {
		cardTypeCode.typeCode = "JCB";
		cardTypeCode.type = "JCB";
	} else if (firstFourDigits >= "3337" && firstFourDigits <= "3349") {
		cardTypeCode.typeCode = "JCB";
		cardTypeCode.type = "JCB";
	} else if (firstFourDigits >= "3528" && firstFourDigits <= "3589") {		
		cardTypeCode.typeCode = "JCB";
		cardTypeCode.type = "JCB";
	} else if (firstFourDigits >= "3890" && firstFourDigits <= "3899") {
		
	} else if (firstFourDigits >= "4000" && firstFourDigits <= "4999") {
		cardTypeCode.typeCode = "VSA";
		cardTypeCode.type = "Visa";
	} else if (firstFourDigits >= "5100" && firstFourDigits <= "5599") {
		cardTypeCode.typeCode = "MCD";
		cardTypeCode.type = "MCD";
	} else if (firstFourDigits == "5610") {

	} else if (firstFourDigits == "6011") {
		cardTypeCode.typeCode = "DSC";
		cardTypeCode.type = "Disc";
	} else if (firstFourDigits == "9999") {
		cardTypeCode.typeCode = "CSH";
		cardTypeCode.type = "CSH";
	}else {
		cardTypeCode.typeCode = "N/A";
		cardTypeCode.type = "N/A";
	}
	
	return cardTypeCode;
};


/**
 * Perform an APG Retail service transaction.
 * 
 * @param url
 * @param requestData
 * @param successCallback
 * @param errorCallback
 */
apgRetail.services.PosService.prototype._processTransaction = function(url, requestData, successCallback, errorCallback) {
	var _this = this;
	if (this._config.pingBeforeRequest) {
		this.ping(function onPingSuccess(data) {
			_this._postData(url, requestData, successCallback, errorCallback)
		}, errorCallback);
	} else {
		_this._postData(url, requestData, successCallback, errorCallback)
	}
};

apgRetail.services.PosService.prototype.ping = function(successCallback, errorCallback) {
	$.ajax({
		url : this._config.pingUrl,
		type : 'GET',
		dataType : 'jsonp',
		timeout: this._config.timeout,
		success : function(serviceResponse) {
			if (successCallback) {
				successCallback(serviceResponse);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			if (errorCallback) {
				var serviceResponse = new apgRetail.model.PosServiceResponse();
				var err = new apgRetail.model.PosServiceError();
				err.message = 'Unable to connect to pin pad. Please check to make sure your device is connected.';
				err.code = '';
				serviceResponse.success = false;
				serviceResponse.errors.push(err);
				errorCallback(serviceResponse);
			}
		}
	});
};

	
apgRetail.services.PosService.prototype.cardNotPresentCreditSale = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.cnpCreditSaleUrl, cardRequest, successCallback, errorCallback);
};
	
apgRetail.services.PosService.prototype.cardNotPresentCreditReturn = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.cnpCreditReturnUrl, cardRequest, successCallback, errorCallback);
};

apgRetail.services.PosService.prototype.cnpCreditAuth = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.cnpCreditAuthUrl, cardRequest, successCallback, errorCallback);
};

apgRetail.services.PosService.prototype.creditAuth = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.creditAuthUrl, cardRequest, successCallback, errorCallback);
};

apgRetail.services.PosService.prototype.find = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.findUrl, cardRequest, successCallback, errorCallback);
};

apgRetail.services.PosService.prototype.findFacility = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.findFacilityUrl, cardRequest, successCallback, errorCallback);
};
	
apgRetail.services.PosService.prototype.debitSale = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.debitSaleUrl, cardRequest, successCallback, errorCallback);
};
	
apgRetail.services.PosService.prototype.debitReturn = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.debitReturnUrl, cardRequest, successCallback, errorCallback);
};

apgRetail.services.PosService.prototype.creditSale = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.creditSaleUrl, cardRequest, successCallback, errorCallback);
};
	
apgRetail.services.PosService.prototype.creditReturn = function(cardRequest, successCallback, errorCallback) {
	this._processTransaction(this._config.creditReturnUrl, cardRequest, successCallback, errorCallback);
};	
	
apgRetail.services.PosService.prototype.sign = function(signRequest, successCallback, errorCallback) {
	signRequest.filePath = this._config.signPath;
	this._processTransaction(this._config.signUrl, signRequest, successCallback, errorCallback);
};
