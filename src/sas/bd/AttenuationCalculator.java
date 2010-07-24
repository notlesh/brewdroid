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
public class AttenuationCalculator extends Activity {

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
	protected RelatedFieldWatcher _ogWatcher;
	protected RelatedFieldWatcher _ogBrixWatcher;
	protected RelatedFieldWatcher _fgWatcher;
	protected RelatedFieldWatcher _fgBrixWatcher;

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.attenuation_calculator );

		initializeWidgets();
    }

	/**
	 * Initializes widgets
	 */
	private void initializeWidgets() {

		// pre-defined widgets
		_ogInput = (EditText)findViewById(R.id.og);
		_fgInput = (EditText)findViewById(R.id.fg);
		_ogBrixInput = (EditText)findViewById(R.id.ogBrix);
		_fgBrixInput = (EditText)findViewById(R.id.fgBrix);
		_abvInput = (EditText)findViewById(R.id.abv);
		_abwInput = (EditText)findViewById(R.id.abw);
		_apparentAttenuationInput = (EditText)findViewById(R.id.apparent_attenuation);
		_realAttenuationInput = (EditText)findViewById(R.id.real_attenuation);
		_realExtractInput = (EditText)findViewById(R.id.extract_sg);
		_realExtractBrixInput = (EditText)findViewById(R.id.extract_brix);

		// set up listeners
		_ogWatcher = new BrixFromSGWatcher( _ogBrixInput ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		};
		_ogBrixWatcher = new SGFromBrixWatcher( _ogInput ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		}; 
		_fgWatcher = new BrixFromSGWatcher( _fgBrixInput ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		}; 
		_fgBrixWatcher = new SGFromBrixWatcher( _fgInput ) {
			public void postProcess( String input ) {
				updateOutputs();
			}
		}; 

		// tell listeners about each other
		_ogWatcher.addConflictingWatcher( _ogBrixWatcher );
		_ogBrixWatcher.addConflictingWatcher( _ogWatcher );
		_fgWatcher.addConflictingWatcher( _fgBrixWatcher );
		_fgBrixWatcher.addConflictingWatcher( _fgWatcher );

		_ogInput.addTextChangedListener( _ogWatcher );
		_fgInput.addTextChangedListener( _fgWatcher );
		_ogBrixInput.addTextChangedListener( _ogBrixWatcher );
		_fgBrixInput.addTextChangedListener( _fgBrixWatcher );

	}

	/**
	 * Updates output.
	 */
	protected void updateOutputs() {
		try {
			double og = Double.parseDouble( _ogInput.getText().toString() );
			double fg = Double.parseDouble( _fgInput.getText().toString() );

			double abv = BrewMath.calculateABV( og, fg );
			double abw = BrewMath.calculateABW( og, fg );
			double apparentAttenuation = BrewMath.calculateApparentAttenuation( og, fg );
			double realAttenuation = BrewMath.calculateRealExtractPercent( og, fg );
			double extractSG = BrewMath.calculateRealExtract( og, fg );
			double extractBrix = BrewMath.convertSGtoPlato( extractSG );

			_abvInput.setText( ""+ BrewConstants.PERCENT_FORMATTER.format(abv) );
			_abwInput.setText( ""+ BrewConstants.PERCENT_FORMATTER.format(abw) );
			_apparentAttenuationInput.setText( 
					""+ BrewConstants.PERCENT_FORMATTER.format(apparentAttenuation) );
			_realAttenuationInput.setText( 
					""+ BrewConstants.PERCENT_FORMATTER.format(realAttenuation) );
			_realExtractInput.setText( 
					""+ BrewConstants.SG_FORMATTER.format(extractSG) );
			_realExtractBrixInput.setText( 
					""+ BrewConstants.BRIX_FORMATTER.format(extractBrix) );

		} catch ( Exception e ) {
			// e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}
}
