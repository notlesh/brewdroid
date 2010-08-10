/**
 * Copyright 2010 Stephen Shelton, all rights reserved.
 */
package sas.bd;

import sas.bd.objects.*;

import java.text.*;
import java.util.*;

import android.database.*;
import android.view.*;
import android.text.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.os.Bundle;

/**
 * Grain List
 */
public class GrainList extends ListActivity {

	// menu numbers
	private static final int NEW_GRAIN = 1;
	private static final int EDIT_GRAIN = 2;
	private static final int DELETE_GRAIN = 3;

	// request codes
	private static final int NEW_GRAIN_REQUEST = 1001;
	private static final int EDIT_GRAIN_REQUEST = 1002;

	// Member Variables
	private Cursor _cursor;
	private GrainCursorAdapter _adapter;
	private Map<View, String> _viewMap;

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // setContentView( R.layout.grain_list );

		_cursor = GrainDatabase.instance(GrainList.this).getListCursor();
		initializeWidgets();
    }

	/**
	 * Called to create options menu
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		menu.add( 0, NEW_GRAIN, 0, getString( R.string.newLabel ));
		return true;
	}

	/**
	 * Called when (main activity) menu option is selected
	 */
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch ( item.getItemId() ) {
			case NEW_GRAIN:
				Intent intent = new Intent( this, EditGrain.class );
				startActivityForResult( intent, NEW_GRAIN_REQUEST );
				break;
		}
		return true;
	}

	/**
	 * Initialize widgets
	 */
	public void initializeWidgets() {

		_viewMap = new HashMap();
		_adapter = new GrainCursorAdapter();
		setListAdapter( _adapter );
		registerForContextMenu( getListView() );
	}

	/**
	 * Handle result from sub-screens
	 */
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
		System.out.println( "onActivityResult()" );
		if ( resultCode == Activity.RESULT_CANCELED ) {
			// noop
		} else if ( resultCode != Activity.RESULT_OK ) {
			throw new IllegalArgumentException( "How did we get here?" );
		}

		switch ( requestCode ) {
			case NEW_GRAIN_REQUEST:
			case EDIT_GRAIN_REQUEST:
				_viewMap.clear();
				_cursor = GrainDatabase.instance(GrainList.this).getListCursor();
				_adapter.changeCursor( _cursor );
				break;
		}
	}

	/**
	 * Called when an item is clicked
	 */
	@Override
	public void onListItemClick( ListView listView, View view, int position, long id ) {
		Toast.makeText( GrainList.this, "Clicked "+ _viewMap.get(view), 150 ).show();

		// TODO: if we are in select-grain mode, select this. otherwise, noop.
	}

	/**
	 * Called to create context menu
	 */
	@Override
	public void onCreateContextMenu( 
			ContextMenu menu, 
			View listView, 
			ContextMenu.ContextMenuInfo info ) {

		AdapterView.AdapterContextMenuInfo adapterContextInfo 
				= (AdapterView.AdapterContextMenuInfo)info;
		View view = adapterContextInfo.targetView;

		// construct context menu
		String grainName = ((TextView)view.findViewById( R.id.grain_list_item_name )).getText().toString();
		menu.setHeaderTitle( getString( R.string.edit ) +" "+ grainName );
		menu.add( 1, EDIT_GRAIN, 0, getString( R.string.edit ) );
		menu.add( 1, DELETE_GRAIN, 0, getString( R.string.delete ) );
	}

	/**
	 * Called to handle menu click (from context menu)
	 */
	@Override
	public boolean onContextItemSelected( MenuItem item ) {
		AdapterView.AdapterContextMenuInfo info
				= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		View view = info.targetView;
		String id = _viewMap.get( view );
		String grainName = ((TextView)view.findViewById( R.id.grain_list_item_name )).getText().toString();

		switch ( item.getItemId() ) {
			case EDIT_GRAIN:
				Intent intent = new Intent( this, EditGrain.class );
				intent.putExtra( GrainDatabase.GUID, id );
				startActivityForResult( intent, NEW_GRAIN_REQUEST );
				break;
			case DELETE_GRAIN:

				// TODO: try/catch
				GrainDatabase.instance(GrainList.this).remove( id );
				_viewMap.clear();
				_cursor = GrainDatabase.instance(GrainList.this).getListCursor();
				_adapter.changeCursor( _cursor );

				String message = String.format( getString(R.string.removed), grainName );
				Toast.makeText( GrainList.this, message, 200 ).show();
				break;
		}

		return true;
		
	}

	/**
	 * GrainCursorAdapter
	 */
	private class GrainCursorAdapter extends SimpleCursorAdapter {

		/**
		 * Constructor
		 */
		public GrainCursorAdapter() {
			super( 
				GrainList.this,
				R.layout.grain_list_item,
				_cursor,
				new String[] {
					GrainDatabase.NAME,
					GrainDatabase.ORIGIN,
					GrainDatabase.SRM,
					GrainDatabase.POTENTIAL
				},
				new int[] {
					R.id.grain_list_item_name,
					R.id.grain_list_item_origin,
					R.id.grain_list_item_srm,
					R.id.grain_list_item_potential
				} );

		}

		/**
		 * Override getview so we can add long click listeners
		 */
		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {



			if ( _cursor.getPosition() < _cursor.getCount() ) {

				View view = super.getView( position, convertView, parent );

				// grab id before cursor is advanced
				String id = _cursor.getString(0);

				// hang on to it until we redraw
				_viewMap.put( view, id );

				return view;
			} else {

				// if empty, delegate to super and be done
				return super.getView( position, convertView, parent );
			}
		}
	}
}

