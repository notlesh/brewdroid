/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import sas.bd.objects.*;
import sas.bd.persistence.*;

import java.text.*;
import java.util.*;

import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.Activity;
import android.os.Bundle;

/**
 * Entry point
 */
public class EditGrain extends Activity {

	// Member Variables
	protected Button _button;
	protected EditText _nameInput;
	protected EditText _originInput;
	protected EditText _potentialInput;
	protected EditText _srmInput;
	protected String _grainGUID;
	protected GrainModel _grain;

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_grain );

		Intent intent = getIntent();
		_grainGUID = intent.getStringExtra( DatabaseUtil.GUID );

		initializeWidgets();
    }

	/**
	 * Initializes widgets
	 */
	private void initializeWidgets() {

		_nameInput = (EditText)findViewById( R.id.edit_grain_name );
		_originInput = (EditText)findViewById( R.id.edit_grain_origin );
		_potentialInput = (EditText)findViewById( R.id.edit_grain_potential );
		_srmInput = (EditText)findViewById( R.id.edit_grain_srm );

		if ( null != _grainGUID ) {
			_grain = GrainDatabase.instance(this).get( _grainGUID );
			_nameInput.setText( _grain.getName() );
			_originInput.setText( _grain.getOrigin() );
			_potentialInput.setText( ""+ _grain.getPotential() );
			_srmInput.setText( ""+ _grain.getSRM() );
		}

		// TODO: set initial values based on existing grain (if edit)

		// submit button
		_button = (Button)findViewById( R.id.edit_grain_submit );
		// TODO: change button label based on intent
		_button.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {
				submit();
			}
		} );

	}

	/**
	 * Validates form and attempts to submit values
	 */
	private void submit() {

		double srm = Double.parseDouble( _srmInput.getText().toString() );
		double potential = Double.parseDouble( _potentialInput.getText().toString() );

		String guid = _grainGUID;
		if ( null == _grainGUID ) {
			guid = UUID.randomUUID().toString();

			_grain = new GrainModel(
					guid,
					1l,
					_nameInput.getText().toString(),
					_originInput.getText().toString(),
					srm,
					potential );
		} else {

			_grain.incrementSerial();
			_grain.setName( _nameInput.getText().toString() );
			_grain.setOrigin( _originInput.getText().toString() );
			_grain.setSRM( srm );
			_grain.setPotential( potential );
		}

		Marshaller.instance(this).save( _grain );

		// TODO: validate
		setResult( RESULT_OK, new Intent() );
		finish();
	}
}
