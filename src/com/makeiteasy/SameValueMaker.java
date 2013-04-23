package com.makeiteasy;

import java.util.Map;

public class SameValueMaker<T> extends Maker<T> {
	protected final Map<Property<? super T, Object>, Object> recordedValues = newHashMap();

	public SameValueMaker(Instantiator<T> instantiator, PropertyValue<? super T, ?>[] propertyValues) {
		super(instantiator, propertyValues);
	}

	protected SameValueMaker(SameValueMaker<T> maker, PropertyValue<? super T, ?>[] propertyValues) {
		super(maker, propertyValues);
		recordedValues.putAll(maker.recordedValues);
		removePropertyValuesFromRecordedValues(propertyValues);
	}

	private void removePropertyValuesFromRecordedValues(PropertyValue<? super T, ?>[] propertyValues) {
		for (PropertyValue<? super T, ?> propVal : propertyValues) {
			if (recordedValues.containsKey(propVal.property())) {
				recordedValues.remove(propVal.property());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V valueOf(Property<? super T, V> property, Donor<? extends V> defaultValue) {
		V value;
		if (recordedValues.containsKey(property)) {
			value = (V) recordedValues.get(property);

			if (values.containsKey(property)) {
				V newValue = super.valueOf(property, defaultValue);
				if (!newValue.equals(value)) {
					recordedValues.put((Property<? super T, Object>) property, value);
					return newValue;
				}
			}
		} else {
			value = super.valueOf(property, defaultValue);
			recordedValues.put((Property<? super T, Object>) property, value);
		}
		return value;
	}

	/**
	 * Returns a new Maker if <code>newInstance</code> is true for the same type of object and with the same values
	 * except where overridden by the given <var>propertyValues</var>.
	 * 
	 * @param propertyValues
	 *            those initial properties of the new Make that will differ from this Maker
	 * @return a new Maker or the orginal instance
	 */
	@Override
	public Maker<T> createWithAdditionalPropertyValues(boolean newInstance,
			@SuppressWarnings("unchecked") PropertyValue<? super T, ?>... propertyValues) {
		if (newInstance) {
			return new SameValueMaker<T>(this, propertyValues);
		} else {
			super.createWithAdditionalPropertyValues(newInstance, propertyValues);
			removePropertyValuesFromRecordedValues(propertyValues);
			return this;
		}
	}
}
