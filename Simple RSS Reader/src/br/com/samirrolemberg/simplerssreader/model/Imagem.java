package br.com.samirrolemberg.simplerssreader.model;



public class Imagem {

	private final long idImagem;
	private final String descricao;
	private final String link;
	private final String titulo;
	private final String url;
	private final Feed feed;

	public static class Builder{
		private long idImagem;
		private String descricao;
		private String link;
		private String titulo;
		private String url;
		private Feed feed;

		public Imagem build(){
			return new Imagem(this);
		}
		public Builder idImagem(long idImagem){this.idImagem = idImagem; return this;}
		public Builder descricao(String descricao){this.descricao = descricao; return this;}
		public Builder link(String link){this.link = link; return this;}
		public Builder titulo(String titulo){this.titulo = titulo; return this;}
		public Builder url(String url){this.url = url; return this;}
		public Builder feed(Feed feed){this.feed = feed; return this;}

	}
	private Imagem(Builder builder){
		super();
		idImagem = builder.idImagem;
		descricao = builder.descricao;
		link = builder.link;
		titulo = builder.titulo;
		url = builder.url;
		feed = builder.feed;
	}
	public long getIdImagem() {
		return idImagem;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getLink() {
		return link;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getUrl() {
		return url;
	}
	public Feed getFeed() {
		return feed;
	}
	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Imagem [idImagem=");
		builder2.append(idImagem);
		builder2.append(", descricao=");
		builder2.append(descricao);
		builder2.append(", link=");
		builder2.append(link);
		builder2.append(", titulo=");
		builder2.append(titulo);
		builder2.append(", url=");
		builder2.append(url);
		builder2.append(", feed=");
		builder2.append(feed);
		builder2.append("]");
		return builder2.toString();
	}
	
}
