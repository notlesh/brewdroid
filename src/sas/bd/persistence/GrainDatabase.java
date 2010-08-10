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
import android.app.*;
import android.database.sqlite.*;
import android.database.*;
import android.os.Bundle;

/**
 * Grain database
 */
public class GrainDatabase {

	// singleton
	private static GrainDatabase INSTANCE;

	public static final int VERSION = 1;
	public static final String DATABASE_NAME = "BrewDroid";
	public static final String TABLE_NAME = "Grains";

	// Fields
	public static final String GUID = "_id";
	public static final String NAME = "name";
	public static final String ORIGIN = "origin";
	public static final String SRM = "srm";
	public static final String POTENTIAL = "potential";

	protected SQLiteDatabase _db;
	protected Context _context;

	// Queries
	public static final String CREATE_TABLE_STATEMENT =
			"CREATE TABLE "+ TABLE_NAME +" ("+ GUID +" TEXT, "+ NAME +" TEXT, "+ ORIGIN
			+" TEXT, "+ SRM +" INTEGER, "+ POTENTIAL +" REAL);";

	public static final String INSERT_STATEMENT =
			"INSERT INTO "+ TABLE_NAME +" VALUES ( ?, ?, ?, ?, ? );";

	public static final String UPDATE_STATEMENT =
			"UPDATE "+ TABLE_NAME +" SET "+ NAME +"=?, "+ ORIGIN +"=?, "+ SRM +"=?, "+
			POTENTIAL +"=? WHERE "+ GUID +"=?";

	public static final String FETCH_STATEMENT =
			"SELECT * FROM "+ TABLE_NAME +" WHERE "+ GUID +"=?";

	public static final String LIST_STATEMENT =
			"SELECT * FROM "+ TABLE_NAME;

	public static final String DELETE_STATEMENT =
			"DELETE FROM "+ TABLE_NAME +" WHERE "+ GUID +"=?";
	

	// Compiled queries
	private final SQLiteStatement _insertQuery;
	private final SQLiteStatement _updateQuery;
	private final SQLiteStatement _fetchQuery;
	private final SQLiteStatement _deleteQuery;

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
		_context = context;

		GrainDatabaseOpenHelper helper = new GrainDatabaseOpenHelper( _context );

		_db = helper.getWritableDatabase();
		_insertQuery = _db.compileStatement( INSERT_STATEMENT );
		_updateQuery = _db.compileStatement( UPDATE_STATEMENT );
		_fetchQuery = _db.compileStatement( FETCH_STATEMENT );
		_deleteQuery = _db.compileStatement( DELETE_STATEMENT );
	}

	/**
	 * Inserts a GrainModel into the database
	 */
	public void insert( GrainModel model ) {
		_insertQuery.clearBindings();
		_insertQuery.bindString( 1, model.getGUID() );
		_insertQuery.bindString( 2, model.getName() );
		_insertQuery.bindString( 3, model.getOrigin() );
		_insertQuery.bindDouble( 4, model.getSRM() );
		_insertQuery.bindDouble( 5, model.getPotential() );
		_insertQuery.executeInsert();
	}

	/**
	 * Updates a GrainModel
	 */
	public void update( GrainModel model ) {
		_updateQuery.clearBindings();
		_updateQuery.bindString( 5, model.getGUID() );
		_updateQuery.bindString( 1, model.getName() );
		_updateQuery.bindString( 2, model.getOrigin() );
		_updateQuery.bindDouble( 3, model.getSRM() );
		_updateQuery.bindDouble( 4, model.getPotential() );
		_updateQuery.execute();
	}

	/**
	 * Remove a grain
	 * TODO: referential integrity check (or make delet a soft delete)
	 */
	public void remove( String guid ) {
		_deleteQuery.clearBindings();
		_deleteQuery.bindString( 1, guid );
		_deleteQuery.execute();
	}

	/**
	 * Fetch a single grain
	 */
	public GrainModel getGrain( String guid ) {
		Cursor cursor = _db.rawQuery( FETCH_STATEMENT, new String[] { guid } );
		int count = cursor.getCount();
		if ( count == 0 ) {
			return null;
		} else if ( count > 1 ) {
			throw new IllegalStateException( "More than one grain with a guid" );
		} else {
			cursor.moveToNext();

			String name = cursor.getString(1);
			String origin = cursor.getString(2);
			double srm = cursor.getDouble(3);
			double potential = cursor.getDouble(4);

			return new GrainModel( guid, name, origin, srm, potential );
		}
	}

	/**
	 * Returns a cursor for all results
	 */
	public Cursor getListCursor() {
		return _db.rawQuery( LIST_STATEMENT, new String[0] );
	}

	/**
	 * Returns a full list of grains
	 */
	public List<GrainModel> listGrains() {

		Cursor cursor = getListCursor();
		List<GrainModel> grains = new ArrayList( cursor.getCount() );

		while ( ! cursor.isLast() ) {
			cursor.moveToNext();

			String guid = cursor.getString(0);
			String name = cursor.getString(1);
			String origin = cursor.getString(2);
			double srm = cursor.getDouble(3);
			double potential = cursor.getDouble(4);

			GrainModel grain = new GrainModel( guid, name, origin, srm, potential );
			grains.add( grain );
		}
		
		return grains;
	}

	/**
	 * Handles creation and updating of database as necessary
	 */
	private class GrainDatabaseOpenHelper extends SQLiteOpenHelper {

		/**
		 * Constructor
		 */
		public GrainDatabaseOpenHelper( Context context ) {
			super( context, DATABASE_NAME, null, VERSION );
		}

		/**
		 * Called on create
		 */
		@Override
		public void onCreate( SQLiteDatabase db ) {
			db.execSQL( CREATE_TABLE_STATEMENT );
			// TODO: populate "default" grains -- from web?
		}

		/**
		 * Called when database needs to be upgraded
		 */
		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
			// N/A
		}

	}
}
