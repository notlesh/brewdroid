/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import java.text.*;

import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.Activity;
import android.os.Bundle;

/**
 * Listens to changes from a Farenheit based input and updates a related Celcius field.
 */
public class FarenheitToCelciusWatcher extends RelatedFieldWatcher {

	/**
	 * Constructor
	 */
	public FarenheitToCelciusWatcher( TextView target ) {
		super( target );
	}
	
	/**
	 * Calculate
	 */
	@Override
	public String calculate( String input ) {
		double value = Double.parseDouble( input );
		return BrewConstants.CELCIUS_FORMATTER.format( BrewMath.convertDegreesFtoC( value ));
	}
}


