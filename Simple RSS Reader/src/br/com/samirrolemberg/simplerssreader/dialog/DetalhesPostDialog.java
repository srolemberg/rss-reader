package br.com.samirrolemberg.simplerssreader.dialog;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAODescricao;
import br.com.samirrolemberg.simplerssreader.model.Categoria;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.u.U;


public class DetalhesPostDialog {

	private Context context;
	private View view;
	private Post post;
	
	public DetalhesPostDialog(Context context, View view, Post post) {
		super();
		this.context = context;
		this.view = view;
		this.post = post;
	}
	//TODO: TRROCAROS LINKS DE DESCRIÇÃO PARA WEBVIEW
	//TODO: TROCAR OS TEXTOS PARAR LINS ACESSIVEIS.
	public View create(){
		TextView autor = (TextView) view.findViewById(R.id.autor__text_detalhes_dialog_post);
		LinearLayout autorLinear = (LinearLayout) view .findViewById(R.id.autor__linear_detalhes_dialog_post);

		TextView link = (TextView) view.findViewById(R.id.link__text_detalhes_dialog_post);
		LinearLayout linkLinear = (LinearLayout) view .findViewById(R.id.link__linear_detalhes_dialog_post);

		TextView data_publicacao = (TextView) view.findViewById(R.id.data_publicacao__text_detalhes_dialog_post);
		LinearLayout data_publicacaoLinear = (LinearLayout) view .findViewById(R.id.data_publicacao__linear_detalhes_dialog_post);
		
		TextView titulo = (TextView) view.findViewById(R.id.titulo__text_detalhes_dialog_post);
		LinearLayout tituloLinear = (LinearLayout) view .findViewById(R.id.titulo__linear_detalhes_dialog_post);

		TextView data_atualizacao = (TextView) view.findViewById(R.id.data_atualizacao__text_detalhes_dialog_post);
		LinearLayout data_atualizacaoLinear = (LinearLayout) view .findViewById(R.id.data_atualizacao__linear_detalhes_dialog_post);

		TextView link_URI = (TextView) view.findViewById(R.id.link_URI__text_detalhes_dialog_post);
		LinearLayout link_URILinear = (LinearLayout) view .findViewById(R.id.link_URI__linear_detalhes_dialog_post);

		TextView categoria = (TextView) view.findViewById(R.id.categorias__text_detalhes_dialog_post);
		LinearLayout categoriaLinear = (LinearLayout) view .findViewById(R.id.categorias__linear_detalhes_dialog_post);

		TextView descricao = (TextView) view.findViewById(R.id.descricao__text_detalhes_dialog_post);
		LinearLayout descricaoLinear = (LinearLayout) view .findViewById(R.id.descricao__linear_detalhes_dialog_post);

		//post.getAutor();
		if (post.getAutor()!=null && !post.getAutor().isEmpty()) {
			autor.setText(post.getAutor());
		}else{
			autorLinear.setVisibility(View.GONE);
		}

		DAOCategoria daoCategoria = new DAOCategoria(context);
		if (daoCategoria.size(post)>0) {
			List<Categoria> categorias = daoCategoria.listarTudo(post);
			StringBuffer str = new StringBuffer();
			for (Categoria cat : categorias) {
				str.append(cat.getNome());
				str.append(" | ");
			}
			categoria.setText(str);
		}else{
			categoriaLinear.setVisibility(View.GONE);
		}
		DatabaseManager.getInstance().closeDatabase();
		
		//post.getData_atualizacao();
		if (post.getData_atualizacao()!=null) {
			//data_atualizacao.setText(post.getData_atualizacao().toString());
			data_atualizacao.setText(U.date_time_24_mask(post.getData_atualizacao(), context));
		}else{
			data_atualizacaoLinear.setVisibility(View.GONE);
		}
		//post.getData_publicacao();
		if (post.getData_publicacao()!=null) {
			//data_publicacao.setText(post.getData_publicacao().toString());
			data_publicacao.setText(U.date_time_24_mask(post.getData_publicacao(), context));
		}else{
			data_publicacaoLinear.setVisibility(View.GONE);
		}
		//post.getDescricao();//fazer select
		DAODescricao daoDescricao = new DAODescricao(context);		
		if (daoDescricao.size(post)>0) {
			descricao.setText(daoDescricao.buscar(post).getValor());
		}else{
			descricaoLinear.setVisibility(View.GONE);
		}
		DatabaseManager.getInstance().closeDatabase();
		
		//post.getLink();
		if (post.getLink()!=null && !post.getLink().isEmpty()) {
			link.setText(post.getLink());
		}else{
			linkLinear.setVisibility(View.GONE);
		}
		//post.getLink_URI();
		if (post.getLink_URI()!=null && !post.getLink_URI().isEmpty()) {
			link_URI.setText(post.getLink_URI());
		}else{
			link_URILinear.setVisibility(View.GONE);
		}
		post.getTitulo();
		if (post.getTitulo()!=null && !post.getTitulo().isEmpty()) {
			titulo.setText(post.getTitulo());
		}else{
			tituloLinear.setVisibility(View.GONE);
		}
		
		return view;
	}
}
