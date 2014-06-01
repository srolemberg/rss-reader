package br.com.samirrolemberg.simplerssreader;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.dao.DAOPost;
import br.com.samirrolemberg.simplerssreader.model.Feed;


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
		TextView autor = (TextView) view.findViewById(R.id.autor__text_detalhes_dialog);
		LinearLayout autorLinear = (LinearLayout) view .findViewById(R.id.autor__linear_detalhes_dialog);
		
		TextView direitoAutoral = (TextView) view.findViewById(R.id.direito_autoral__text_detalhes_dialog);
		LinearLayout direitoAutoralLinear = (LinearLayout) view .findViewById(R.id.direito_autoral__linear_detalhes_dialog);
		
		TextView descricao = (TextView) view.findViewById(R.id.descricao__text_detalhes_dialog);
		LinearLayout descricaoLinear = (LinearLayout) view .findViewById(R.id.descricao__linear_detalhes_dialog);

		TextView codificacao = (TextView) view.findViewById(R.id.codificacao__text_detalhes_dialog);
		LinearLayout codificacaoLinear = (LinearLayout) view .findViewById(R.id.codificacao__linear_detalhes_dialog);
		
		TextView tipoFeed = (TextView) view.findViewById(R.id.tipo_feed__text_detalhes_dialog);
		LinearLayout tipoFeedLinear = (LinearLayout) view .findViewById(R.id.tipo_feed__linear_detalhes_dialog);
		
		TextView idioma = (TextView) view.findViewById(R.id.idioma__text_detalhes_dialog);
		LinearLayout idiomaLinear = (LinearLayout) view .findViewById(R.id.idioma__linear_detalhes_dialog);
		
		TextView link = (TextView) view.findViewById(R.id.link__text_detalhes_dialog);
		LinearLayout linkLinear = (LinearLayout) view .findViewById(R.id.link__linear_detalhes_dialog);
		
		TextView titulo = (TextView) view.findViewById(R.id.titulo__text_detalhes_dialog);
		LinearLayout tituloLinear = (LinearLayout) view .findViewById(R.id.titulo__linear_detalhes_dialog);
		
		TextView uri = (TextView) view.findViewById(R.id.uri__text_detalhes_dialog);
		LinearLayout uriLinear = (LinearLayout) view .findViewById(R.id.uri__linear_detalhes_dialog);
		
		TextView data_cadastro = (TextView) view.findViewById(R.id.data_cadastro__text_detalhes_dialog);
		LinearLayout data_cadastroLinear = (LinearLayout) view .findViewById(R.id.data_cadastro__linear_detalhes_dialog);
		
		TextView data_sincronizacao = (TextView) view.findViewById(R.id.data_sincronizacao__text_detalhes_dialog);
		LinearLayout data_sincronizacaoLinear = (LinearLayout) view .findViewById(R.id.data_sincronizacao__linear_detalhes_dialog);
		
		TextView rss = (TextView) view.findViewById(R.id.rss__text_detalhes_dialog);
		LinearLayout rssLinear = (LinearLayout) view .findViewById(R.id.rss__linear_detalhes_dialog);
		
		//TextView acesso = (TextView) view.findViewById(R.id.acesso__text_detalhes_dialog);
		//LinearLayout acessoLinear = (LinearLayout) view .findViewById(R.id.acesso__linear_detalhes_dialog);
		
		TextView registro = (TextView) view.findViewById(R.id.registros__text_detalhes_dialog);
		//LinearLayout registroLinear = (LinearLayout) view .findViewById(R.id.registros__linear_detalhes_dialog);

		TextView data_publicacao = (TextView) view.findViewById(R.id.data_publicacao__text_detalhes_dialog);
		LinearLayout data_publicacaoLinear = (LinearLayout) view .findViewById(R.id.data_publicacao__linear_detalhes_dialog);

		TextView categoria = (TextView) view.findViewById(R.id.categoria__text_detalhes_dialog);
		LinearLayout categoriaLinear = (LinearLayout) view .findViewById(R.id.categoria__linear_detalhes_dialog);

		//feed.getAutor();
		if (feed.getAutor()!=null && !feed.getAutor().isEmpty()) {
			autor.setText(feed.getAutor());
		}else{
			autorLinear.setVisibility(View.GONE);
		}
		//feed.getCategorias();//adicionar no layout
		if (feed.getCategorias()!=null && feed.getCategorias().size()>0) {
			categoria.setText(feed.getCategorias().toString());
		}else{
			categoriaLinear.setVisibility(View.GONE);
		}
		//feed.getCodificacao();
		if (feed.getCodificacao()!=null && !feed.getCodificacao().isEmpty()) {
			codificacao.setText(feed.getCodificacao());
		}else{
			codificacaoLinear.setVisibility(View.GONE);
		}
		//feed.getData_cadastro();
		if (feed.getData_cadastro()!=null) {
			data_cadastro.setText(feed.getData_cadastro().toString());
		}else{
			data_cadastroLinear.setVisibility(View.GONE);
		}
		//feed.getData_publicacao();//colocar no layout
		if (feed.getData_publicacao()!=null) {
			data_publicacao.setText(feed.getData_publicacao().toString());
		}else{
			data_publicacaoLinear.setVisibility(View.GONE);
		}
		//feed.getData_sincronizacao();
		if (feed.getData_sincronizacao()!=null) {
			data_sincronizacao.setText(feed.getData_sincronizacao().toString());
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
