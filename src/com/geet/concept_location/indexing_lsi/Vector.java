package com.geet.concept_location.indexing_lsi;
import java.io.Serializable;
public class Vector implements Serializable{
	public double [] dimensionValue;
	public Vector(int dimensions){
		dimensionValue = new double[dimensions];
		for (int i = 0; i < dimensionValue.length; i++) {
			dimensionValue[i]= 0;
		}
	}
	public Vector addWithVector(Vector vector){
		for (int i = 0; i < dimensionValue.length; i++) {
			dimensionValue[i] = (dimensionValue[i]+vector.dimensionValue[i]);
		}
		return this;
	}
	public double getDotProductWith(Vector vector){
		double dotProduct = 0.0;
		for (int i = 0; i < dimensionValue.length; i++) {
			dotProduct += (dimensionValue[i])*(vector.dimensionValue[i]); 
		}
		return dotProduct;
	}
	// refined
	public double cosine(Vector vector) {
	    double product = 0.0;
	    double xsLengthSquared = 0.0;
	    double ysLengthSquared = 0.0;
	    for (int k = 0; k < dimensionValue.length; ++k) {
	        double scaledXs = dimensionValue[k];
	        double scaledYs = vector.dimensionValue[k];
	        xsLengthSquared += scaledXs * scaledXs;
	        ysLengthSquared += scaledYs * scaledYs;
	        product += scaledXs * scaledYs;
	    }
	    if (xsLengthSquared ==0 || ysLengthSquared == 0) {
			return -1;
		}
	    return product / Math.sqrt(xsLengthSquared * ysLengthSquared);
	}
	public double dotProduct(Vector vector) {
		double sum = 0.0;
		for (int k = 0; k < dimensionValue.length; ++k){
			sum += dimensionValue[k] * vector.dimensionValue[k] ;
		}
		return sum;
	}
	public double scalarValue(){
		double scalarValue = 0;
		for (int i = 0; i < dimensionValue.length; i++) {
			scalarValue += dimensionValue[i]; 
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
	public boolean isNull(){
		if (scalarValue()!=0) {
			return false;
		}
		return true;
	}
	public String toCSVString() {
		String string="";
		for (int i = 0; i < dimensionValue.length; i++) {
			string+=dimensionValue[i]+",";
		}
		return string;
	}
}