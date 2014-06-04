package br.com.samirrolemberg.simplerssreader;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.tasks.AdicionarFeedTask;
import br.com.samirrolemberg.simplerssreader.u.U;

public class AdicionarFeedActivity extends Activity {

	private AdicionarFeedTask task = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adicionar_feed);
		// Show the Up button in the action bar.
		setupActionBar();
		final EditText url = (EditText) findViewById(R.id.url_feed_adicionar_feed_activity);
		
		
		url.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (URLUtil.isValidUrl(url.getText().toString())) {//se é uma url válida
					if (U.isConnected(AdicionarFeedActivity.this)) {
						Toast.makeText(AdicionarFeedActivity.this, "Enter button "+url.getText().toString(), Toast.LENGTH_SHORT).show();
						LinearLayout layout = (LinearLayout) findViewById(R.id.dados_feed__adicionar_feed_activity);
						layout.setVisibility(View.GONE);

						task = new AdicionarFeedTask(AdicionarFeedActivity.this);
						String[] params = {url.getText().toString()};
						task.execute(params);
					}else{
						Toast.makeText(AdicionarFeedActivity.this, "Não há conexão de internet.", Toast.LENGTH_LONG).show();					
					}
				}else{
					Toast.makeText(AdicionarFeedActivity.this, "URL inválida.", Toast.LENGTH_LONG).show();					
				}
				return false;
			}
		});
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		//getMenuInflater().inflate(R.menu.adicionar_feed, menu);
//		return true;
//	}

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
