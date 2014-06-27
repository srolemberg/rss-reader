package br.com.samirrolemberg.simplerssreader;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOFeed;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesFeedDialog;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesSobreDialog;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.services.AtualizarFeedsService;
import br.com.samirrolemberg.simplerssreader.services.ExcluirFeedService;
import br.com.samirrolemberg.simplerssreader.services.LimparConteudoService;
import br.com.samirrolemberg.simplerssreader.tasks.AtualizarFeedTask;
import br.com.samirrolemberg.simplerssreader.u.Executando;
import br.com.samirrolemberg.simplerssreader.u.U;

import com.bugsense.trace.BugSenseHandler;

public class MainActivity extends Activity {

	private ListView listaFeeds = null;
	private Feed feedAux = null;
	private ListaFeedAdapter adapter;
	
	private void carregar(){
		DAOFeed daoFeed = new DAOFeed(MainActivity.this);
		
		adapter = new ListaFeedAdapter(daoFeed.listarTudo(), this);			
		DatabaseManager.getInstance().closeDatabase();
		
		listaFeeds = (ListView) findViewById(R.id.lista_feeds);
		listaFeeds.setAdapter(adapter);

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		carregar();
	}
	@Override
	protected void onStart() {
		super.onStart();
		//new DAO(MainActivity.this);
		//TODO:BUGSENSE - REMOVER DEPOIS?
		BugSenseHandler.startSession(MainActivity.this);
	}
	@Override
	protected void onStop() {
		super.onStart();
		//TODO:BUGSENSE - REMOVER DEPOIS?
		BugSenseHandler.closeSession(MainActivity.this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//new DAO(MainActivity.this);
		//TODO NEW DAO REF.
		//a aplicação pode ter destruido oa referência da Databasemanager.
		//Inicia no OnCreate ou nos OnRestart. (Otimizar para controlar nulo no futuro)
		
		//TODO:BUGSENSE - REMOVER DEPOIS?
	    BugSenseHandler.initAndStartSession(this, getString(R.string.bugsense__api_key));
		
		setContentView(R.layout.activity_main);
		
		//TODO: VER SE SERÁ NECESSÁRIO COLOCAR EM TODAS AS ACTIVITYS
		//new DAO(MainActivity.this);//para inicializar a instancia do banco caso não haja. 
		//CRIA OU ATUALIZA O BANCO TAMBÉM
		
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
		case R.id.menu_contexto_feeds_abrir_no_navegador:
			//Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			Uri uri = Uri.parse(feedAux.getLink());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case R.id.menu_contexto_feeds_atualizar_feed:
			//Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			if (!Executando.ATUALIZA_FEED.containsKey(feedAux.getIdFeed()+feedAux.getRss())) {
				//se o mesmo feed já está atualizando está atualizando o feed
				if (U.isConnected(MainActivity.this)) {
					//se tem conexão de internet
					AtualizarFeedTask task = new AtualizarFeedTask(MainActivity.this, feedAux);
					String[] params = {feedAux.getRss().toString()};
					task.execute(params);
				}else{
					Toast.makeText(MainActivity.this, "Não há conexão de internet.", Toast.LENGTH_SHORT).show();					
				}
			}else{
				Toast.makeText(MainActivity.this, "Este feed está atualizando. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_contexto_feeds_detalhes:
			//Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			LayoutInflater inflater = this.getLayoutInflater();
			View detalhe = (new DetalhesFeedDialog(MainActivity.this, inflater.inflate(R.layout.dialog_detalhes_feed, null), feedAux)).create();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Detalhes do Feed")
			.setView(detalhe)
			.setPositiveButton("Fechar", null)
			.show();
			break;
		case R.id.menu_contexto_feeds_limpar_conteudo:
			//Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(feedAux.getTitulo())
			.setMessage("Você removerá todo o conteúdo adicionado para este Feed anteriormente. Deseja continuar?")
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!U.isMyServiceRunning(LimparConteudoService.class, MainActivity.this)) {//se o serviço não está rodando...
						Intent intent = new Intent(MainActivity.this, LimparConteudoService.class);
						intent.putExtra("Feed", feedAux);
						startService(intent);
					}else{
						Toast.makeText(MainActivity.this, "Este feed está atualizando. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();
					}

//					if (!Executando.ATUALIZA_FEED.containsKey(feedAux.getIdFeed()+feedAux.getRss())) {
//						Intent intent = new Intent(MainActivity.this, LimparConteudoService.class);
//						intent.putExtra("Feed", feedAux);
//						//service.startService(intent);
//						startService(intent);
////						LimparConteudoFeedTask task = new LimparConteudoFeedTask(MainActivity.this, feedAux);
////						String[] params = {""};
////						task.execute(params);
//						//carregar();//vai dar problema com muitos feeds.						
//					}else{
//						Toast.makeText(MainActivity.this, "Este feed está atualizando. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();
//					}
				}
			})
			.setNegativeButton("Não", null)
			.show();
			break;
		case R.id.menu_contexto_feeds_excluir:
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(feedAux.getTitulo())
			.setMessage("Deseja remover o FEED selecionado?")
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!Executando.ATUALIZA_FEED.containsKey(feedAux.getIdFeed()+feedAux.getRss())) {
						//TODO: ATUALIZAR LISTA DE REMOÇÃO CONFORME OS DADOS DE UM FEED
						//excluir(MainActivity.this, feedAux);
						
						//remove o feed do banco apenas
						DAOFeed daoFeed = new DAOFeed(MainActivity.this);
						daoFeed.remover(feedAux);
						DatabaseManager.getInstance().closeDatabase();
						carregar();//atualiza para o usuário
						
						//remove o restante no serviço
						Intent intent = new Intent(MainActivity.this, ExcluirFeedService.class);
						intent.putExtra("Feed", feedAux);
						startService(intent);

//						//remove o restante do feed em background
//						ExcluirFeedTask task = new ExcluirFeedTask(MainActivity.this, feedAux);
//						String[] params = {""};
//						task.execute(params);						
					}else{
						Toast.makeText(MainActivity.this, "Este feed está atualizando. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();
					}
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
		case R.id.action_adicionar_feed:{			
			Intent intent = new Intent(MainActivity.this, AdicionarFeedActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.action_atualizar_lista_feed:{
			DAOFeed daoFeed = new DAOFeed(MainActivity.this);
			ArrayList<Feed> feeds = (ArrayList<Feed>) daoFeed.listarTudo();
			//AtualizarTudoService service = new AtualizarTudoService(MainActivity.this);
			Log.w("MY-SERVICES", "Feeds: "+feeds.size());
			Intent intent = new Intent(MainActivity.this, AtualizarFeedsService.class);
			intent.putExtra("Feeds", feeds);
			//service.startService(intent);
			DatabaseManager.getInstance().closeDatabase();
			startService(intent);
		}
			break;
		case R.id.action_configurar:{
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();			
		}
			break;
		case R.id.action_ajuda:{
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MainActivity.this, AjudaActivity.class);
			startActivity(intent);			
		}
			break;
		case R.id.action_sobre:{
			Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			LayoutInflater inflater = this.getLayoutInflater();
			View detalhe = (new DetalhesSobreDialog(MainActivity.this, inflater.inflate(R.layout.dialog_sobre, null))).create();
			new AlertDialog.Builder(MainActivity.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Sobre")
			.setView(detalhe)
			.setPositiveButton("Fechar", null)
			.show();	
		}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
