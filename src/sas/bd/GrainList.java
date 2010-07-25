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
import android.app.*;
import android.os.Bundle;

/**
 * Grain List
 */
public class GrainList extends ListActivity {

    /** 
	 * Called when the activity is first created. 
	 */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // setContentView( R.layout.grain_list );

		initializeWidgets();
    }

	/**
	 * Initialize widgets
	 */
	public void initializeWidgets() {

		setListAdapter( new GrainArrayAdapter() );

		ListView listView = getListView();
		listView.setTextFilterEnabled( true );

		listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				// noop
			}
		} );

	}

	/**
	 * Custom array adapter class
	 */
	private class GrainArrayAdapter extends ArrayAdapter<GrainModel> {

		/**
		 * Constructor
		 */
		public GrainArrayAdapter() {
			super( GrainList.this, R.layout.grain_list_item, R.id.grain_list_item_name, TEST_GRAINS );
		}

		/**
		 * Override getView
		 */
		public View getView( int position, View convertView, ViewGroup parent ) {
			
			GrainModel model = TEST_GRAINS.get( position );

			LayoutInflater inflater = (LayoutInflater)getSystemService(
			      Context.LAYOUT_INFLATER_SERVICE );
			View row = inflater.inflate( R.layout.grain_list_item, null );
			TextView name = (TextView)row.findViewById( R.id.grain_list_item_name );
			TextView origin = (TextView)row.findViewById( R.id.grain_list_item_origin );
			TextView potential = (TextView)row.findViewById( R.id.grain_list_item_potential );
			TextView srm = (TextView)row.findViewById( R.id.grain_list_item_srm );

			name.setText( model.getName() );
			origin.setText( model.getOrigin() );
			potential.setText( "100%" );
			srm.setText( "12" );

			return row;
		}
	}

	/**
	 * Grain Model class
	 */
	public static class GrainModel {

		// Member Variables
		protected String _name;
		protected String _origin;

		/**
		 * Constructor
		 */
		public GrainModel( String name, String origin ) {
			_name = name;
			_origin = origin;
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
		 * To String
		 */
		public String toString() {
			return _name;
		}
	}

	// test data
	public static final List<GrainModel> TEST_GRAINS = new ArrayList(23);
	static {
		TEST_GRAINS.add( new GrainModel( "Pale Ale 2-row", "USA" ));
		TEST_GRAINS.add( new GrainModel( "Pale Ale 6-row", "USA" ));
		TEST_GRAINS.add( new GrainModel( "Pale Ale 2-row", "Germany" ));
		TEST_GRAINS.add( new GrainModel( "Pale Ale 6-row", "Germany" ));
		TEST_GRAINS.add( new GrainModel( "Pilsener 2-row", "USA" ));
		TEST_GRAINS.add( new GrainModel( "Pilsener", "Germany" ));
		TEST_GRAINS.add( new GrainModel( "Pilsener", "Czech Republic" ));
		TEST_GRAINS.add( new GrainModel( "Munich", "Germany" ));
	}
}
