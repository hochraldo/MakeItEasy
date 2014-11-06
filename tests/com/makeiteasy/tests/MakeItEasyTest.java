package com.makeiteasy.tests;

import static com.makeiteasy.MakeItEasy.a;
import static com.makeiteasy.MakeItEasy.make;
import static com.makeiteasy.MakeItEasy.makeA;
import static com.makeiteasy.MakeItEasy.makeAn;
import static com.makeiteasy.MakeItEasy.with;
import static com.makeiteasy.MakeItEasy.withNull;
import static com.makeiteasy.Property.newProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

import com.makeiteasy.Instantiator;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

public class MakeItEasyTest {
	public static class ThingToMake {
		public final String name;
		public final Integer age;

		public ThingToMake(String name, Integer age) {
			this.name = name;
			this.age = age;
		}
	}

	public static final Property<ThingToMake, String> name = newProperty();
	public static final Property<ThingToMake, Integer> age = newProperty();

	public static Instantiator<ThingToMake> ThingToMake = new Instantiator<ThingToMake>() {
		@Override
		public ThingToMake instantiate(PropertyLookup<ThingToMake> lookup) {
			return new ThingToMake(lookup.valueOf(name, "Nemo"), lookup.valueOf(age, 99));
		}
	};

	@Test
	public void usesDefaultPropertyValuesIfNoPropertiesSpecified() {
		ThingToMake madeThing = make(a(ThingToMake));

		assertThat(madeThing.name, equalTo("Nemo"));
		assertThat(madeThing.age, equalTo(99));
	}

	@Test
	public void useMakeAConvenienceMethodToMake() {
		ThingToMake madeThing1 = makeA(ThingToMake, with(age, 50));
		ThingToMake madeThing2 = makeAn(ThingToMake, with(name, "Bob"));

		assertThat(madeThing1.age, is(50));
		assertThat(madeThing2.name, is("Bob"));
	}

	@Test
	public void overridesDefaultValuesWithExplicitProperties() {
		ThingToMake madeThing = make(a(ThingToMake, with(name, "Bob"), with(age, 10)));

		assertThat(madeThing.name, equalTo("Bob"));
		assertThat(madeThing.age, equalTo(10));

		ThingToMake differentName = make(a(ThingToMake, with(name, "Bill")));
		assertThat(differentName.name, equalTo("Bill"));
	}

	@Test
	public void withNullSetsPropertyValueToNull() {
		ThingToMake madeThing = make(a(ThingToMake, withNull(name), withNull(age)));

		assertThat(madeThing.name, nullValue());
		assertThat(madeThing.age, nullValue());

	}

	public static class ThingContainer {
		public final ThingToMake thing;

		public ThingContainer(ThingToMake thing) {
			this.thing = thing;
		}
	}

	public static Property<ThingContainer, ThingToMake> thing = newProperty();

	public static Instantiator<ThingContainer> ThingContainer = new Instantiator<ThingContainer>() {
		@Override
		public ThingContainer instantiate(PropertyLookup<ThingContainer> lookup) {
			return new ThingContainer(lookup.valueOf(thing, make(a(ThingToMake))));
		}
	};

	@Test
	public void canUseMakersToInitialisePropertyValues() {
		ThingContainer container = make(a(ThingContainer, with(thing, a(ThingToMake, with(name, "foo")))));

		assertThat(container.thing.name, equalTo("foo"));
	}
}
