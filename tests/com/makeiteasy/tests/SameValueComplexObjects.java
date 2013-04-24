package com.makeiteasy.tests;

import static com.makeiteasy.MakeItEasy.a;
import static com.makeiteasy.MakeItEasy.sameValueMaker;
import static com.makeiteasy.MakeItEasy.theSame;
import static com.makeiteasy.MakeItEasy.with;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.makeiteasy.Donor;
import com.makeiteasy.Maker;
import com.makeiteasy.tests.objects.ComplexObject;
import com.makeiteasy.tests.objects.ComplexObjectMaker;
import com.makeiteasy.tests.objects.ReferenceA;
import com.makeiteasy.tests.objects.ReferenceAMaker;
import com.makeiteasy.tests.objects.ReferenceB;
import com.makeiteasy.tests.objects.ReferenceBMaker;
import com.makeiteasy.tests.objects.ReferenceC;
import com.makeiteasy.tests.objects.ReferenceCMaker;

public class SameValueComplexObjects {

	@Test
	public void sameDonorForComplexObjects() {
		Donor<ReferenceA> sameA = theSame(ReferenceAMaker.ReferenceA);
		Donor<ReferenceB> sameB = theSame(ReferenceBMaker.ReferenceB);
		Donor<ReferenceC> sameC = theSame(ReferenceCMaker.ReferenceC);
		Maker<ComplexObject> maker = a(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refA, sameA),
				with(ComplexObjectMaker.refB, sameB), with(ComplexObjectMaker.refC, sameC));

		ComplexObject co1 = maker.make();
		ComplexObject co2 = maker.make();

		assertThat(co1.refA, sameInstance(co2.refA));
		assertThat(co1.refB, sameInstance(co2.refB));
		assertThat(co1.refC, sameInstance(co2.refC));
	}

	@Test
	public void sameMakerWithButDoesNotDeliverSameValuesForAllOtherValues() {
		Maker<ReferenceB> refBMaker = a(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = a(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		ComplexObject co2 = maker.but(with(ComplexObjectMaker.refB, refBMaker)).make();

		assertThat(co1.refA.valueA1, not(is(co2.refA.valueA1)));
		assertThat(co1.refB, not(sameInstance(co2.refB)));
		assertThat(co1.refB.valueB1, is(co2.refB.valueB1));
		assertThat(co1.refC.valueC1, not(is(co2.refC.valueC1)));
	}

	@Test
	public void differentRefenrencedMakersDeliverTheirAccordingValues() {
		Maker<ReferenceB> refBMaker = a(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = a(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();

		Maker<ReferenceB> newRefBMaker = refBMaker.but(with(ReferenceBMaker.valueB1, 20));
		ComplexObject co2 = maker.but(with(ComplexObjectMaker.refB, newRefBMaker)).make();

		assertThat(co1.refA.valueA1, not(is(co2.refA.valueA1)));
		assertThat(co1.refB, not(sameInstance(co2.refB)));
		assertThat(co1.refB.valueB1, is(10));
		assertThat(co2.refB.valueB1, is(20));
		assertThat(co1.refC.valueC1, not(is(co2.refC.valueC1)));
	}

	@Test
	public void referencedMakerThatChangesAPropertyValueWitSameInstanceButIsReflected() {
		Maker<ReferenceB> refBMaker = a(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = a(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		refBMaker.sbut(with(ReferenceBMaker.valueB1, 20));
		ComplexObject co2 = maker.make();

		assertThat(co1.refA.valueA1, not(is(co2.refA.valueA1)));
		assertThat(co1.refB, not(sameInstance(co2.refB)));
		assertThat(co1.refB.valueB1, is(10));
		assertThat(co2.refB.valueB1, is(20));
		assertThat(co1.refC.valueC1, not(is(co2.refC.valueC1)));
	}

	@Test
	public void explicitSameMakerDoesDeliverSameValuesButNotSameInstance() {
		Maker<ReferenceB> refBMaker = a(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		ComplexObject co2 = maker.make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refA.valueA1, is(co2.refA.valueA1));
		assertThat(co1.refB.valueB1, is(co2.refB.valueB1));
		assertThat(co1.refC.valueC1, is(co2.refC.valueC1));
	}

	@Test
	public void explicitSameMakerWithButDoesDeliverSameValuesForAllOtherValues() {
		Maker<ReferenceB> refBMaker = a(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		Maker<ReferenceC> refcMaker = a(ReferenceCMaker.ReferenceC, with(ReferenceCMaker.valueC1, 33));
		ComplexObject co2 = maker.but(with(ComplexObjectMaker.refC, refcMaker)).make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refA.valueA1, is(co2.refA.valueA1));
		assertThat(co1.refB.valueB1, is(co2.refB.valueB1));
		assertThat(co1.refC.valueC1, not(is(co2.refC.valueC1)));
		assertThat(co2.refC.valueC1, is(33));
	}

	@Test
	public void explicitSameMakerWithReferencedMakerThatUpdadesAPropertyValueWithSameInstanceButIsReflectedAndOtherValuesChange() {
		Maker<ReferenceB> refBMaker = a(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		refBMaker.sbut(with(ReferenceBMaker.valueB1, 20));
		ComplexObject co2 = maker.make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refA.valueA1, is(co2.refA.valueA1));
		assertThat(co1.refB.valueB1, is(10));
		assertThat(co2.refB.valueB1, is(20));
		assertThat(co1.refB.valueB2, not(is(co2.refB.valueB2)));
		assertThat(co1.refC.valueC1, is(co2.refC.valueC1));
	}

	@Test
	public void explicitSameMakerWithReferencedMakerThatUpdadesAPropertyValueWithSameInstanceButIsReflectedAndOtherValuesStayTheSame() {
		Maker<ReferenceB> refBMaker = sameValueMaker(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, 10));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		refBMaker.sbut(with(ReferenceBMaker.valueB1, 20));
		ComplexObject co2 = maker.make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refA.valueA1, is(co2.refA.valueA1));
		assertThat(co1.refB.valueB1, is(10));
		assertThat(co2.refB.valueB1, is(20));
		assertThat(co1.refB.valueB2, is(co2.refB.valueB2));
		assertThat(co1.refC.valueC1, is(co2.refC.valueC1));
	}

	@Test
	public void explicitSameMakerAfterResetGeneratesNewValue() {
		Maker<ReferenceB> refBMaker = sameValueMaker(ReferenceBMaker.ReferenceB, with(ReferenceBMaker.valueB1, (Integer) null));
		Maker<ComplexObject> maker = sameValueMaker(ComplexObjectMaker.ComplexObject, with(ComplexObjectMaker.refB, refBMaker));

		ComplexObject co1 = maker.make();
		refBMaker.reset().sbut(with(ReferenceBMaker.valueB2, (Integer) null));
		ComplexObject co2 = maker.make();

		assertThat(co1, not(sameInstance(co2)));
		assertThat(co1.refB.valueB1, nullValue());
		assertThat(co2.refB.valueB1, notNullValue());
		assertThat(co1.refB.valueB2, notNullValue());
		assertThat(co2.refB.valueB2, nullValue());
	}
}
