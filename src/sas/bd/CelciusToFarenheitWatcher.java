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
 * Listens to changes from a Celcius based input and updates a related Farenheit field.
 */
public class CelciusToFarenheitWatcher extends RelatedFieldWatcher {

	/**
	 * Constructor
	 */
	public CelciusToFarenheitWatcher( TextView target ) {
		super( target );
	}
	
	/**
	 * Calculate
	 */
	@Override
	public String calculate( String input ) {
		double value = Double.parseDouble( input );
		return BrewConstants.FARENHEIT_FORMATTER.format( BrewMath.convertDegreesCtoF( value ));
	}
}


