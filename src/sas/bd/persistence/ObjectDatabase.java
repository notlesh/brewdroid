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
 * Base class for databases about an obect. Given a table name, list of fields, and a version, this
 * class will automate the standard CRUDL (create, update, delete, list, fetch) functions.
 */
public abstract class ObjectDatabase<T extends Persistable> {

	// Member Variables
	protected final SQLiteDatabase _db;
	protected final Context _context;
	protected final PersistableInflator<T> _inflator;

	protected final String[] _allFields;
	protected final String _tableName;
	protected final int _version;

	// Queries
	protected final String _createStatement;
	protected final String _insertStatement;
	protected final String _updateStatement;
	protected final String _listStatement;
	protected final String _fetchStatement;
	protected final String _deleteStatement;

	// Compiled queries
	private final SQLiteStatement _insertQuery;
	private final SQLiteStatement _updateQuery;
	private final SQLiteStatement _fetchQuery;
	private final SQLiteStatement _deleteQuery;

	/**
	 * Constructor
	 */
	public ObjectDatabase( PersistableInflator<T> inflator, Context context ) {
		_context = context;
		_inflator = inflator;

		_allFields = _inflator.getAllFields();
		_tableName = _inflator.getTableName();
		_version = _inflator.getVersion();

		System.out.println( "------------- Creating table "+ _tableName );
		_createStatement = DatabaseUtil.generateCreateStatement( _tableName, _allFields );
		System.out.println( "Create: "+ _createStatement );
		_insertStatement = DatabaseUtil.generateInsertStatement( _tableName, _allFields.length );
		System.out.println( "Insert: "+ _insertStatement );
		_updateStatement = DatabaseUtil.generateUpdateStatement( _tableName, _allFields );
		System.out.println( "Update: "+ _updateStatement );
		_listStatement = DatabaseUtil.generateListAllStatement( _tableName );
		System.out.println( "List: "+ _listStatement );
		_fetchStatement = DatabaseUtil.generateFetchStatement( _tableName );
		System.out.println( "Fetch: "+ _fetchStatement );
		_deleteStatement = DatabaseUtil.generateDeleteStatement( _tableName );
		System.out.println( "Delete: "+ _deleteStatement );

		Helper helper = new Helper( _context );

		_db = helper.getWritableDatabase();
		_insertQuery = _db.compileStatement( _insertStatement );
		_updateQuery = _db.compileStatement( _updateStatement );
		_fetchQuery = _db.compileStatement( _fetchStatement );
		_deleteQuery = _db.compileStatement( _deleteStatement );
		
	}

	/**
	 * Inserts a Persistabel into the database
	 */
	public void insert( T object ) {

		_insertQuery.clearBindings();
		String[] values = object.getDatabaseFields();
		for ( int i=0; i<values.length; i++ ) {
			_insertQuery.bindString( i+1, values[i] );
		}
		_insertQuery.executeInsert();
	}

	/**
	 * Updates an object
	 */
	public void update( T object ) {
		_updateQuery.clearBindings();
		String[] values = object.getDatabaseFields();
		for ( int i=1; i<values.length; i++ ) {
			_updateQuery.bindString( i, values[i] );
		}
		_updateQuery.bindString( values.length, values[0] );
		_updateQuery.execute();
	}

	/**
	 * Remove an object
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
	public T get( String guid ) {
		Cursor cursor = _db.rawQuery( _fetchStatement, new String[] { guid } );
		try {

			assert (cursor.getCount() == 1)
					: "More than one object for guid "+ guid;

			cursor.moveToNext();

			assert (cursor.getColumnCount() == _allFields.length)
					: "Invalid field count. Got "+ cursor.getColumnCount() 
						+", expected "+ _allFields.length;
			
			String[] values = new String[_allFields.length];
			for ( int i=0; i<_allFields.length; i++ ) {
				values[i] = cursor.getString(i);
			}

			return _inflator.inflate( values );

		} finally {
			cursor.close();
		}
	}

	/**
	 * Returns a cursor for all results
	 */
	public Cursor getListCursor() {
		return _db.rawQuery( _listStatement, new String[0] );
	}

	/**
	 * Returns a full list of grains
	 */
	public List<T> list() {

		Cursor cursor = getListCursor();
		try {
			List<T> objects = new ArrayList( cursor.getCount() );

			while ( ! cursor.isLast() ) {
				cursor.moveToNext();

				assert (cursor.getColumnCount() == _allFields.length)
						: "Invalid field count. Got "+ cursor.getColumnCount() 
							+", expected "+ _allFields.length;
				
				String[] values = new String[_allFields.length];
				for ( int i=0; i<_allFields.length; i++ ) {
					values[i] = cursor.getString(i+1);
				}

				objects.add( _inflator.inflate( values ));
			}
			
			return objects;
		} finally {
			cursor.close();
		}
	}

	/**
	 * Handles creation and updating of database as necessary
	 */
	private class Helper extends SQLiteOpenHelper {

		/**
		 * Constructor
		 */
		public Helper( Context context ) {
			super( context, DATABASE_NAME, null, _version );
		}

		/**
		 * Called on create
		 */
		@Override
		public void onCreate( SQLiteDatabase db ) {
			db.execSQL( _createStatement );
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
