package br.com.samirrolemberg.simplerssreader;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.adapter.ListaFeedAdapter;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOFeed;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class MainActivity extends Activity {

	private ListView listaFeeds = null;
	private Feed feedAux = null;
	private ListaFeedAdapter adapter;
	
	private void carregar(){
		DAOFeed daoFeed = new DAOFeed(MainActivity.this);
		
		adapter = new ListaFeedAdapter(daoFeed.listarTudo(), this);			
		daoFeed.close();
		
		listaFeeds = (ListView) findViewById(R.id.lista_feeds);
		listaFeeds.setAdapter(adapter);

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		carregar();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		carregar();

		listaFeeds.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
				feedAux = (Feed) adapter.getItemAtPosition(position);
				return false;
			}
		});
		
		listaFeeds.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				feedAux = (Feed) adapter.getItemAtPosition(position);
				
				Intent intent = new Intent(MainActivity.this, ListarPostsActivity.class);
				intent.putExtra("Feed", feedAux);
				startActivity(intent);

			}
		});
		
		//registradores
		registerForContextMenu(listaFeeds);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		//Menu de contexto da lista de feeds
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu_lista_feeds_principal, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_contexto_abrir_no_navegador:
			//Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			Uri uri = Uri.parse(feedAux.getLink());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case R.id.menu_contexto_atualizar_feed:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_contexto_detalhes:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_contexto_limpar_conteudo:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_contexto_excluir:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Remover")
			.setMessage("Deseja remover o FEED selecionado?")
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "YES!!", Toast.LENGTH_SHORT).show();		
					//TODO: ATUALIZAR LISTA DE REMOÇÃO CONFORME OS DADOS DE UM FEED
					DAOFeed daoFeed = new DAOFeed(MainActivity.this);
					daoFeed.remover(feedAux);
					daoFeed.close();
					DAOPost daoPost = new DAOPost(MainActivity.this);
					List<Post> posts = daoPost.listarTudo(feedAux);
					DAODescricao daoDescricao = new DAODescricao(MainActivity.this);
					for (Post post : posts) {
						daoPost.remover(post);
						daoDescricao.remover(post);
					}
					daoPost.close();
					daoDescricao.close();
					daoFeed.close();
					carregar();
				}
			})
			.setNegativeButton("Não", null)
			.show();
			break;
		default:
			Toast.makeText(MainActivity.this, "Dismiss", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_bar_principal, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_adicionar_feed:
			Intent intent = new Intent(MainActivity.this, AdicionarFeedActivity.class);
			startActivity(intent);
			break;
		case R.id.action_atualizar_lista_feed:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_configurar:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_ajuda:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_sobre:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
