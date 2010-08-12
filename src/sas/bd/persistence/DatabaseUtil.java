/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd.persistence;

import sas.bd.objects.*;

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
 * Utility to help with database operations
 */
public class DatabaseUtil {

	// well known fields
	public static final String DATABASE_NAME = "BrewDroid";
	public static final String GUID = "_id";

	/**
	 * Generates a SQL statement to create a table with the given fields
	 */
	public static String generateCreateStatement( String tableName, String[] fields ) {
		StringBuffer buffer = new StringBuffer( 25 + (8 * fields.length) );
		buffer.append( "CREATE TABLE " )
		      .append( tableName )
			  .append( " (" );

		for ( int i=0; i<fields.length; i++ ) {

			buffer.append( fields[i] );

			if ( i < fields.length - 1 ) {
				buffer.append( ',' );
			}
		}

		buffer.append( ");" );
		return buffer.toString();
	}

	/**
	 * Generates a SQL statement to insert a row with the given number of fields
	 */
	public static String generateInsertStatement( String tableName, int numFields ) {
		StringBuffer buffer = new StringBuffer( 24 + (numFields * 2) + tableName.length() );
		buffer.append( "INSERT INTO " )
		      .append( tableName )
			  .append( " VALUES (" );

		for ( int i=0; i<numFields; i++ ) {

			buffer.append( '?' );

			if ( i < numFields - 1 ) {
				buffer.append( ',' );
			}
		}

		buffer.append( ");" );

		return buffer.toString();
	}

	/**
	 * Generates a SQL statement to update the given fields in a row
	 */
	public static String generateUpdateStatement( String tableName, String[] fields ) {
		StringBuffer buffer = new StringBuffer( 35 + (9 * fields.length) );
		buffer.append( "UPDATE " )
		      .append( tableName )
			  .append( " SET " );

		int i = 0;
		if ( fields[0] == GUID ) { // should work since GUID is hard coded
			i = 1;
		}

		for ( ; i<fields.length; i++ ) {

			buffer.append( fields[i] )
			      .append( "=?" );

			if ( i < fields.length - 1 ) {
				buffer.append( ',' );
			}
		}

		buffer.append( " WHERE " )
		      .append( GUID )
			  .append( "=?;" );

		return buffer.toString();
	}

	/**
	 * Generates a SQL statement to list all elements in a given table.
	 */
	public static String generateListAllStatement( String tableName ) {
		StringBuffer buffer = new StringBuffer( tableName.length() + 15 );
		buffer.append( "SELECT * FROM " )
		      .append( tableName )
			  .append( ';' );
		return buffer.toString();
	}

	/**
	 * Generates a SOL statement to fetch a row with all fields for a given GUID
	 */
	public static String generateFetchStatement( String tableName ) {
		StringBuffer buffer = new StringBuffer( tableName.length() + 27 );
		buffer.append( "SELECT * FROM " )
		      .append( tableName )
			  .append( " WHERE " )
			  .append( GUID )
			  .append( "=?;" );
		return buffer.toString();
	}

	/**
	 * Generates a SOL statement to delete a row for a given GUID
	 */
	public static String generateDeleteStatement( String tableName ) {
		StringBuffer buffer = new StringBuffer( tableName.length() + 35 );
		buffer.append( "DELETE FROM " )
		      .append( tableName )
			  .append( " WHERE " )
			  .append( GUID )
			  .append( "=?;" );
		return buffer.toString();
	}

}
