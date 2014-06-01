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
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.adapter.ListarPostsFragmentAdapter;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.dialog.DetalhesPostDialog;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.model.Post;

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
		daoPost.close();
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
		daoPost.close();
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

				daoConteudo.close();
				daoDescricao.close();

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

		return view;
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
		case R.id.menu_contexto_abrir_no_navegador:
			//Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			Uri uri = Uri.parse(postAux.getLink());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;
		case R.id.menu_contexto_detalhes:
			//Toast.makeText(getActivity(), item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			Toast.makeText(getActivity(), item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View detalhe = (new DetalhesPostDialog(getActivity(), inflater.inflate(R.layout.dialog_detalhes_post, null), postAux)).create();
			new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Detalhes do Post")
			.setView(detalhe)
			.setPositiveButton("Fechar", null)
			.show();
			break;
		case R.id.menu_contexto_excluir:
			Toast.makeText(getActivity(), item.getTitle().toString(), Toast.LENGTH_SHORT).show();
			new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Remover")
			.setMessage("Deseja remover o POST selecionado?")
			.setPositiveButton("Sim", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "YES!!", Toast.LENGTH_SHORT).show();					
					//TODO: ATUALIZAR LISTA DE REMOÇÃO CONFORME OS DADOS DE UM POST
					DAOPost daoPost = new DAOPost(getActivity());
					daoPost.remover(postAux);
					daoPost.close();
					DAODescricao daoDescricao = new DAODescricao(getActivity());
					daoDescricao.remover(postAux);
					daoDescricao.close();
					carregar(view);
					//TODO: TALVEZ SEJA NECESSÁRIO COLOCAR A REMOÇÃO EM BACKGROUND NA NOTIFICÇÃO
				}
			})
			.setNegativeButton("Não", null)
			.show();
			break;
		default:
			Toast.makeText(getActivity(), "Dismiss", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}

}
