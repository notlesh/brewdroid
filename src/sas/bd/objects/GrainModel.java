/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd.objects;

import sas.bd.persistence.*;

import java.text.*;
import java.util.*;

import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.os.Bundle;

/**
 * Grain Model class
 */
public class GrainModel implements Persistable {

	// Member Variables
	protected String _guid;
	protected long _serial;

	protected String _name;
	protected String _origin;
	protected double _potential;
	protected double _srm;

	/**
	 * Constructor
	 */
	public GrainModel( String guid, long serial, String name, String origin, double srm, double potential ) {
		_guid = guid;
		_serial = serial;
		_name = name;
		_origin = origin;
		_srm = srm;
		_potential = potential;
	}

	/**
	 * Returns the name 
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Sets the name
	 */
	public void setName( String name ) {
		_name = name;
	}

	/**
	 * Returns the origin
	 */
	public String getOrigin() {
		return _origin;
	}

	/**
	 * Sets the origin
	 */
	public void setOrigin( String origin ) {
		_origin = origin;
	}

	/**
	 * Returns the potential for this grain
	 */
	public double getPotential() {
		return _potential;
	}

	/**
	 * Sets the potential
	 */
	public void setPotential( double potential ) {
		_potential = potential;
	}

	/**
	 * Returns the srm
	 */
	public double getSRM() {
		return _srm;
	}

	/**
	 * Sets the SRM 
	 */
	public void setSRM( double srm ) {
		_srm = srm;
	}

	/**
	 * To String
	 */
	public String toString() {
		return _name;
	}

	/**
	 * Returns the GUID for this object
	 */
	@Override
	public String getGUID() {
		return _guid;
	}

	/**
	 * Returns the serial of this persistable.
	 */
	@Override
	public long getSerial() {
		return _serial;
	}

	/**
	 * Increments the serial
	 */
	@Override
	public void incrementSerial() {
		_serial++;
	}

	/**
	 * Returns all fields
	 */
	@Override
	public String[] getDatabaseFields() {
		return new String[] {
			_name,
			_origin,
			""+ _srm,
			""+ _potential
		};
	}

	/**
	 * Returns children (none)
	 */
	@Override
	public List<Persistable> getChildPersistables() {
		return null;
	}

	/**
	 * Inflator
	 */
	public static class Inflator implements PersistableInflator<GrainModel> {

		/**
		 * Should return a new instance of an object from the given fields
		 */
		@Override
		public GrainModel inflate( String guid, long serial, String[] fields ) {
			assert (fields.length == GrainDatabase.ALL_FIELDS.length) :
					"GrainModel requires 4 fields, but got "+ fields.length;

			return new GrainModel(
				guid,
				serial,
				fields[0],
				fields[1],
				Double.parseDouble( fields[2] ),
				Double.parseDouble( fields[3] ));
		}

		/**
		 * Should return a list of all (non-standard) fields
		 */
		@Override
		public String[] getAllFields() {
			return GrainDatabase.ALL_FIELDS;
		}

		/**
		 * Returns the version of the current schema
		 */
		@Override
		public int getVersion() {
			return 1;
		}

		/**
		 * Should return the database name
		 */
		@Override
		public String getTableName() {
			return GrainDatabase.TABLE_NAME;
		}

	}
}
