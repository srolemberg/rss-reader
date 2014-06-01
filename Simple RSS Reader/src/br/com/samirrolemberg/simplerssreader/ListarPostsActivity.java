package br.com.samirrolemberg.simplerssreader;

import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.SpinnerAdapter;
import br.com.samirrolemberg.simplerssreader.adapter.ListaFeedSpinnerAdapter;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOFeed;
import br.com.samirrolemberg.simplerssreader.fragment.ListarPostsFragment;
import br.com.samirrolemberg.simplerssreader.model.Feed;

public class ListarPostsActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private Feed feedAux = null;
	private List<Feed> feedsAux = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_posts);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		if (getIntent().getExtras()!=null) {
			feedAux = (Feed) getIntent().getExtras().get("Feed");
		}
		DAOFeed daoFeed = new DAOFeed(this);
		feedsAux = daoFeed.listarTudo();
		DatabaseManager.getInstance().closeDatabase();
		
		//ListaFeedSpinnerAdapter adapter = new ListaFeedSpinnerAdapter(daoFeed.listarTudo(), this);

		SpinnerAdapter adapter = new ListaFeedSpinnerAdapter(feedsAux, this);
		//TODO: tentar conseguir um limitador do SpinnerAdapter
		
		actionBar.setListNavigationCallbacks(adapter, this);
		if (feedAux!=null) {			
			for (int i = 0; i < feedsAux.size(); i++) {
				if (feedAux.getIdFeed()==feedsAux.get(i).getIdFeed()) {
					actionBar.setSelectedNavigationItem(i);
					break;
				}
			}
		}else{
			actionBar.setSelectedNavigationItem(0);
		}
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
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

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		feedAux = feedsAux.get(position);//redefine o feed repassado		
		
		Fragment fragment = new ListarPostsFragment();
		Bundle args = new Bundle();
		args.putInt(ListarPostsFragment.ARG_LIST_POST, position + 1);
		args.putSerializable("Feed", feedAux);
		fragment.setArguments(args);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
				
		return true;
	}

}
