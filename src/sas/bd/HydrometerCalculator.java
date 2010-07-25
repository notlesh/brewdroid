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

	// Member Variables
	protected EditText _sgReading;
	protected EditText _brixReading;
	protected EditText _tempF;
	protected EditText _tempC;
	protected EditText _correctedReading;
	protected EditText _correctedBrixReading;

	protected RelatedFieldWatcher _sgWatcher;
	protected RelatedFieldWatcher _brixWatcher;
	protected RelatedFieldWatcher _tempFWatcher;
	protected RelatedFieldWatcher _tempCWatcher;

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.hydrometer_calculator );

		initializeWidgets();
    }

	/**
	 * Initialize widgets
	 */
	public void initializeWidgets() {

		// pre-defined widgets
		_sgReading = (EditText)findViewById( R.id.sg_reading );
		_brixReading = (EditText)findViewById( R.id.brix_reading );
		_tempF = (EditText)findViewById( R.id.temp_reading_f );
		_tempC = (EditText)findViewById( R.id.temp_reading_c );
		_correctedReading = (EditText)findViewById( R.id.corrected_sg );
		_correctedBrixReading = (EditText)findViewById( R.id.corrected_brix );

		// set up listeners
		_sgWatcher = new BrixFromSGWatcher( _brixReading ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		};
		_brixWatcher = new SGFromBrixWatcher( _sgReading ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		};
		_tempFWatcher = new FarenheitToCelciusWatcher( _tempC ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		};
		_tempCWatcher = new CelciusToFarenheitWatcher( _tempF ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		};

		// tell listeners about each other
		_sgWatcher.addConflictingWatcher( _brixWatcher );
		_brixWatcher.addConflictingWatcher( _sgWatcher );
		_tempFWatcher.addConflictingWatcher( _tempCWatcher );
		_tempCWatcher.addConflictingWatcher( _tempFWatcher );

		_sgReading.addTextChangedListener( _sgWatcher );
		_brixReading.addTextChangedListener( _brixWatcher );
		_tempF.addTextChangedListener( _tempFWatcher );
		_tempC.addTextChangedListener( _tempCWatcher );

	}

	/**
	 * Updates output.
	 */
	protected void updateOutputs() {
		try {
			// TODO:
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
