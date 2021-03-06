package br.com.samirrolemberg.simplerssreader.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.ExibirPostActivity;
import br.com.samirrolemberg.simplerssreader.MainActivity;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.adapter.ListarPostsFragmentAdapter;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesPerguntaDialogFeed;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesPerguntaDialogPost;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesPostDialog;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.tasks.ExcluirPostTask;
import br.com.samirrolemberg.simplerssreader.u.Executando;
import br.com.samirrolemberg.simplerssreader.u.U;

public class ListarPostsFragment extends Fragment{

	
	public static final String ARG_LIST_POST = "list_post";

	//TODO: REMOVER FEED AUX? PODE SER DESNECESSARIO AQUI,A NÃO SER QUE SEJA POSSIVEL QUERER ACESSO AO FEED DIRETAMENTE
	private Feed feedAux;//feed selecionado na outra tela ou do spinner adapter
	private Post postAux; //post selecionado
	
	private List<Post> postsFrag;//lista de posts
	
	private ListView listView = null; // listview dentro do view
	
	private View view = null;// view que liga o fragment
	
	
	public ListarPostsFragment() {//obrigatório
	}

	private void carregar(View view){
		DAOPost daoPost = new DAOPost(getActivity());
		postsFrag = daoPost.listarTudo(feedAux);
		DatabaseManager.getInstance().closeDatabase();
		listView = (ListView) view.findViewById(R.id.list_post__exibir_post_fragment);
		ListarPostsFragmentAdapter adapter = new ListarPostsFragmentAdapter(postsFrag, listView);
		listView.setAdapter(adapter);

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		feedAux = (Feed) getArguments().get("Feed");
		DAOPost daoPost = new DAOPost(getActivity());
		postsFrag = daoPost.listarTudo(feedAux);
		DatabaseManager.getInstance().closeDatabase();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_listar_posts_list, container, false);
		
		//TextView textView = (TextView) view.findViewById(R.id.frase__exibir_posts_list_fragment);
		//textView.setText(textView.getText().toString()+" "+feedAux.getTitulo());
		
		//ListView listView = new ListView(getActivity());
//		listView = (ListView) view.findViewById(R.id.list_post__exibir_post_fragment);
//		ExibirPostsFragmentAdapter adapter = new ExibirPostsFragmentAdapter(postsFrag, listView);
//		listView.setAdapter(adapter);
		
		carregar(view);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
				postAux = (Post) adapter.getItemAtPosition(position);
				return false;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				postAux = (Post) adapter.getItemAtPosition(position);
				
				DAOConteudo daoConteudo = new DAOConteudo(getActivity());
				DAODescricao daoDescricao = new DAODescricao(getActivity());
				
				boolean temConteudo = daoConteudo.isExist(postAux);
				boolean temDescricao = daoDescricao.isExist(postAux);

				DatabaseManager.getInstance().closeDatabase();
				DatabaseManager.getInstance().closeDatabase();

				if (temConteudo || temDescricao) {
					Intent intent = new Intent(getActivity(), ExibirPostActivity.class);
					intent.putExtra("Post", postAux);
					intent.putExtra("Feed", feedAux);
					startActivity(intent);
				}else{
					Toast.makeText(getActivity(), "Este Post não contém conteúdo ou descrição prévia. Clique e segure o post por alguns segundos para mais opções.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		//registradores
		registerForContextMenu(listView);
		setHasOptionsMenu(true);
		return view;
	}
//	private Intent doShare(){
//	    Intent intent = new Intent(Intent.ACTION_SEND);
//	    intent.setType("text/plain");
//	    intent.putExtra(Intent.EXTRA_SUBJECT, feedAux.getDescricao());
//	    intent.putExtra(Intent.EXTRA_TEXT, feedAux.getLink());
//	    intent.putExtra(Intent.EXTRA_TITLE, feedAux.getTitulo());
//	    return intent;
//	}
	private Intent doSharePost(){
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    //intent.putExtra(Intent.EXTRA_SUBJECT, feedAux.getDescricao());
	    intent.putExtra(Intent.EXTRA_TEXT, postAux.getLink());
	    intent.putExtra(Intent.EXTRA_TITLE, postAux.getTitulo());
	    return intent;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		//Menu de contexto da lista de feeds
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.menu_lista_posts_fragment, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_contexto_lista_abrir_no_navegador:{
			if (U.isConnected(getActivity())) {
				Uri uri = Uri.parse(postAux.getLink());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);				
			}else{
				Toast.makeText(getActivity(), "Não há conexão de internet.", Toast.LENGTH_SHORT).show();					
			}			
		}
			break;
		case R.id.menu_contexto_lista_detalhes:{
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View detalhe = (new DetalhesPostDialog(getActivity(), inflater.inflate(R.layout.dialog_detalhes_post, null), postAux)).create();
			new AlertDialog.Builder(getActivity())
			.setView(detalhe)
			.setPositiveButton("Fechar", null)
			.show();			
		}
			break;
		case R.id.menu_contexto_lista_excluir:{
			LayoutInflater inflater = getActivity().getLayoutInflater();
			String frase = "Deseja remover o POST selecionado?";
			View excluir = (new DetalhesPerguntaDialogPost(getActivity(), inflater.inflate(R.layout.dialog_pergunta_feed, null), postAux, frase)).create();
			new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setView(excluir)
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!Executando.ATUALIZA_FEED.containsKey(feedAux.getIdFeed()+feedAux.getRss())) {
						//remove o feed do banco apenas
						DAOPost daoPost = new DAOPost(getActivity());
						daoPost.remover(postAux);
						DatabaseManager.getInstance().closeDatabase();
						carregar(view);//atualiza para o usuário
						//remove o restante do post em background
						ExcluirPostTask task = new ExcluirPostTask(getActivity(), postAux);
						String[] params = {""};
						task.execute(params);
//						Toast.makeText(getActivity(), "YES!!", Toast.LENGTH_SHORT).show();					
//						//TODO: ATUALIZAR LISTA DE REMOÇÃO CONFORME OS DADOS DE UM POST
//						DAOPost daoPost = new DAOPost(getActivity());
//						daoPost.remover(postAux);
//						daoPost.DatabaseManager.getInstance().closeDatabase();
//						DAODescricao daoDescricao = new DAODescricao(getActivity());
//						daoDescricao.remover(postAux);
//						daoDescricao.DatabaseManager.getInstance().closeDatabase();
//						carregar(view);
//						//TODO: TALVEZ SEJA NECESSÁRIO COLOCAR A REMOÇÃO EM BACKGROUND NA NOTIFICÇÃO
						
					}else{
						Toast.makeText(getActivity(), "Este post está atualizando. Aguarde alguns instantes.", Toast.LENGTH_SHORT).show();
					}
				}
			})
			.setNegativeButton("Não", null)
			.show();
		}
			break;
		case R.id.menu_contexto_lista_compartilhar:{
			if (U.isConnected(getActivity())) {
				Toast.makeText(getActivity(), item.getTitle().toString(), Toast.LENGTH_SHORT).show();
				startActivity(doSharePost());				
			}else{
				Toast.makeText(getActivity(), "Não há conexão de internet.", Toast.LENGTH_SHORT).show();
			}			
		}
			break;
		default:{
			Toast.makeText(getActivity(), "Dismiss", Toast.LENGTH_SHORT).show();			
		}
			break;
		}
		return super.onContextItemSelected(item);
	}
	

}
