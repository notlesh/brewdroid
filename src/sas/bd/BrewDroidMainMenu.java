/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import android.view.*;
import android.widget.*;
import android.content.*;
import android.app.Activity;
import android.os.Bundle;

/**
 * Entry point
 */
public class BrewDroidMainMenu extends Activity {

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

		initializeWidgets();
    }

	/**
	 * Initializes widgets
	 */
	private void initializeWidgets() {

		final Button attenuationButton = (Button)findViewById(R.id.attenuation_button);
		attenuationButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {
				Intent intent = new Intent( 
						BrewDroidMainMenu.this,
						AttenuationCalculator.class );
				BrewDroidMainMenu.this.startActivity( intent );
			}
		});

		final Button hydrometerButton = (Button)findViewById(R.id.hydrometer_button);
		hydrometerButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {
				Intent intent = new Intent( 
						BrewDroidMainMenu.this,
						HydrometerCalculator.class );
				BrewDroidMainMenu.this.startActivity( intent );
			}
		});

		final Button grainsButton = (Button)findViewById(R.id.grains_button);
		grainsButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {
				Intent intent = new Intent( 
						BrewDroidMainMenu.this,
						GrainList.class );
				BrewDroidMainMenu.this.startActivity( intent );
			}
		});

		final Button recipesButton = (Button)findViewById(R.id.recipes_button);
		recipesButton.setOnClickListener( new View.OnClickListener() {
			public void onClick( View v ) {
				Intent intent = new Intent( 
						BrewDroidMainMenu.this,
						RecipeTabs.class );
				BrewDroidMainMenu.this.startActivity( intent );
			}
		});

	}
}
