package com.makeiteasy.tests.objects;

import static com.makeiteasy.Property.newProperty;

import java.util.Random;

import com.makeiteasy.Instantiator;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

public class ReferenceAMaker {
	public static final Property<ReferenceA, Integer> valueA1 = newProperty();
	public static final Property<ReferenceA, Integer> valueA2 = newProperty();

	public static Instantiator<ReferenceA> ReferenceA = new Instantiator<ReferenceA>() {
		@Override
		public ReferenceA instantiate(PropertyLookup<ReferenceA> lookup) {
			Random r = new Random();
			return new ReferenceA(lookup.valueOf(valueA1, r.nextInt()), lookup.valueOf(valueA2, r.nextInt()));
		}
	};
}
