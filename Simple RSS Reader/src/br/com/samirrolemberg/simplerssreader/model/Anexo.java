package br.com.samirrolemberg.simplerssreader.model;



public class Anexo {

	private final long idAnexo;
	private final long tamanho;
	private final String tipo;
	private final String url;
	private final Post post;
	
	public static class Builder{
		private long idAnexo;
		private long tamanho;
		private String tipo;
		private String url;
		private Post post;
		
		public Anexo build(){
			return new Anexo(this);
		}

		public Builder idAnexo(long idAnexo){this.idAnexo = idAnexo; return this;}
		public Builder tamanho(long tamanho){this.tamanho = tamanho; return this;}
		public Builder tipo(String tipo){this.tipo = tipo; return this;}
		public Builder url(String url){this.url = url; return this;}
		public Builder post(Post post){this.post = post; return this;}

	}
	private Anexo(Builder builder){
		super();
		idAnexo = builder.idAnexo;
		tamanho = builder.tamanho;
		tipo = builder.tipo;
		url = builder.url;
		post = builder.post;
	}
	
	public long getIdAnexo() {
		return idAnexo;
	}
	public long getTamanho() {
		return tamanho;
	}
	public String getTipo() {
		return tipo;
	}
	public String getUrl() {
		return url;
	}
	public Post getPost() {
		return post;
	}
	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Anexo [idAnexo=");
		builder2.append(idAnexo);
		builder2.append(", tamanho=");
		builder2.append(tamanho);
		builder2.append(", tipo=");
		builder2.append(tipo);
		builder2.append(", url=");
		builder2.append(url);
		builder2.append(", post=");
		builder2.append(post);
		builder2.append("]");
		return builder2.toString();
	}

}
