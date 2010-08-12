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

	public String[] getDatabaseFields();
}
