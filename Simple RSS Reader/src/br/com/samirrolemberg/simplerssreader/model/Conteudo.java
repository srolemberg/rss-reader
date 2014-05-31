package br.com.samirrolemberg.simplerssreader.model;


public class Conteudo {

	private final long idConteudo;
	private final String modo;
	private final String tipo;
	private final String valor;
	private final Post post;

	public static class Builder{
		private long idConteudo;
		private String modo;
		private String tipo;
		private String valor;
		private Post post;
		
		public Conteudo build(){
			return new Conteudo(this);
		}

		public Builder idConteudo(long idConteudo){this.idConteudo = idConteudo; return this;}
		public Builder modo(String modo){this.modo = modo; return this;}
		public Builder tipo(String tipo){this.tipo = tipo; return this;}
		public Builder valor(String valor){this.valor = valor; return this;}
		public Builder post(Post post){this.post = post; return this;}

	}
	private Conteudo(Builder builder){
		super();
		idConteudo = builder.idConteudo;
		modo = builder.modo;
		tipo = builder.tipo;
		valor = builder.valor;
		post = builder.post;
	}
	public long getIdConteudo() {
		return idConteudo;
	}
	public String getModo() {
		return modo;
	}
	public String getTipo() {
		return tipo;
	}
	public String getValor() {
		return valor;
	}
	public Post getPost() {
		return post;
	}
	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("Conteudo [idConteudo=");
		builder2.append(idConteudo);
		builder2.append(", modo=");
		builder2.append(modo);
		builder2.append(", tipo=");
		builder2.append(tipo);
		builder2.append(", valor=");
		builder2.append(valor);
		builder2.append(", post=");
		builder2.append(post);
		builder2.append("]");
		return builder2.toString();
	}
	
}
