/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd.objects;

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
public class GrainModel {

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
}
