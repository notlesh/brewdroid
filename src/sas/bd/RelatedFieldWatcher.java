/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import java.text.*;
import java.util.*;

import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.Activity;
import android.os.Bundle;

/**
 * Listens to changes from a field and updates a related field.
 *
 * Conflictng listeners can be added. These will be disabled before updating the target. These
 * listeners should generally be those attached to the target; this will avoid cyclical updates from
 * being fired.
 */
public abstract class RelatedFieldWatcher implements TextWatcher {

	// Member Variables
	protected boolean _enabled = true;
	protected final TextView _target;
	protected final List<RelatedFieldWatcher> _conflictingWatchers;

	/**
	 * Constructor
	 */
	public RelatedFieldWatcher( TextView target ) {
		_target = target;
		_conflictingWatchers = new ArrayList(1);
	}

	/**
	 * This field should process the input and calculate an output, which will be applied to the
	 * target.
	 */
	public abstract String calculate( String input );

	/**
	 * This method can be overriden to do extra work after the update.
	 */
	public void postProcess( String input ) { /* override me */ };

	/**
	 * Adds a conflicting listener.
	 */
	public final void addConflictingWatcher( RelatedFieldWatcher watcher ) {
		if ( watcher == this ) {
			throw new IllegalArgumentException( "Can't conflict with self!" );
		}
		_conflictingWatchers.add( watcher );
	}

	/**
	 * Removes a conflicting listener.
	 */
	public final void removeConflictingWatcher( RelatedFieldWatcher watcher ) {
		if ( watcher == this ) {
			throw new IllegalArgumentException( "Can't conflict with self!" );
		}
		_conflictingWatchers.remove( watcher );
	}

	/**
	 * Process input
	 */
	protected void process( String input ) {
		if ( _enabled ) {
			for ( RelatedFieldWatcher watcher : _conflictingWatchers ) {
				watcher.disable();
			}

			try {
				_target.setText( calculate( input ));
				postProcess( input );
			} catch ( Exception e ) {
				e.printStackTrace();
				// noop, means invalid input
			}

			for ( RelatedFieldWatcher watcher : _conflictingWatchers ) {
				watcher.enable();
			}
		}
	}

	/**
	 * Called after text has changed.
	 */
	@Override
	public void afterTextChanged( Editable editable ) {
		process( editable.toString() );
	}

	/**
	 * Called before text changed
	 */
	@Override
	public void beforeTextChanged( CharSequence s, int start, int count, int after ) {
		// don't care
	}

	/**
	 * Called as text is changed
	 */
	@Override
	public void onTextChanged( CharSequence s, int start, int before, int count ) {
		// don't care
	}

	/**
	 * Sets whether this listener is enabled
	 */
	public void setEnabled( boolean enabled ) {
		_enabled = enabled;
	}

	/**
	 * Disables this field
	 */
	public void disable() {
		_enabled = false;
	}

	/**
	 * Enables this field 
	 */
	public void enable() {
		_enabled = true;
	}
}

