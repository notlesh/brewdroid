/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import sas.bd.objects.*;

import java.text.*;
import java.util.*;

import android.database.*;
import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.os.Bundle;

/**
 * Renders the tabs for the recipe activity.
 */
public class RecipeTabs extends TabActivity {

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.recipe_tabs );

		TabHost tabHost = getTabHost();

		// Style tab
		Intent styleIntent = new Intent( this, RecipeStyle.class );
		TabHost.TabSpec styleSpec = tabHost.newTabSpec( getString( R.string.style ));
		styleSpec.setIndicator( getString( R.string.style ));
		styleSpec.setContent( styleIntent );
		tabHost.addTab( styleSpec );

		// Grain tab
		Intent grainIntent = new Intent();
		TabHost.TabSpec grainSpec = tabHost.newTabSpec( getString( R.string.grain ));
		grainSpec.setContent( grainIntent );
		grainSpec.setIndicator( getString( R.string.grain ));
		tabHost.addTab( grainSpec );

		// Hops tab
		Intent hopsIntent = new Intent();
		TabHost.TabSpec hopsSpec = tabHost.newTabSpec( getString( R.string.hops ));
		hopsSpec.setIndicator( getString( R.string.hops ));
		hopsSpec.setContent( hopsIntent );
		tabHost.addTab( hopsSpec );
		
    }

}
