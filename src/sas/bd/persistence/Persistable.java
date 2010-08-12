/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd.persistence;

import sas.bd.objects.*;

import java.text.*;
import java.util.*;

/**
 * Any implementing class must be able to produce an ordered set of strings useful in a database
 */
public interface Persistable {

	/**
	 * Return the GUID of this object.
	 */
	public String getGUID();

	/**
	 * Return the current version of this bean.
	 */
	public long getSerial();

	/**
	 * Increment the serial
	 */
	public void incrementSerial();

	/**
	 * Return all non-standard fields defined for this type.
	 */
	public String[] getDatabaseFields();

	/**
	 * Return a list of subordinate children
	 */
	public List<Persistable> getChildPersistables();
}
