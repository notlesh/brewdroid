/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import java.text.*;
import java.util.*;

import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.database.sqlite.*;
import android.os.Bundle;

/**
 * Grain database
 */
public class GrainDatabase extends SQLiteOpenHelper {

	public static final int VERSION = 1;
	public static final String DATABASE_NAME = "BrewDroid";
	public static final String TABLE_NAME = "Grains";

	public static final String GUID = "guid";
	public static final String NAME = "name";
	public static final String ORIGIN = "origin";
	public static final String SRM = "srm";
	public static final String POTENTIAL = "potential";

	// Queries
	public static final String CREATE_TABLE =
			"CREATE TABLE "+ TABLE_NAME +" ("+ GUID +" TEXT, "+ NAME +" TEXT, "+ ORIGIN
			+" TEXT, "+ SRM +" INTEGER, "+ POTENTIAL +" REAL);";

	/**
	 * Creates a new GrainDatabase
	 */
	public GrainDatabase( Context context ) {
		super( context, DATABASE_NAME, null, VERSION );
	}

	/**
	 * Called on create
	 */
	@Override
	public void onCreate( SQLiteDatabase db ) {
		db.execSQL( CREATE_TABLE );
	}

	/**
	 * Called when database needs to be upgraded
	 */
	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		// ??
	}
}
