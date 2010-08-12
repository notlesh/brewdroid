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
 * Renders the style form for a recipe
 */
public class RecipeStyle extends Activity {

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle bundle ) {
        super.onCreate( bundle );
        setContentView( R.layout.recipe_style );
		
    }

}
