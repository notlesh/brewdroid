/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd.persistence;

import sas.bd.objects.*;

import java.text.*;
import java.util.*;

/**
 * A class that can inflate an object from a set of strings
 */
public interface PersistableInflator<T extends Persistable> {

	/**
	 * Should return a new instance of an object from the given fields
	 */
	public T inflate( String[] fields );

	/**
	 * Should return a list of all (non-standard) fields
	 */
	public String[] getAllFields();

	/**
	 * Returns the version of the current schema
	 */
	public int getVersion();

	/**
	 * Should return the database name
	 */
	public String getTableName();

}
