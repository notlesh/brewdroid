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
	protected DecimalFormat _formatter;
	protected OGInputWatcher _ogWatcher;
	protected OGBrixInputWatcher _ogBrixWatcher;
	protected FGInputWatcher _fgWatcher;
	protected FGBrixInputWatcher _fgBrixWatcher;

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.attenuation_calculator );

		_formatter = new DecimalFormat();

		initializeWidgets();
    }

	/**
	 * Initializes widgets
	 */
	private void initializeWidgets() {

		// pre-defined widgets
		_ogInput = (EditText)findViewById(R.id.og);
		_ogInput.setSingleLine();

		_fgInput = (EditText)findViewById(R.id.fg);
		_fgInput.setSingleLine();

		_ogBrixInput = (EditText)findViewById(R.id.ogBrix);
		_ogBrixInput.setSingleLine();

		_fgBrixInput = (EditText)findViewById(R.id.fgBrix);
		_fgBrixInput.setSingleLine();

		_abvInput = (EditText)findViewById(R.id.abv);
		_abvInput.setEnabled( false );
		_abvInput.setSingleLine();

		_abwInput = (EditText)findViewById(R.id.abw);
		_abwInput.setEnabled( false );
		_abwInput.setSingleLine();

		_apparentAttenuationInput = (EditText)findViewById(R.id.apparent_attenuation);
		_apparentAttenuationInput.setEnabled( false );
		_apparentAttenuationInput.setSingleLine();

		_realAttenuationInput = (EditText)findViewById(R.id.real_attenuation);
		_realAttenuationInput.setEnabled( false );
		_realAttenuationInput.setSingleLine();

		_realExtractInput = (EditText)findViewById(R.id.extract_sg);
		_realExtractInput.setEnabled( false );
		_realExtractInput.setSingleLine();

		_realExtractBrixInput = (EditText)findViewById(R.id.extract_brix);
		_realExtractBrixInput.setEnabled( false );
		_realExtractBrixInput.setSingleLine();

		// set up listeners
		_ogWatcher = new OGInputWatcher();
		_ogBrixWatcher = new OGBrixInputWatcher(); 
		_fgWatcher = new FGInputWatcher(); 
		_fgBrixWatcher = new FGBrixInputWatcher(); 

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
			double apparentAttenuation = BrewMath.calculateApparentAttenuation( og, fg ) * 100;
			double realAttenuation = BrewMath.calculateRealExtractPercent( og, fg ) * 100;
			double extractSG = BrewMath.calculateRealExtract( og, fg );
			double extractBrix = BrewMath.convertSGtoPlato( extractSG );

			_abvInput.setText( ""+ _formatter.format(abv) + "%" );
			_abwInput.setText( ""+ _formatter.format(abw) + "%" );
			_apparentAttenuationInput.setText( ""+ _formatter.format(apparentAttenuation) + "%" );
			_realAttenuationInput.setText( ""+ _formatter.format(realAttenuation) + "%" );
			_realExtractInput.setText( ""+ _formatter.format(extractSG) );
			_realExtractBrixInput.setText( ""+ _formatter.format(extractBrix) );

		} catch ( Exception e ) {
			// e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Processes the og field.
	 */
	protected synchronized void processOG() {
		try {

			double og = Double.parseDouble( _ogInput.getText().toString() );
			double ogBrix = BrewMath.convertSGtoPlato( og );
			String ogBrixText = _formatter.format( ogBrix );

			// update brix field
			_ogBrixWatcher.setEnabled( false );
			_ogBrixInput.setTextKeepState( ogBrixText );
			_ogBrixWatcher.setEnabled( true );

			updateOutputs();

		} catch ( Exception e ) {
			e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Processes the fg field.
	 */
	protected synchronized void processFG() {
		try {

			double fg = Double.parseDouble( _fgInput.getText().toString() );
			double fgBrix = BrewMath.convertSGtoPlato( fg );
			String fgBrixText = _formatter.format( fgBrix );

			// update brix field
			_fgBrixWatcher.setEnabled( false );
			_fgBrixInput.setTextKeepState( fgBrixText );
			_fgBrixWatcher.setEnabled( true );

			updateOutputs();

		} catch ( Exception e ) {
			e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Processes the og brix field.
	 */
	protected synchronized void processOGBrix() {
		try {

			double ogBrix = Double.parseDouble( _ogBrixInput.getText().toString() );
			double og = BrewMath.convertPlatoToSG( ogBrix );
			String ogText = _formatter.format( og );

			// update og field
			_ogWatcher.setEnabled( false );
			_ogInput.setTextKeepState( ogText );
			_ogWatcher.setEnabled( true );

			updateOutputs();

		} catch ( Exception e ) {
			e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Processes the fg brix field.
	 */
	protected synchronized void processFGBrix() {
		try {

			double fgBrix = Double.parseDouble( _fgBrixInput.getText().toString() );
			double fg = BrewMath.convertPlatoToSG( fgBrix );
			String fgText = _formatter.format( fg );

			// update fg field
			_fgWatcher.setEnabled( false );
			_fgInput.setTextKeepState( fgText );
			_fgWatcher.setEnabled( true );

			updateOutputs();

		} catch ( Exception e ) {
			e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * BaseWatcher 
	 */
	private abstract class BaseWatcher implements TextWatcher {

		// Member Variables
		protected boolean _enabled = true;

		/**
		 * Called on change
		 */
		public abstract void onChange( Editable s );

		/**
		 * Called after text has changed.
		 */
		public void afterTextChanged( Editable s ) {
			if ( _enabled ) {
				onChange( s );
			}
		}

		/**
		 * Called before text changed
		 */
		public void beforeTextChanged( CharSequence s, int start, int count, int after ) {
		}

		/**
		 * Called as text is changed
		 */
		public void onTextChanged( CharSequence s, int start, int before, int count ) {
		}

		/**
		 * Sets whether this listener is enabled
		 */
		public void setEnabled( boolean enabled ) {
			_enabled = enabled;
		}
	}

	/**
	 * Listens to changes to the OG field
	 */
	private class OGInputWatcher extends BaseWatcher {

		/**
		 * Called after text has changed.
		 */
		public void onChange( Editable s ) {
			processOG();
		}
	}

	/**
	 * Listens to changes to the OG brix field
	 */
	private class OGBrixInputWatcher extends BaseWatcher {

		/**
		 * Called after text has changed.
		 */
		public void onChange( Editable s ) {
			processOGBrix();
		}
	}

	/**
	 * Listens to changes to the FG field
	 */
	private class FGInputWatcher extends BaseWatcher {

		/**
		 * Called after text has changed.
		 */
		public void onChange( Editable s ) {
			processFG();
		}
	}

	/**
	 * Listens to changes to the FG brix field
	 */
	private class FGBrixInputWatcher extends BaseWatcher {

		/**
		 * Called after text has changed.
		 */
		public void onChange( Editable s ) {
			processFGBrix();
		}
	}
}
