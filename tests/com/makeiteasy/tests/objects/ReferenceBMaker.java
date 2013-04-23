package com.makeiteasy.tests.objects;

import static com.makeiteasy.Property.newProperty;

import java.util.Random;

import com.makeiteasy.Instantiator;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

public class ReferenceBMaker {
	public static final Property<ReferenceB, Integer> valueB1 = newProperty();
	public static final Property<ReferenceB, Integer> valueB2 = newProperty();

	public static Instantiator<ReferenceB> ReferenceB = new Instantiator<ReferenceB>() {
		@Override
		public ReferenceB instantiate(PropertyLookup<ReferenceB> lookup) {
			Random r = new Random();
			return new ReferenceB(lookup.valueOf(valueB1, r.nextInt()), lookup.valueOf(valueB2, r.nextInt()));
		}
	};
}
