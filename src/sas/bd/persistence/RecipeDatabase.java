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
 * Recipe database
 */
public class RecipeDatabase {

	// singleton
	private static RecipeDatabase INSTANCE;

	public static final int VERSION = 1;
	public static final String TABLE_NAME = "Recipes";

	// Fields
	public static final String NAME = "name";
	public static final String AUTHOR = "author";
	public static final String CREATE_DATE = "create_date";
	public static final String STYLE = "style";

	public static final String[] ALL_FIELDS = new String[] {
		GUID, NAME, AUTHOR, CREATE_DATE, STYLE
	};

	protected SQLiteDatabase _db;
	protected Context _context;

	// queries
	protected final String _createStatement;
	protected final String _insertStatement;
	protected final String _updateStatement;
	protected final String _listStatement;
	protected final String _fetchStatement;
	protected final String _deleteStatement;

	// Compiled queries

	/**
	 * Returns the only instance of this class that will exist
	 *
	 * TODO: what happens when other activiies try to use this?
	 */
	public static RecipeDatabase instance( Context context ) {
		if ( null == INSTANCE ) {
			INSTANCE = new RecipeDatabase( context );
		}
		return INSTANCE;
	}

	/**
	 * Creates a new RecipeDatabase
	 */
	private RecipeDatabase( Context context ) {
		_context = context;

		// generate queries
		_createStatement = DatabaseUtil.generateCreateStatement( TABLE_NAME, ALL_FIELDS );
		_insertStatement = DatabaseUtil.generateInsertStatement( TABLE_NAME, ALL_FIELDS.length );
		_updateStatement = DatabaseUtil.generateUpdateStatement( TABLE_NAME, ALL_FIELDS );
		_listStatement = DatabaseUtil.generateListAllStatement( TABLE_NAME );
		_fetchStatement = DatabaseUtil.generateFetchStatement( TABLE_NAME );
		_deleteStatement = DatabaseUtil.generateDeleteStatement( TABLE_NAME );

		RecipeDatabaseOpenHelper helper = new RecipeDatabaseOpenHelper( _context );

		_db = helper.getWritableDatabase();

	}

	/**
	 * Handles creation and updating of database as necessary
	 */
	private class RecipeDatabaseOpenHelper extends SQLiteOpenHelper {

		/**
		 * Constructor
		 */
		public RecipeDatabaseOpenHelper( Context context ) {
			super( context, DATABASE_NAME, null, VERSION );
		}

		/**
		 * Called on create
		 */
		@Override
		public void onCreate( SQLiteDatabase db ) {
			db.execSQL( _createStatement );
			// TODO: populate "default" recipes -- from web?
		}

		/**
		 * Called when database needs to be upgraded
		 */
		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
			// N/A (yet)
		}

	}
}
