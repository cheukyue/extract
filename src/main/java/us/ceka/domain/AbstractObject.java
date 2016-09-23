package us.ceka.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractObject<T> {
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
