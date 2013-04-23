package com.makeiteasy.tests.objects;

import static com.makeiteasy.MakeItEasy.a;
import static com.makeiteasy.Property.newProperty;

import com.makeiteasy.Instantiator;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

public class ComplexObjectMaker {
	public static final Property<ComplexObject, ReferenceA> refA = newProperty();
	public static final Property<ComplexObject, ReferenceB> refB = newProperty();
	public static final Property<ComplexObject, ReferenceC> refC = newProperty();

	public static Instantiator<ComplexObject> ComplexObject = new Instantiator<ComplexObject>() {
		@Override
		public ComplexObject instantiate(PropertyLookup<ComplexObject> lookup) {
			return new ComplexObject(lookup.valueOf(refA, a(ReferenceAMaker.ReferenceA)), lookup.valueOf(refB,
					a(ReferenceBMaker.ReferenceB)), lookup.valueOf(refC, a(ReferenceCMaker.ReferenceC)));
		}
	};
}
