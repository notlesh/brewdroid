/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd.persistence;

import sas.bd.objects.*;
import static sas.bd.persistence.DatabaseUtil.*;

import java.text.*;
import java.util.*;

import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.database.sqlite.*;
import android.database.*;
import android.os.Bundle;

/**
 * Grain database
 */
public class GrainDatabase extends ObjectDatabase<GrainModel> {

	// singleton
	private static GrainDatabase INSTANCE;

	public static final String TABLE_NAME = "Grains";

	// Fields
	public static final String NAME = "name";
	public static final String ORIGIN = "origin";
	public static final String SRM = "srm";
	public static final String POTENTIAL = "potential";

	public static final String[] ALL_FIELDS = new String[] {
		GUID, NAME, ORIGIN, SRM, POTENTIAL
	};

	/**
	 * Returns the only instance of this class that will exist
	 */
	public static GrainDatabase instance( Context context ) {
		if ( null == INSTANCE ) {
			INSTANCE = new GrainDatabase( context );
		}
		return INSTANCE;
	}

	/**
	 * Creates a new GrainDatabase
	 */
	private GrainDatabase( Context context ) {
		super( new GrainModel.Inflator(), context );
	}
}
