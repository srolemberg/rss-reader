package br.com.samirrolemberg.simplerssreader.dialog;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOCategoria;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Categoria;
import br.com.samirrolemberg.simplerssreader.model.Feed;
import br.com.samirrolemberg.simplerssreader.u.U;


public class DetalhesFeedDialog {

	private Context context;
	private View view;
	private Feed feed;
	
	public DetalhesFeedDialog(Context context, View view, Feed feed) {
		super();
		this.context = context;
		this.view = view;
		this.feed = feed;
	}
	
	public View create(){
		TextView tituloDialogo = (TextView) ((view.findViewById(R.id.dialog_titulo_customizado)).findViewById(R.id.texto_titulo_dialogo_customizado));
		tituloDialogo.setText("Detalhes do Feed");
		
		TextView autor = (TextView) view.findViewById(R.id.autor__text_detalhes_dialog_feed);
		LinearLayout autorLinear = (LinearLayout) view .findViewById(R.id.autor__linear_detalhes_dialog_feed);
		
		TextView direitoAutoral = (TextView) view.findViewById(R.id.direito_autoral__text_detalhes_dialog_feed);
		LinearLayout direitoAutoralLinear = (LinearLayout) view .findViewById(R.id.direito_autoral__linear_detalhes_dialog_feed);
		
		TextView descricao = (TextView) view.findViewById(R.id.descricao__text_detalhes_dialog_feed);
		LinearLayout descricaoLinear = (LinearLayout) view .findViewById(R.id.descricao__linear_detalhes_dialog_feed);

		TextView codificacao = (TextView) view.findViewById(R.id.codificacao__text_detalhes_dialog_feed);
		LinearLayout codificacaoLinear = (LinearLayout) view .findViewById(R.id.codificacao__linear_detalhes_dialog_feed);
		
		TextView tipoFeed = (TextView) view.findViewById(R.id.tipo_feed__text_detalhes_dialog_feed);
		LinearLayout tipoFeedLinear = (LinearLayout) view .findViewById(R.id.tipo_feed__linear_detalhes_dialog_feed);
		
		TextView idioma = (TextView) view.findViewById(R.id.idioma__text_detalhes_dialog_feed);
		LinearLayout idiomaLinear = (LinearLayout) view .findViewById(R.id.idioma__linear_detalhes_dialog_feed);
		
		TextView link = (TextView) view.findViewById(R.id.link__text_detalhes_dialog_feed);
		LinearLayout linkLinear = (LinearLayout) view .findViewById(R.id.link__linear_detalhes_dialog_feed);
		
		TextView titulo = (TextView) view.findViewById(R.id.titulo__text_detalhes_dialog_feed);
		LinearLayout tituloLinear = (LinearLayout) view .findViewById(R.id.titulo__linear_detalhes_dialog_feed);
		
		TextView uri = (TextView) view.findViewById(R.id.uri__text_detalhes_dialog_feed);
		LinearLayout uriLinear = (LinearLayout) view .findViewById(R.id.uri__linear_detalhes_dialog_feed);
		
		TextView data_cadastro = (TextView) view.findViewById(R.id.data_cadastro__text_detalhes_dialog_feed);
		LinearLayout data_cadastroLinear = (LinearLayout) view .findViewById(R.id.data_cadastro__linear_detalhes_dialog_feed);
		
		TextView data_sincronizacao = (TextView) view.findViewById(R.id.data_sincronizacao__text_detalhes_dialog_feed);
		LinearLayout data_sincronizacaoLinear = (LinearLayout) view .findViewById(R.id.data_sincronizacao__linear_detalhes_dialog_feed);
		
		TextView rss = (TextView) view.findViewById(R.id.rss__text_detalhes_dialog_feed);
		LinearLayout rssLinear = (LinearLayout) view .findViewById(R.id.rss__linear_detalhes_dialog_feed);
		
		//TextView acesso = (TextView) view.findViewById(R.id.acesso__text_detalhes_dialog_feed);
		//LinearLayout acessoLinear = (LinearLayout) view .findViewById(R.id.acesso__linear_detalhes_dialog_feed);
		
		TextView registro = (TextView) view.findViewById(R.id.registros__text_detalhes_dialog_feed);
		//LinearLayout registroLinear = (LinearLayout) view .findViewById(R.id.registros__linear_detalhes_dialog_feed);

		TextView data_publicacao = (TextView) view.findViewById(R.id.data_publicacao__text_detalhes_dialog_feed);
		LinearLayout data_publicacaoLinear = (LinearLayout) view .findViewById(R.id.data_publicacao__linear_detalhes_dialog_feed);

		TextView categoria = (TextView) view.findViewById(R.id.categoria__text_detalhes_dialog_feed);
		LinearLayout categoriaLinear = (LinearLayout) view .findViewById(R.id.categoria__linear_detalhes_dialog_feed);

		//feed.getAutor();
		//TODO: FAZER OS SELECTS DOS ATRIBUTOS QUE TEM LISTA
		if (feed.getAutor()!=null && !feed.getAutor().isEmpty()) {
			autor.setText(feed.getAutor());
		}else{
			autorLinear.setVisibility(View.GONE);
		}
		//feed.getCategorias();//adicionar no layout
		DAOCategoria daoCategoria = new DAOCategoria(context);
		List<Categoria> categorias = daoCategoria.listarTudo(feed);
		if (daoCategoria.size(feed)>0) {
			StringBuffer str = new StringBuffer();
			for (Categoria cat : categorias) {
				str.append(cat.getNome());
				str.append(" ");
			}
			categoria.setText(str);
		}else{
			categoriaLinear.setVisibility(View.GONE);
		}
		DatabaseManager.getInstance().closeDatabase();
		//feed.getCodificacao();
		if (feed.getCodificacao()!=null && !feed.getCodificacao().isEmpty()) {
			codificacao.setText(feed.getCodificacao());
		}else{
			codificacaoLinear.setVisibility(View.GONE);
		}
		//feed.getData_cadastro();
		if (feed.getData_cadastro()!=null) {
			//data_cadastro.setText(feed.getData_cadastro().toString());
			data_cadastro.setText(U.date_time_24_mask(feed.getData_cadastro(), context));
		}else{
			data_cadastroLinear.setVisibility(View.GONE);
		}
		//feed.getData_publicacao();//colocar no layout
		if (feed.getData_publicacao()!=null) {
			//data_publicacao.setText(feed.getData_publicacao().toString());
			data_publicacao.setText(U.date_time_24_mask(feed.getData_publicacao(), context));
		}else{
			data_publicacaoLinear.setVisibility(View.GONE);
		}
		//feed.getData_sincronizacao();
		if (feed.getData_sincronizacao()!=null) {
			//data_sincronizacao.setText(feed.getData_sincronizacao().toString());
			data_sincronizacao.setText(U.date_time_24_mask(feed.getData_sincronizacao(), context));
		}else{
			data_sincronizacaoLinear.setVisibility(View.GONE);
		}
		//feed.getDescricao();
		if (feed.getDescricao()!=null && !feed.getDescricao().isEmpty()) {
			descricao.setText(feed.getDescricao());
		}else{
			descricaoLinear.setVisibility(View.GONE);
		}
		//feed.getDireitoAutoral();
		if (feed.getDireitoAutoral()!=null && !feed.getDireitoAutoral().isEmpty()) {
			direitoAutoral.setText(feed.getDireitoAutoral());
		}else{
			direitoAutoralLinear.setVisibility(View.GONE);
		}
		//feed.getIdioma();
		if (feed.getIdioma()!=null && !feed.getIdioma().isEmpty()) {
			idioma.setText(feed.getIdioma());
		}else{
			idiomaLinear.setVisibility(View.GONE);
		}
		//feed.getLink();
		if (feed.getLink()!=null && !feed.getLink().isEmpty()) {
			link.setText(feed.getLink());
		}else{
			linkLinear.setVisibility(View.GONE);
		}
		//feed.getPosts();//fazer select
		DAOPost daoPost = new DAOPost(context);
		long size = daoPost.size(feed);
		registro.setText(""+size);
		DatabaseManager.getInstance().closeDatabase();
//		if (feed.getPosts()!=null && !feed.getAutor().isEmpty()) {
//			autor.setText(feed.getAutor());
//		}else{
//			autorLinear.setVisibility(View.GONE);
//		}
		//feed.getRss();
		if (feed.getRss()!=null && !feed.getRss().isEmpty()) {
			rss.setText(feed.getRss());
		}else{
			rssLinear.setVisibility(View.GONE);
		}
		//feed.getTipoFeed();
		if (feed.getTipoFeed()!=null && !feed.getTipoFeed().isEmpty()) {
			tipoFeed.setText(feed.getTipoFeed());
		}else{
			tipoFeedLinear.setVisibility(View.GONE);
		}
		//feed.getTitulo();
		if (feed.getTitulo()!=null && !feed.getTitulo().isEmpty()) {
			titulo.setText(feed.getTitulo());
		}else{
			tituloLinear.setVisibility(View.GONE);
		}
		//feed.getUri();
		if (feed.getUri()!=null && !feed.getUri().isEmpty()) {
			uri.setText(feed.getUri());
		}else{
			uriLinear.setVisibility(View.GONE);
		}
		
		
		return view;
	}
}
