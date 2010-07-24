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
 * Hydrometer calculations
 */
public class HydrometerCalculator extends Activity {

/*
	// Member Variables
	protected EditText _abvInput;
	protected EditText _abwInput;
	protected EditText _apparentAttenuationInput;
	protected EditText _realAttenuationInput;
	protected EditText _realExtractInput;
	protected EditText _realExtractBrixInput;

	protected EditText _ogInput;
	protected EditText _fgInput;
	protected EditText _ogBrixInput;
	protected EditText _fgBrixInput;
	protected NumberFormat _formatter;
	protected NumberFormat _percentFormatter;
	protected OGInputWatcher _ogWatcher;
	protected OGBrixInputWatcher _ogBrixWatcher;
	protected FGInputWatcher _fgWatcher;
	protected FGBrixInputWatcher _fgBrixWatcher;
*/

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.hydrometer_calculator );

/*
		_formatter = new DecimalFormat( "##.###" );
		_percentFormatter = new DecimalFormat( "###.##%" );

		initializeWidgets();
*/
    }
}
