package br.com.samirrolemberg.simplerssreader.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Post;


public class DetalhesPerguntaDialogPost {

	private Context context;
	private View view;
	private Post post;
	private String frase;
	
	public DetalhesPerguntaDialogPost(Context context, View view, Post post, String frase) {
		super();
		this.context = context;
		this.view = view;
		this.post = post;
		this.frase = frase;
	}
	
	public View create(){
		TextView tituloDialogo = (TextView) ((view.findViewById(R.id.dialog_titulo_customizado)).findViewById(R.id.texto_titulo_dialogo_customizado));
		tituloDialogo.setText(post.getTitulo());
		
		TextView frase = (TextView) view.findViewById(R.id.dialog_pergunta_feed__frase);
		frase.setText(this.frase);

		return view;
	}
}
