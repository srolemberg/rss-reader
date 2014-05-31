package br.com.samirrolemberg.simplerssreader.model;


public class Categoria {

	private final long idCategoria;
	private final String nome;
	private final String url;
	private final Feed feed;
	private final Post post;
	
	public static class Builder{
		
		private long idCategoria;
		private String nome;
		private String url;
		private Feed feed;
		private Post post;
		
		public Categoria build(){
			return new Categoria(this);
		}
		
		public Builder idCategoria(long idCategoria){this.idCategoria = idCategoria; return this;}
		public Builder nome(String nome){this.nome = nome; return this;}
		public Builder url(String url){this.url = url; return this;}
		public Builder feed(Feed feed){this.feed = feed; return this;}
		public Builder post(Post post){this.post = post; return this;}

	}
	
	private Categoria(Builder builder){
		super();
		idCategoria = builder.idCategoria;
		nome = builder.nome;
		url = builder.url;
		feed = builder.feed;
		post = builder.post;
	}

	public long getIdCategoria() {
		return idCategoria;
	}

	public String getNome() {
		return nome;
	}

	public String getUrl() {
		return url;
	}

	public Feed getFeed() {
		return feed;
	}

	public Post getPost() {
		return post;
	}

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Categoria [idCategoria=");
		builder2.append(idCategoria);
		builder2.append(", nome=");
		builder2.append(nome);
		builder2.append(", url=");
		builder2.append(url);
		builder2.append(", feed=");
		builder2.append(feed);
		builder2.append(", post=");
		builder2.append(post);
		builder2.append("]");
		return builder2.toString();
	}
	
}
