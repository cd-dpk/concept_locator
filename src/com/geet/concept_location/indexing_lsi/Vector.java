package com.geet.concept_location.indexing_lsi;
public class Vector {
	public double [] dimensionValue;
	public Vector(int dimensions){
		dimensionValue = new double[dimensions];
		for (int i = 0; i < dimensionValue.length; i++) {
			dimensionValue[i]= 0;
		}
	}
	public Vector addWithVector(Vector vector, double[] scales){
		for (int i = 0; i < scales.length; i++) {
			dimensionValue[i] = (dimensionValue[i]+vector.dimensionValue[i]) * scales[i];
		}
		return this;
	}
	public double getDotProductWith(Vector vector, double[] scales){
		double dotProduct = 0.0;
		for (int i = 0; i < scales.length; i++) {
			dotProduct += (dimensionValue[i]*scales[i])*(vector.dimensionValue[i]*scales[i]); 
		}
		return dotProduct;
	}
	public double cosine(Vector vector, double[] scales) {
		double dotProduct = getDotProductWith(vector, scales);
		double scalarProduct = scalarValue(scales)*vector.scalarValue(scales);
		return dotProduct / scalarProduct;
	}
	public double scalarValue(double [] scales){
		double scalarValue = 0;
		for (int i = 0; i < scales.length; i++) {
			scalarValue += dimensionValue[i]* scales[i]; 
		}
		return Math.sqrt(scalarValue);
	}
	@Override
	public String toString() {
		String string="";
		for (int i = 0; i < dimensionValue.length; i++) {
			string+=dimensionValue[i]+",";
		}
		string += "\n";
		return string;
	}
	public Vector getUnitVector(double [] scales){
		Vector unitVector = new Vector(scales.length);
		double scalarValue = scalarValue(scales);
		for (int i = 0; i < scales.length; i++) {
			unitVector.dimensionValue[i] = (dimensionValue[i]* scales[i])/scalarValue; 
		}
		return unitVector;
	}
	public boolean isNullVector(double [] scales){
		if (scalarValue(scales)==0) {
			return true;
		}
		return false;
	} 
}
