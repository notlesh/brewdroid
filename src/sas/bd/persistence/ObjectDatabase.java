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

	public static final int NUM_STANDARD_FIELDS = 2; // guid, serial

	// Member Variables
	protected final SQLiteDatabase _db;
	protected final Context _context;
	protected final PersistableInflator<T> _inflator;

	protected final String[] _objectFields;
	protected final String _tableName;
	protected final int _version;

	// Queries
	protected final String _createStatement;
	protected final String _insertStatement;
	protected final String _updateStatement;
	protected final String _listStatement;
	protected final String _fetchStatement;
	protected final String _deleteStatement;
	protected final String _serialStatement;

	// Compiled queries
	private final SQLiteStatement _insertQuery;
	private final SQLiteStatement _updateQuery;
	private final SQLiteStatement _fetchQuery;
	private final SQLiteStatement _deleteQuery;
	private final SQLiteStatement _serialQuery;

	/**
	 * Constructor
	 */
	public ObjectDatabase( PersistableInflator<T> inflator, Context context ) {
		_context = context;
		_inflator = inflator;

		_objectFields = _inflator.getAllFields();
		_tableName = _inflator.getTableName();
		_version = _inflator.getVersion();

		// generate superset of fields
		String[] allFields = new String[ NUM_STANDARD_FIELDS + _objectFields.length ];
		allFields[0] = GUID;
		allFields[1] = SERIAL;
		System.arraycopy( _objectFields, 0, allFields, NUM_STANDARD_FIELDS, _objectFields.length );

		// generate queries for this database type
		_createStatement = DatabaseUtil.generateCreateStatement( _tableName, allFields );
		_insertStatement = DatabaseUtil.generateInsertStatement( _tableName, allFields.length );
		_updateStatement = DatabaseUtil.generateUpdateStatement( _tableName, allFields );
		_listStatement = DatabaseUtil.generateListAllStatement( _tableName );
		_fetchStatement = DatabaseUtil.generateFetchStatement( _tableName );
		_deleteStatement = DatabaseUtil.generateDeleteStatement( _tableName );
		_serialStatement = DatabaseUtil.generateSelect( _tableName, SERIAL );

		// obtain database object
		// TODO: ensure that this is created correctly:
		//		what happens in different contexts?
		//		if databases are not the same object, will transactions work as expected?
		Helper helper = new Helper( _context );
		_db = helper.getWritableDatabase();

		// compile queries
		_insertQuery = _db.compileStatement( _insertStatement );
		_updateQuery = _db.compileStatement( _updateStatement );
		_fetchQuery = _db.compileStatement( _fetchStatement );
		_deleteQuery = _db.compileStatement( _deleteStatement );
		_serialQuery = _db.compileStatement( _serialStatement );
		
	}

	/**
	 * Inserts a Persistable into the database
	 */
	protected void insert( T object ) {

		_insertQuery.clearBindings();

		// standard fields
		_insertQuery.bindString( 1, object.getGUID() );
		_insertQuery.bindLong( 2, object.getSerial() );

		// object's fields
		String[] values = object.getDatabaseFields();
		for ( int i=0; i<values.length; i++ ) {
			_insertQuery.bindString( i + NUM_STANDARD_FIELDS + 1, values[i] );
		}
		_insertQuery.executeInsert();
	}

	/**
	 * Updates an object
	 */
	protected void update( T object ) {
		
		_updateQuery.clearBindings();
		String[] values = object.getDatabaseFields();

		// standard fields (except guid)
		_updateQuery.bindLong( 1, object.getSerial() );

		for ( int i=0; i<values.length; i++ ) {
			_updateQuery.bindString( i + NUM_STANDARD_FIELDS, values[i] );
		}
		_updateQuery.bindString( values.length + NUM_STANDARD_FIELDS, object.getGUID() );
		_updateQuery.execute();
	}

	/**
	 * Remove an object
	 * TODO: referential integrity check (or make delete a soft delete)
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

			assert (cursor.getColumnCount() == (_objectFields.length + NUM_STANDARD_FIELDS))
					: "Invalid field count. Got "+ cursor.getColumnCount() 
						+", expected "+ (_objectFields.length + NUM_STANDARD_FIELDS);

			// standard fields
			long serial = cursor.getLong(1);
			
			// bean's fields
			String[] values = new String[_objectFields.length];
			for ( int i=0; i<_objectFields.length; i++ ) {
				values[i] = cursor.getString(i + NUM_STANDARD_FIELDS);
			}

			return _inflator.inflate( guid, serial, values );

		} finally {
			cursor.close();
		}
	}

	/**
	 * Returns the serial for an object
	 */
	public long getSerial( String guid ) {
		Cursor cursor = _db.rawQuery( _serialStatement, new String[] { guid } );
		try {

			if ( cursor.getCount() == 0 ) {
				return 0l;
			}

			assert (cursor.getCount() == 1)
					: "More than one object for guid "+ guid;

			cursor.moveToNext();

			assert (cursor.getColumnCount() == 1)
					: ""+ cursor.getColumnCount() +" returned for getSerial()";
			
			return cursor.getLong(0);

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

				assert (cursor.getColumnCount() == (_objectFields.length + NUM_STANDARD_FIELDS))
						: "Invalid field count. Got "+ cursor.getColumnCount() 
							+", expected "+ (_objectFields.length + NUM_STANDARD_FIELDS);

				// standard fields
				String guid = cursor.getString(0);
				long serial = cursor.getLong(1);
				
				// bean's fields
				String[] values = new String[_objectFields.length];
				for ( int i=NUM_STANDARD_FIELDS; i<_objectFields.length; i++ ) {
					values[i] = cursor.getString(i);
				}

				objects.add( _inflator.inflate( guid, serial, values ));
			}
			
			return objects;
		} finally {
			cursor.close();
		}
	}

	// ================================
	// Expose methods from the database
	// ================================

	/**
	 * Start a transaction. See SQLiteDatabase documentation.
	 */
	public void beginTransaction() {
		_db.beginTransaction();
	}

	/**
	 * End a transaction. See SQLiteDatabase documentation.
	 */
	public void endTransaction() {
		_db.endTransaction();
	}

	/**
	 * Flag a transaction as successful. See SQLiteDatabase documentation.
	 */
	public void setTransactionSuccessful() {
		_db.setTransactionSuccessful();
	}

	/**
	 * Handles creation and updating of database as necessary
	 */
	private class Helper extends SQLiteOpenHelper {

		/**
		 * Constructor
		 */
		public Helper( Context context ) {
			super( context, DATABASE_NAME, null, DATABASE_VERSION );
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
