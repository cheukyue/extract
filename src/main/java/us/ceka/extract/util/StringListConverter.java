package us.ceka.extract.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String>{

	public String convertToDatabaseColumn(List<String> list) {
		return String.join(",", list);
	}

	@Override
	public List<String> convertToEntityAttribute(String data) {
		return new ArrayList<>(Arrays.asList(data.split(",")));
	}



}
