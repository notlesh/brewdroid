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
 * Handles the saving and fetching of potentially complex types to and from various databases.
 */
public class Marshaller {

	// Singleton
	private static Marshaller INSTANCE;

	// Member Variables
	protected Map<Class, ObjectDatabase> _registry;
	protected Map<Class, List<Class>> _subordinateTypes;

	/**
	 * Returns the instance of this class
	 */
	public static Marshaller instance( Context context ) {
		if ( null == INSTANCE ) {
			INSTANCE = new Marshaller( context );
		}
		return INSTANCE;
	}

	/**
	 * Constructor
	 */
	private Marshaller( Context context ) {
		_registry = new HashMap();
		_subordinateTypes = new HashMap();

		// ==============
		// register types
		// ==============

		// GrainModel
		_registry.put( GrainModel.class, GrainDatabase.instance( context ));
		_subordinateTypes.put( GrainModel.class, new ArrayList(0) );
	}

	/**
	 * Call to register an object type with a database.
	 */
	public void register( Class type, ObjectDatabase database ) {
		_registry.put( type, database );
	}

	/**
	 * Call to register an object type with a list of subordinate types.
	 */
	public void registerSubordinateTypes( Class type, List<Class> subordinates ) {
		_subordinateTypes.put( type, subordinates );
	}

	/**
	 * Saves an object
	 */
	public void save( Persistable object ) {
		ObjectDatabase database = _registry.get( object.getClass() );

		assert (null != database)
				: "No database registered for "+ object.getClass().getName();

		try {
			database.beginTransaction();

			long currentSerial = database.getSerial( object.getGUID() );

			if ( currentSerial != object.getSerial() - 1 ) {
				throw new IllegalStateException( "Can't save "+ object.getClass().getName() 
						+" with serial "+ object.getSerial() +", serial "+ currentSerial
						+" exists in database" );
			}

			// save this object
			if ( object.getSerial() == 1l ) {
				database.insert( object );
			} else {
				database.update( object );
			}

			// save child objects
			List<Persistable> children = object.getChildPersistables();
			if ( null != children ) {
				for ( Persistable child : children ) {
					save( child );
				}
			}

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
}
