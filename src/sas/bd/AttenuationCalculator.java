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
	protected EditText _ogInput;
	protected EditText _fgInput;
	protected EditText _ogBrixInput;
	protected EditText _fgBrixInput;
	protected DecimalFormat _formatter;
	protected SGInputWatcher _sgWatcher;
	protected BrixInputWatcher _brixWatcher;

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

		_sgWatcher = new SGInputWatcher();
		_brixWatcher = new BrixInputWatcher();

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

		_ogInput.addTextChangedListener( _sgWatcher );
		_fgInput.addTextChangedListener( _sgWatcher );
		_ogBrixInput.addTextChangedListener( _brixWatcher );
		_fgBrixInput.addTextChangedListener( _brixWatcher );

	}

	/**
	 * Updates output.
	 */
	protected void update() {
		try {
			double og = Double.parseDouble( _ogInput.getText().toString() );
			double fg = Double.parseDouble( _fgInput.getText().toString() );

			double abv = BrewMath.calculateABV( og, fg );

			_abvInput.setText( ""+ _formatter.format(abv) + "%" );
		} catch ( Exception e ) {
			// e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Updates sg fields.
	 */
	protected synchronized void updateSG() {
		try {
			double og = Double.parseDouble( _ogInput.getText().toString() );
			double fg = Double.parseDouble( _fgInput.getText().toString() );

			double ogBrix = BrewMath.convertSGtoPlato( og );
			double fgBrix = BrewMath.convertSGtoPlato( fg );

			String ogBrixText = _formatter.format( ogBrix );
			String fgBrixText = _formatter.format( fgBrix );

			_brixWatcher.setEnabled( false );

			if ( ! ogBrixText.equals( _ogBrixInput.getText().toString() )) {
				_ogBrixInput.setText( ogBrixText );
			}

			if ( ! fgBrixText.equals( _fgBrixInput.getText().toString() )) {
				_fgBrixInput.setText( fgBrixText );
			}

			_brixWatcher.setEnabled( true );

			update();
		} catch ( Exception e ) {
			e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Updates Brix fields.
	 */
	protected synchronized void updateBrix() {
		try {
			double ogBrix = Double.parseDouble( _ogBrixInput.getText().toString() );
			double fgBrix = Double.parseDouble( _fgBrixInput.getText().toString() );

			double og = BrewMath.convertPlatoToSG( ogBrix );
			double fg = BrewMath.convertPlatoToSG( fgBrix );

			String ogString = _formatter.format( og );
			String fgString = _formatter.format( fg );

			_sgWatcher.setEnabled( false );

			if ( ! ogString.equals( _ogInput.getText().toString() )) {
				_ogInput.setText( ogString );
			}

			if ( ! fgString.equals( _fgInput.getText().toString() )) {
				_fgInput.setText( fgString );
			}

			_sgWatcher.setEnabled( true );

			update();
		} catch ( Exception e ) {
			e.printStackTrace();
			_abvInput.setText( "[Invalid Input]" );
		}
	}

	/**
	 * Listens to changes to SG fields and updates other fields.
	 */
	private class SGInputWatcher implements TextWatcher {

		// Member Variables
		protected boolean _enabled = true;

		/**
		 * Called after text has changed.
		 */
		public void afterTextChanged( Editable s ) {
			if ( _enabled ) {
				updateSG();
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
	 * Listens to changes to brix inputs and updates other fields.
	 */
	private class BrixInputWatcher implements TextWatcher {

		// Member Variables
		protected boolean _enabled = true;

		/**
		 * Called after text has changed.
		 */
		public void afterTextChanged( Editable s ) {
			if ( _enabled ) {
				updateBrix();
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
}
