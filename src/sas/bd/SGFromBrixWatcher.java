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
 * Listens to changes from a Plato based input and updates a related SG field.
 */
public class SGFromBrixWatcher extends RelatedFieldWatcher {

	/**
	 * Constructor
	 */
	public SGFromBrixWatcher( TextView target ) {
		super( target );
	}
	
	/**
	 * Calculate
	 */
	@Override
	public String calculate( String input ) {
		double value = Double.parseDouble( input );
		return BrewConstants.SG_FORMATTER.format( BrewMath.convertPlatoToSG( value ));
	}
}

