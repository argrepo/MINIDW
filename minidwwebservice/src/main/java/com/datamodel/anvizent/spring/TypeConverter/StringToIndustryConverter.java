package com.datamodel.anvizent.spring.TypeConverter;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import com.datamodel.anvizent.service.model.Industry;

public class StringToIndustryConverter implements Converter<String, Industry> {

	@Override
	public Industry convert(String industryId) {
		int id = 0;
		Industry industry = new Industry();
		try {
			id = Integer.parseInt(industryId);
			industry.setId(id);
		} catch (NumberFormatException e) {
			throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Industry.class), industryId, null);
		}

		return industry;
	}

}
