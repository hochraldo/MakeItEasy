package com.makeiteasy;

public class NullValueDonor<T> implements Donor<T> {

	@Override
	public T value() {
		return null;
	}

}
