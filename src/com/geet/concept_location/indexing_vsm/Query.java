package com.geet.concept_location.indexing_vsm;

import java.io.Serializable;

public class Query implements Serializable{
	private double [] vectorInVectorSpaceModel;
	private double [] vectorInExtendedVectorSpaceModel;
	public double[] getVectorInVectorSpaceModel() {
		return vectorInVectorSpaceModel;
	}
	public void setVectorInVectorSpaceModel(double[] vectorInVectorSpaceModel) {
		this.vectorInVectorSpaceModel = vectorInVectorSpaceModel;
	}
	public double[] getVectorInExtendedVectorSpaceModel() {
		return vectorInExtendedVectorSpaceModel;
	}
	public void setVectorInExtendedVectorSpaceModel(
			double[] vectorInExtendedVectorSpaceModel) {
		this.vectorInExtendedVectorSpaceModel = vectorInExtendedVectorSpaceModel;
	}
}