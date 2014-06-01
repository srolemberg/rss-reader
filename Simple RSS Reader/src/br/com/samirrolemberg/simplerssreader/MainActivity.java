package br.com.samirrolemberg.simplerssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.adapter.ListaFeedAdapter;
import br.com.samirrolemberg.simplerssreader.dao.DAOFeed;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.tasks.notification.ExcluirFeedTask;
import br.com.samirrolemberg.simplerssreader.tasks.notification.LimparConteudoFeedTask;
import br.com.samirrolemberg.simplerssreader.u.Executando;

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
				if (Executando.ADICIONAR_FEED.containsKey(feedAux.getIdFeed()+feedAux.getRss())) {
					//se contém ainda está executando a adição dos feeds
					Toast.makeText(MainActivity.this, "Este feed ainda não está pronto. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();					
					return true;
				}else{//retorna falso se está apto para exibir o menu
					return false;					
				}

			}
		});
		
		listaFeeds.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				feedAux = (Feed) adapter.getItemAtPosition(position);
				if (Executando.ADICIONAR_FEED.containsKey(feedAux.getIdFeed()+feedAux.getRss())) {
					//se contém ainda está executando a adição dos feeds
					Toast.makeText(MainActivity.this, "Este feed ainda não está pronto. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();					
				}else{
					Intent intent = new Intent(MainActivity.this, ListarPostsActivity.class);
					intent.putExtra("Feed", feedAux);
					startActivity(intent);					
				}
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
			LayoutInflater inflater = this.getLayoutInflater();
			View view = (new DetalhesFeedDialog(MainActivity.this, inflater.inflate(R.layout.dialog_detalhes_feed, null), feedAux)).create();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Detalhes do Feed")
			.setView(view)
			.setPositiveButton("Fechar", null)
			.show();
			break;
		case R.id.menu_contexto_limpar_conteudo:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(feedAux.getTitulo())
			.setMessage("Você removerá todo o conteúdo adicionado para este Feed anteriormente. Deseja continuar?")
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "YES!!", Toast.LENGTH_SHORT).show();		
					
					LimparConteudoFeedTask task = new LimparConteudoFeedTask(MainActivity.this, feedAux);
					String[] params = {""};
					task.execute(params);

				}
			})
			.setNegativeButton("Não", null)
			.show();
			break;
		case R.id.menu_contexto_excluir:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(feedAux.getTitulo())
			.setMessage("Deseja remover o FEED selecionado?")
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "YES!!", Toast.LENGTH_SHORT).show();		
					//TODO: ATUALIZAR LISTA DE REMOÇÃO CONFORME OS DADOS DE UM FEED
					//excluir(MainActivity.this, feedAux);
					
					//remove o feed do banco apenas
					DAOFeed daoFeed = new DAOFeed(MainActivity.this);
					daoFeed.remover(feedAux);
					daoFeed.close();
					carregar();//atualiza para o usuário
					//remove o restante do feed em background
					ExcluirFeedTask task = new ExcluirFeedTask(MainActivity.this, feedAux);
					String[] params = {""};
					task.execute(params);
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
	
//	private void excluir(Context context, Feed feed){
//		DAOFeed daoFeed = new DAOFeed(context);
//		DAOPost daoPost = new DAOPost(context);
//		DAODescricao daoDescricao = new DAODescricao(context);
//		DAOImagem daoImagem = new DAOImagem(context);
//		DAOAnexo daoAnexo = new DAOAnexo(context);
//		DAOCategoria daoCategoria = new DAOCategoria(context);
//		DAOConteudo daoConteudo = new DAOConteudo(context);
//		
//		List<Post> posts = daoPost.listarTudo(feed);
//		
//		daoFeed.remover(feed);
//		daoCategoria.remover(feed);
//		daoImagem.remover(feed);
//		for (Post post : posts) {
//			daoPost.remover(post);
//			daoDescricao.remover(post);
//			daoAnexo.remover(post);
//			daoCategoria.remover(post);
//			daoConteudo.remover(post);
//		}
//		
//		daoFeed.close();
//		daoPost.close();
//		daoDescricao.close();
//		daoImagem.close();
//		daoAnexo.close();
//		daoCategoria.close();
//		daoConteudo.close();
//		
//	}
}
