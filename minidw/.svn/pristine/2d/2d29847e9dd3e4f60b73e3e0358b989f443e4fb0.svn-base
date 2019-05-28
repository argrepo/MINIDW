package com.datamodel.anvizent.validator;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.StandardPackageForm;

public class StandardPackageFormValidator implements Validator {
	@Autowired
	MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return StandardPackageForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		StandardPackageForm sp = (StandardPackageForm) obj;
		String packageName = sp.getPackageName().trim();
		int trailingmonths =sp.getTrailingMonths();
		Locale locale = sp.getLocale();
		int i = 0;
		switch (i) {
			case 0:
				// Statements
				if (StringUtils.isBlank(packageName)) {
					String message = messageSource.getMessage("anvizent.package.message.packageNameShouldNotBeEmpty", null, locale);
					errors.rejectValue("packageName", message, new Object[] { "'packageName'" }, message);
					break;
				} else {
					i++;
				}
				// optional

			case 1:
				// Statements
				if (packageName.length() < Constants.Config.PACKAGE_NAME_MIN || packageName.length() > Constants.Config.PACKAGE_NAME_MAX) {
					String message = messageSource.getMessage("anvizent.package.message.packageNameShouldContainAtleast3Characters", null, locale)
							.replace("$", String.valueOf(Constants.Config.PACKAGE_NAME_MIN)).replace("?", String.valueOf(Constants.Config.PACKAGE_NAME_MAX));
					errors.rejectValue("packageName", message, new Object[] { "'packageName'" }, message);
					break;
				} else {
					i++;
				}
			case 2:
				if (!packageName.matches(Constants.Regex.ALPHA_NUMERICS_WITH_SP_CHAR)) {
					String message = messageSource.getMessage("anvizent.package.message.specialCharactersandOnlyAllowed", null, locale);
					errors.rejectValue("packageName", message, new Object[] { "'packageName'" }, message);
					break;
				} else {
					i++;
				}
			case 3:
				if (!packageName.matches(Constants.Regex.REJECT_ONLY_SP_CHAR)) {
					String message = messageSource.getMessage("anvizent.package.message.addAtLleastAnAlphanumericCharacter", null, locale);
					errors.rejectValue("packageName", message, new Object[] { "'packageName'" }, message);
					break;
				}
		}
		
		if(trailingmonths == 0){
			String message = messageSource.getMessage("anvizent.package.message.trailingmonthsShouldNotBeEmpty", null, locale);
			errors.rejectValue("trailingMonths", message, new Object[] { "'packageName'" }, message);
		}
	}

}
