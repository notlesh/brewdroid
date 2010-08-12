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
	protected String _name;
	protected String _origin;
	protected double _potential;
	protected double _srm;

	/**
	 * Constructor
	 */
	public GrainModel( String guid, String name, String origin, double srm, double potential ) {
		_guid = guid;
		_name = name;
		_origin = origin;
		_srm = srm;
		_potential = potential;
	}

	/**
	 * Returns the guid for this model
	 */
	public String getGUID() {
		return _guid;
	}

	/**
	 * Returns the name 
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Returns the origin
	 */
	public String getOrigin() {
		return _origin;
	}

	/**
	 * Returns the potential for this grain
	 */
	public double getPotential() {
		return _potential;
	}

	/**
	 * Returns the srm
	 */
	public double getSRM() {
		return _srm;
	}

	/**
	 * To String
	 */
	public String toString() {
		return _name;
	}

	/**
	 * Returns all fields
	 */
	@Override
	public String[] getDatabaseFields() {
		return new String[] {
			_guid,
			_name,
			_origin,
			""+ _srm,
			""+ _potential
		};
	}

	/**
	 * Inflator
	 */
	public static class Inflator implements PersistableInflator<GrainModel> {

		/**
		 * Should return a new instance of an object from the given fields
		 */
		@Override
		public GrainModel inflate( String[] fields ) {
			assert (fields.length == 5) :
					"GrainModel requires 5 fields, but got "+ fields.length;

			return new GrainModel(
				fields[0],
				fields[1],
				fields[2],
				Double.parseDouble( fields[3] ),
				Double.parseDouble( fields[4] ));
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
