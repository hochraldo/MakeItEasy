package com.makeiteasy.tests;

import static com.makeiteasy.MakeItEasy.a;
import static com.makeiteasy.MakeItEasy.sameValueMaker;
import static com.makeiteasy.MakeItEasy.theSame;
import static com.makeiteasy.MakeItEasy.with;
import static com.makeiteasy.Property.newProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Test;

import com.makeiteasy.Donor;
import com.makeiteasy.Instantiator;
import com.makeiteasy.Maker;
import com.makeiteasy.Property;
import com.makeiteasy.PropertyLookup;

public class SameValueComplexObjects {

	static class ComplexObject {
		ReferenceA refA;
		ReferenceB refB;
		ReferenceC refC;

		public ComplexObject(ReferenceA refA, ReferenceB refB, ReferenceC refC) {
			this.refA = refA;
			this.refB = refB;
			this.refC = refC;
		}

	}

	public static final Property<ComplexObject, ReferenceA> refA = newProperty();
	public static final Property<ComplexObject, ReferenceB> refB = newProperty();
	public static final Property<ComplexObject, ReferenceC> refC = newProperty();

	public static Instantiator<ComplexObject> ComplexObject = new Instantiator<ComplexObject>() {
		@Override
		public ComplexObject instantiate(PropertyLookup<ComplexObject> lookup) {
			return new ComplexObject(lookup.valueOf(refA, a(ReferenceA)), lookup.valueOf(refB, a(ReferenceB)), lookup.valueOf(refC,
					a(ReferenceC)));
		}
	};

	static class ReferenceA {
		int valueA1;
		int valueA2;

		public ReferenceA(int valueA1, int valueA2) {
			this.valueA1 = valueA1;
			this.valueA2 = valueA2;
		}

	}

	public static final Property<ReferenceA, Integer> valueA1 = newProperty();
	public static final Property<ReferenceA, Integer> valueA2 = newProperty();

	public static Instantiator<ReferenceA> ReferenceA = new Instantiator<ReferenceA>() {
		@Override
		public ReferenceA instantiate(PropertyLookup<ReferenceA> lookup) {
			Random r = new Random();
			return new ReferenceA(lookup.valueOf(valueA1, r.nextInt()), lookup.valueOf(valueA2, r.nextInt()));
		}
	};

	static class ReferenceB {
		int valueB1;
		int valueB2;

		public ReferenceB(int valueB1, int valueB2) {
			this.valueB1 = valueB1;
			this.valueB2 = valueB2;
		}

	}

	public static final Property<ReferenceB, Integer> valueB1 = newProperty();
	public static final Property<ReferenceB, Integer> valueB2 = newProperty();

	public static Instantiator<ReferenceB> ReferenceB = new Instantiator<ReferenceB>() {
		@Override
		public ReferenceB instantiate(PropertyLookup<ReferenceB> lookup) {
			Random r = new Random();
			return new ReferenceB(lookup.valueOf(valueB1, r.nextInt()), lookup.valueOf(valueB2, r.nextInt()));
		}
	};

	static class ReferenceC {
		int valueC1;
		int valueC2;

		public ReferenceC(int valueC1, int valueC2) {
			this.valueC1 = valueC1;
			this.valueC2 = valueC2;
		}

	}

	public static final Property<ReferenceC, Integer> valueC1 = newProperty();
	public static final Property<ReferenceC, Integer> valueC2 = newProperty();

	public static Instantiator<ReferenceC> ReferenceC = new Instantiator<ReferenceC>() {
		@Override
		public ReferenceC instantiate(PropertyLookup<ReferenceC> lookup) {
			Random r = new Random();
			return new ReferenceC(lookup.valueOf(valueC1, r.nextInt()), lookup.valueOf(valueC2, r.nextInt()));
		}
	};

	@Test
	public void sameDonorForComplexObjects() {
		Donor<ReferenceA> sameA = theSame(ReferenceA);
		Donor<ReferenceB> sameB = theSame(ReferenceB);
		Donor<ReferenceC> sameC = theSame(ReferenceC);
		Maker<ComplexObject> maker = a(ComplexObject, with(refA, sameA), with(refB, sameB), with(refC, sameC));

		ComplexObject co1 = maker.make();
		ComplexObject co2 = maker.make();

		assertThat(co1.refA, sameInstance(co2.refA));
		assertThat(co1.refB, sameInstance(co2.refB));
		assertThat(co1.refC, sameInstance(co2.refC));
	}

	@Test
	public void sameMakerWithButDoesNotDeliverSameValuesForAllOtherValues() {
		Maker<ReferenceB> refBMaker = a(ReferenceB, with(valueB1, 10));
		Maker<ComplexObject> maker = a(ComplexObject, with(refB, refBMaker));

		ComplexObject co1 = maker.make();
		ComplexObject co2 = maker.but(with(refB, refBMaker)).make();

		assertThat(co1.refA.valueA1, not(is(co2.refA.valueA1)));
		assertThat(co1.refB, not(sameInstance(co2.refB)));
		assertThat(co1.refB.valueB1, is(co2.refB.valueB1));
		assertThat(co1.refC.valueC1, not(is(co2.refC.valueC1)));
	}

	@Test
	public void explicitSameMakerDoesDeliverSameValuesButNotSameInstance() {
		Maker<ReferenceB> refBMaker = a(ReferenceB, with(valueB1, 10));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObject, with(refB, refBMaker));

		ComplexObject co1 = maker.make();
		ComplexObject co2 = maker.make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refA.valueA1, is(co2.refA.valueA1));
		assertThat(co1.refB.valueB1, is(co2.refB.valueB1));
		assertThat(co1.refC.valueC1, is(co2.refC.valueC1));
	}

	@Test
	public void explicitSameMakerWithButDoesDeliverSameValuesForAllOtherValues() {
		Maker<ReferenceB> refBMaker = a(ReferenceB, with(valueB1, 10));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObject, with(refB, refBMaker));

		ComplexObject co1 = maker.make();
		Maker<ReferenceC> refcMaker = a(ReferenceC, with(valueC1, 33));
		ComplexObject co2 = maker.but(with(refC, refcMaker)).make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refA.valueA1, is(co2.refA.valueA1));
		assertThat(co1.refB.valueB1, is(co2.refB.valueB1));
		assertThat(co1.refC.valueC1, not(is(co2.refC.valueC1)));
		assertThat(co2.refC.valueC1, is(33));
	}
}
