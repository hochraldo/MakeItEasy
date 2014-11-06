package com.makeiteasy;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.makeiteasy.sequence.ElementsSequence;

/**
 * Syntactic sugar for using Make It Easy test-data builders.
 */
public class MakeItEasy {
	@SafeVarargs
	public static <T> Maker<T> a(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyProviders) {
		return new Maker<>(instantiator, propertyProviders);
	}

	@SafeVarargs
	public static <T> Maker<T> an(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyProviders) {
		return new Maker<>(instantiator, propertyProviders);
	}

	public static <T, V, W extends V> PropertyValue<T, V> with(Property<T, V> property, W value) {
		return new PropertyValue<>(property, new SameValueDonor<V>(value));
	}

	public static <T, V, W extends V> PropertyValue<T, V> with(W value, Property<T, V> property) {
		return new PropertyValue<>(property, new SameValueDonor<V>(value));
	}

	public static <T, V, W extends V> PropertyValue<T, V> with(Property<T, V> property, Donor<W> valueDonor) {
		return new PropertyValue<>(property, valueDonor);
	}

	public static <T, V, W extends V> PropertyValue<T, V> with(Donor<W> valueDonor, Property<T, V> property) {
		return new PropertyValue<>(property, valueDonor);
	}

	public static <T, V, W extends V> PropertyValue<T, V> withNull(Property<T, V> property) {
		return new PropertyValue<>(property, new NullValueDonor<W>());
	}

	@SafeVarargs
	public static <T> Maker<T> sameValueMaker(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyProviders) {
		return new SameValueMaker<>(instantiator, propertyProviders);
	}

	@SafeVarargs
	public static <T> Donor<T> theSame(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyProviders) {
		return theSame(an(instantiator, propertyProviders));
	}

	public static <T> Donor<T> theSame(Donor<T> originalDonor) {
		return new SameValueDonor<>(originalDonor.value());
	}

	public static <T> T make(Maker<T> maker) {
		return maker.value();
	}

	@SafeVarargs
	public static <T> T makeA(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyProviders) {
		return make(a(instantiator, propertyProviders));
	}

	@SafeVarargs
	public static <T> T makeAn(Instantiator<T> instantiator, PropertyValue<? super T, ?>... propertyProviders) {
		return make(an(instantiator, propertyProviders));
	}

	@SafeVarargs
	public static <T> Donor<List<T>> listOf(Donor<? extends T>... donors) {
		return new NewCollectionDonor<List<T>, T>(donors) {
			@Override
			protected List<T> newCollection() {
				return new ArrayList<>();
			}
		};
	}

	@SafeVarargs
	public static <T> Donor<Set<T>> setOf(Donor<? extends T>... donors) {
		return new NewCollectionDonor<Set<T>, T>(donors) {
			@Override
			protected Set<T> newCollection() {
				return new HashSet<>();
			}
		};
	}

	@SafeVarargs
	public static <T extends Comparable<T>> Donor<SortedSet<T>> sortedSetOf(Donor<? extends T>... donors) {
		return new NewCollectionDonor<SortedSet<T>, T>(donors) {
			@Override
			protected SortedSet<T> newCollection() {
				return new TreeSet<>();
			}
		};
	}

	public static <T> Donor<T> from(final Iterable<T> values) {
		return new ElementsSequence<>(values, Collections.<T> emptyList());
	}

	@SafeVarargs
	public static <T> Donor<T> from(T... values) {
		return from(asList(values));
	}

	public static <T> Donor<T> fromRepeating(Iterable<T> values) {
		return new ElementsSequence<>(values, values);
	}

	@SafeVarargs
	public static <T> Donor<T> fromRepeating(T... values) {
		return fromRepeating(asList(values));
	}
}
