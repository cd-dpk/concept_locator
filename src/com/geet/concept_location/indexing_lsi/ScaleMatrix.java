package com.geet.concept_location.indexing_lsi;

import java.io.Serializable;

public class ScaleMatrix implements Serializable{
	double [] scale ;
	
	public double[] getScales() {
		return scale;
	}

	public void setScales(double[] scales) {
		this.scale = scales;
	}

	public ScaleMatrix(double[] scales) {
		setScales(scales);
	}
}
