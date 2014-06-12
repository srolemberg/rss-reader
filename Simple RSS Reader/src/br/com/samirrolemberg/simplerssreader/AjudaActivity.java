package br.com.samirrolemberg.simplerssreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bugsense.trace.BugSenseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import br.com.samirrolemberg.simplerssreader.adapter.ExpandableListAdapter;

public class AjudaActivity extends Activity {

	ExpandableListAdapter listAdapter;
	ExpandableListView listView;
	List<String> listHeader;
	Map<String, List<String>> listChild;

	@Override
	protected void onStart() {
		super.onStart();
		//new DAO(AjudaActivity.this);
		//TODO:BUGSENSE - REMOVER DEPOIS?
		BugSenseHandler.startSession(AjudaActivity.this);
	}
	@Override
	protected void onStop() {
		super.onStart();
		//TODO:BUGSENSE - REMOVER DEPOIS?
		BugSenseHandler.closeSession(AjudaActivity.this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//new DAO(AjudaActivity.this);

		//TODO:BUGSENSE - REMOVER DEPOIS?
	    BugSenseHandler.initAndStartSession(this, getString(R.string.bugsense__api_key));

		setContentView(R.layout.activity_ajuda);
		setupActionBar();
		
		listView = (ExpandableListView) findViewById(R.id.expandable_ajuda);
		
		prepareListData();
		
		listAdapter = new ExpandableListAdapter(this, listHeader, listChild);
		
		listView.setAdapter(listAdapter);
		
	}
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	private void prepareListData(){
		listHeader = new ArrayList<String>();
		listChild = new HashMap<String, List<String>>();
		
		//add DATA HEADER
		listHeader.add("HEADER 1");
		listHeader.add("HEADER 2");
		listHeader.add("HEADER 3");
		//add DATA CHILD
		List<String> header_1_list = new ArrayList<String>();
		header_1_list.add("Child 1 Header 1");
		header_1_list.add("Child 2 Header 1");
		header_1_list.add("Child 2 Header 1");
		List<String> header_2_list = new ArrayList<String>();
		header_2_list.add("Child 1 Header 2");
		header_2_list.add("Child 2 Header 2");
		header_2_list.add("Child 2 Header 2");
		List<String> header_3_list = new ArrayList<String>();
		header_3_list.add("Child 1 Header 3");
		header_3_list.add("Child 2 Header 3");
		header_3_list.add("Child 2 Header 3");
		
		listChild.put(listHeader.get(0), header_1_list);
		listChild.put(listHeader.get(1), header_2_list);
		listChild.put(listHeader.get(2), header_3_list);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
