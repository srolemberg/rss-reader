package br.com.samirrolemberg.simplerssreader.fragment;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOConteudo;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.model.Conteudo;
import br.com.samirrolemberg.simplerssreader.model.Descricao;
import br.com.samirrolemberg.simplerssreader.model.Post;

public class ExibirPostFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_POST_CONTENT= "post_content";

	//private Feed feedAux = null;
	private Post postAux = null;

	private List<Conteudo> conteudosFrag = null;
	private List<Descricao> descricoesFrag = null;
	
	private DAOConteudo daoConteudo = null;
	private DAODescricao daoDescricao = null;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ExibirPostFragment newInstance(int sectionNumber, Bundle args) {
		ExibirPostFragment fragment = new ExibirPostFragment();
		//Bundle args = new Bundle();
		args.putInt(ARG_POST_CONTENT, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ExibirPostFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//feedAux = (Feed) getArguments().get("Feed");
		postAux = (Post) getArguments().get("Post");
		
		daoConteudo = new DAOConteudo(getActivity());
		daoDescricao = new DAODescricao(getActivity());
		
		conteudosFrag = daoConteudo.listarTudo(postAux);
		descricoesFrag = daoDescricao.listarTudo(postAux);
		
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_exibir_post, container, false);
		WebView navegador = (WebView) rootView.findViewById(R.id.navegador__exibir_post_fragment);
		
		daoConteudo = new DAOConteudo(getActivity());
		daoDescricao = new DAODescricao(getActivity());
		
		if (daoConteudo.size(postAux)>0) {//se o conteudo total não é nulo
			String data = conteudosFrag.get(0).getValor();
			String mimeType = conteudosFrag.get(0).getTipo();
			navegador.getSettings().setBlockNetworkLoads(true);
			//navegador.getSettings().setJavaScriptEnabled(true);
			//navegador.loadData(data, "text/"+mimeType+"; charset=UTF-8",null);
			navegador.loadData(data, "text/"+mimeType+"; charset=UTF-8",null);
			//navegador.setText(Integer.toString(getArguments().getInt(ARG_POST_CONTENT)));
			
		}else if (daoDescricao.size(postAux)>0){//se a não há conteudo mas há descrição
			String data = descricoesFrag.get(0).getValor();
			String mimeType = descricoesFrag.get(0).getTipo();
			navegador.getSettings().setBlockNetworkLoads(true);
			//navegador.getSettings().setJavaScriptEnabled(true);
			//navegador.loadData(data, "text/"+mimeType+"; charset=UTF-8",null);
			navegador.loadData(data, mimeType+"; charset=UTF-8",null);
			//navegador.setText(Integer.toString(getArguments().getInt(ARG_POST_CONTENT)));

		}
		DatabaseManager.getInstance().closeDatabase();
		DatabaseManager.getInstance().closeDatabase();
		
		setHasOptionsMenu(true);
		return rootView;
	}

}
