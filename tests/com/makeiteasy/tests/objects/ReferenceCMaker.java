package com.makeiteasy.tests.objects;

import static com.makeiteasy.Property.newProperty;

import java.util.Random;

import com.makeiteasy.Instantiator;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

public class ReferenceCMaker {
	public static final Property<ReferenceC, Integer> valueC1 = newProperty();
	public static final Property<ReferenceC, Integer> valueC2 = newProperty();

	public static Instantiator<ReferenceC> ReferenceC = new Instantiator<ReferenceC>() {
		@Override
		public ReferenceC instantiate(PropertyLookup<ReferenceC> lookup) {
			Random r = new Random();
			return new ReferenceC(lookup.valueOf(valueC1, r.nextInt()), lookup.valueOf(valueC2, r.nextInt()));
		}
	};

}
