package com.geet.concept_location.utils;
public class IndexRange {
	public int a = -1;
	public int b = -1;
	public boolean isValid() {
		if (!(a == -1 && b ==-1)) {
			return true;
		}
		return false;
	}
}
