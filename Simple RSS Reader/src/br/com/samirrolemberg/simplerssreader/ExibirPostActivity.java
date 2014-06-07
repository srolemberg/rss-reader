package br.com.samirrolemberg.simplerssreader;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.adapter.ListarPostSpinnerAdapter;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesAnexosPostDialog;
import br.com.samirrolemberg.simplerssreader.fragment.ExibirPostFragment;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class ExibirPostActivity extends Activity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private Feed feedAux = null;
	private Post postAux = null;
	private List<Post> postListAux = null;
	private ShareActionProvider mShareActionProvider = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exibir_post);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		feedAux = (Feed) getIntent().getExtras().get("Feed");			
		postAux = (Post) getIntent().getExtras().get("Post");			
		DAOPost daoPost = new DAOPost(this);
		postListAux = daoPost.listarTudo(feedAux);
		DatabaseManager.getInstance().closeDatabase();
		
		SpinnerAdapter adapter = new ListarPostSpinnerAdapter(postListAux, this);
		//TODO: tentar conseguir um limitador do SpinnerAdapter
		
		actionBar.setListNavigationCallbacks(adapter, this);
		if (postAux!=null) {			
			for (int i = 0; i < postListAux.size(); i++) {
				if (postAux.getIdPost()==postListAux.get(i).getIdPost()) {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_fragment_exibir_post, menu);
		
		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_fragment_exibir_post_compartilhar).getActionProvider();
		mShareActionProvider.setShareIntent(doShare());
		return super.onCreateOptionsMenu(menu);
	}

	private Intent doShare(){
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    //intent.putExtra(Intent.EXTRA_SUBJECT, postAux.getDescricao());
	    intent.putExtra(Intent.EXTRA_TEXT, postAux.getLink());
	    intent.putExtra(Intent.EXTRA_TITLE, postAux.getTitulo());
	    return intent;
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
			//NavUtils.navigateUpFromSameTask(this);
			//NECESSITA QUE SEJA REPASSADO OS DADOS DO FEED QUE ESTAVA SENDO LIDO NA TELA ANTERIOR
			//COM O ESQUEMA DE HOME O ANDROID DESTROI A TELA ANTERIOR E REMONTA COM OS DADOS ANTIGOS
			//COM ON BACK PRESS ELE SIMPLEMENTE RETORNA PARA UMA TELA ANTERIOR QUE NÃO NECESSARIAMENE
			//SERA A SUA
			Intent intent = new Intent(this, ListarPostsActivity.class);
			intent.putExtra("Feed", feedAux);
			//intent.putExtra("Post", postAux); - resolver esse intent se um dia precisar resetar a posição do feed que foi lido na tela anterior
			NavUtils.navigateUpTo(this, intent);
			break;
		case R.id.action_fragment_exibir_post_anexos:
			//Toast.makeText(ExibirPostActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			DAOAnexo daoAnexo = new DAOAnexo(ExibirPostActivity.this);
			if (daoAnexo.size(postAux)>0) {
				LayoutInflater inflater = this.getLayoutInflater();
				View detalhe = (new DetalhesAnexosPostDialog(ExibirPostActivity.this, inflater.inflate(R.layout.dialog_anexos_post, null), postAux)).create();
				new AlertDialog.Builder(ExibirPostActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Anexos do Post")
				.setView(detalhe)
				.setPositiveButton("Fechar", null)
				.show();				
			}else{
				Toast.makeText(ExibirPostActivity.this, "Este post não possui anexo(s).", Toast.LENGTH_SHORT).show();
			}
			DatabaseManager.getInstance().closeDatabase();
			break;

		default:
			break;

		}
		//return super.onOptionsItemSelected(item);
		return true;
	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		//if (id == R.id.action_settings) {
//		if (id == android.R.id.home) {
//			//NavUtils.navigateUpFromSameTask(this);
//			//NECESSITA QUE SEJA REPASSADO OS DADOS DO FEED QUE ESTAVA SENDO LIDO NA TELA ANTERIOR
//			//COM O ESQUEMA DE HOME O ANDROID DESTROI A TELA ANTERIOR E REMONTA COM OS DADOS ANTIGOS
//			//COM ON BACK PRESS ELE SIMPLEMENTE RETORNA PARA UMA TELA ANTERIOR QUE NÃO NECESSARIAMENE
//			//SERA A SUA
//			Intent intent = new Intent(this, ListarPostsActivity.class);
//			intent.putExtra("Feed", feedAux);
//			//intent.putExtra("Post", postAux); - resolver esse intent se um dia precisar resetar a posição do feed que foi lido na tela anterior
//			NavUtils.navigateUpTo(this, intent);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		postAux = postListAux.get(position);//redefine o feed repassado		

		Bundle args = new Bundle();
		args.putSerializable("Feed", feedAux);
		args.putSerializable("Post", postAux);
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.container,
				ExibirPostFragment.newInstance(position + 1, args)).commit();
//		getFragmentManager()
//				.beginTransaction()
//				.replace(R.id.container,
//						ExibirPostFragment.newInstance(position + 1)).commit();
		return true;
	}

}
