/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import sas.bd.objects.*;

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

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.edit_grain );

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

		GrainDatabase db = GrainDatabase.instance( this );

		double srm = Double.parseDouble( _srmInput.getText().toString() );
		double potential = Double.parseDouble( _potentialInput.getText().toString() );

		GrainModel grain = new GrainModel(
				UUID.randomUUID().toString(),
				_nameInput.getText().toString(),
				_originInput.getText().toString(),
				srm,
				potential );

		db.insert( grain );

		// TODO: validate
		// TODO: submit values (depending on caller's intent)
		setResult( RESULT_OK, new Intent() );
		finish();
	}
}
