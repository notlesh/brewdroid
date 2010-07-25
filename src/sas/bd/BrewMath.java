/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import android.view.*;
import android.widget.*;
import android.content.*;
import android.app.Activity;
import android.os.Bundle;

/**
 * Brew math utility class
 */
public class BrewMath {

	/**
	 * Calculate ABV given OG and SG. Represented as a number between 0 and 1.
	 */
	public static double calculateABV( double og, double sg ) {
		// TODO: don't use approximation
		return (og-sg) * 1.29;
	}

	/**
	 * Calculate ABW given OG and SG. Represented as a number between 0 and 1.
	 */
	public static double calculateABW( double og, double sg ) {
		return 0.7608d * ((og - sg) / (1.775 - og));
	}

	/**
	 * Calculate apparent attenuation
	 */
	public static double calculateApparentAttenuation( double og, double sg ) {
		return 1 - (sg-1)/(og-1);
	}

	/**
	 * Calculate the real extract, as a value between 0 and 1
	 */
	public static double calculateRealExtractPercent( double og, double sg ) {
		return 1 - ((0.1808d * (1-og)) + (0.8192d * (1-sg))) / (1-og);
	}

	/**
	 * Calculate in terms of SG the real extract
	 */
	public static double calculateRealExtract( double og, double sg ) {
		return ((0.1808 * og) + (0.8192 * sg));
	}

	/**
	 * Convert plato to SG
	 *
	 * @param brix
	 * @return equivalent in SG
	 */
	public static double convertPlatoToSG( double brix ) {
		// TODO: don't use approximation
		return (brix / (258.6d - ((brix / 258.2d) * 227.1d)) + 1.0d);
	}

	/**
	 * Convert SG to plato
	 *
	 * @param SG reading
	 * @return equivalent in plato scale
	 */
	public static double convertSGtoPlato( double sg ) {
		return 259.0d - (259.0d / sg);
	}

	/**
	 * Converts Farenheit to Celcius
	 */
	public static double convertDegreesFtoC( double f ) {
		return (5.0d/9.0d) * (f - 32.0d);
	}

	/**
	 * Converts Celcius to Farenheit
	 */
	public static double convertDegreesCtoF( double c ) {
		return (9.0d/5.0d) * c + 32.0d;
	}

	/**
	 * Use look up table to estimate SG correction
	 */
	public static double estimateSGCorrection( double sg, double f ) {
		int d = (int)(f+4) / 10; // divide by ten, discard remainder, round up
		switch ( d ) {
			case 4:
				return sg - 0.002d;
			case 5:
				return sg - 0.001d;
			case 6:
				return sg;
			case 7:
				return sg + 0.001d;
			case 8:
				return sg + 0.002d;
			case 9:
				return sg + 0.004d;
			case 10:
				return sg + 0.005d;
			case 11:
				return sg + 0.007d;
			case 12:
				return sg + 0.008d;
			case 13:
				return sg + 0.010d;
			case 14:
				return sg + 0.013d;
			case 15:
				return sg + 0.015d;
		}

		throw new IllegalArgumentException( ""+ f + " is out of temperature range (40F-150F)" );
	}
}
