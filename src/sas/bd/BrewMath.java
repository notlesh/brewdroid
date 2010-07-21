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
	 * Calculate ABV given OG and SG
	 */
	public static double calculateABV( double og, double sg ) {
		// TODO: don't use approximation
		return (og-sg) * 129;
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
}
