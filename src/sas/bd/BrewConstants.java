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
 * Entry point
 */
public class BrewConstants extends Activity {

	// class variables
	protected static NumberFormat NUMBER_FORMATTER = new DecimalFormat( "##.###" );
	protected static NumberFormat BRIX_FORMATTER = NUMBER_FORMATTER;
	protected static NumberFormat SG_FORMATTER = new DecimalFormat( "0.000" );
	protected static NumberFormat PERCENT_FORMATTER = new DecimalFormat( "##0.00%" );
	protected static NumberFormat CELCIUS_FORMATTER = NUMBER_FORMATTER;
	protected static NumberFormat FARENHEIT_FORMATTER = NUMBER_FORMATTER;

}
