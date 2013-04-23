package com.makeiteasy;

import java.util.HashMap;
import java.util.Map;

/**
 * Makes objects of a given type with a specified initial state.
 * 
 * @param <T>
 *            the type of object to make
 */
public class Maker<T> implements PropertyLookup<T>, Donor<T> {
	protected final Map<Property<? super T, ?>, PropertyValue<? super T, ?>> values = newHashMap();
	protected final Instantiator<T> instantiator;

	/**
	 * Creates a Maker for objects of a given type with a given initial state.
	 * 
	 * @param instantiator
	 *            creates the new objects
	 * @param propertyValues
	 *            define the initial state of the new objects
	 */
	@SafeVarargs
	public Maker(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyValues) {
		this.instantiator = instantiator;
		setPropertyValues(propertyValues);
	}

	protected static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	@SafeVarargs
	protected Maker(Maker<T> that, PropertyValue<? super T, ?>... propertyValues) {
		this.instantiator = that.instantiator;
		this.values.putAll(that.values);
		setPropertyValues(propertyValues);
	}

	private void setPropertyValues(PropertyValue<? super T, ?>[] propertyValues) {
		for (PropertyValue<? super T, ?> propertyValue : propertyValues) {
			values.put(propertyValue.property(), propertyValue);
		}
	}

	/**
	 * Makes a new object.
	 * 
	 * The {@link com.makeiteasy.MakeItEasy#make(Maker) MakeItEasy.make} method
	 * is syntactic sugar to make calls to this method read more naturally in most
	 * contexts.
	 * 
	 * @return a new object
	 */
	public T make() {
		return instantiator.instantiate(this);
	}

	@Override
	public T value() {
		return make();
	}

	/**
	 * Returns a new Maker for the same type of object and with the same initial state
	 * except where overridden by the given <var>propertyValues</var>.
	 * 
	 * @param propertyValues
	 *            those initial properties of the new Make that will differ from this Maker
	 * @return a new Maker
	 */
	@SafeVarargs
	public final Maker<T> but(PropertyValue<? super T, ?>... propertyValues) {
		return createWithAdditionalPropertyValues(true, propertyValues);
	}

	/**
	 * Returns the same Maker for the same type of object and with the same initial state
	 * except where overridden by the given <var>propertyValues</var>.
	 * 
	 * @param propertyValues
	 *            those initial properties of the new Make that will differ from this Maker
	 * @return the same instance of the maker
	 */
	@SafeVarargs
	public final Maker<T> sbut(PropertyValue<? super T, ?>... propertyValues) {
		return createWithAdditionalPropertyValues(false, propertyValues);
	}

	protected Maker<T> createWithAdditionalPropertyValues(boolean newInstance,
			@SuppressWarnings("unchecked") PropertyValue<? super T, ?>... propertyValues) {
		if (newInstance) {
			return new Maker<T>(this, propertyValues);
		} else {
			setPropertyValues(propertyValues);
			return this;
		}
	}

	@Override
	public <V> V valueOf(Property<? super T, V> property, V defaultValue) {
		return valueOf(property, new SameValueDonor<V>(defaultValue));
	}

	@Override
	public <V> V valueOf(Property<? super T, V> property, Donor<? extends V> defaultValue) {
		if (values.containsKey(property)) {
			return (V) values.get(property).value();
		} else {
			return defaultValue.value();
		}
	}
}
