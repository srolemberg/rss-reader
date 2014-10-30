package br.com.samirrolemberg.simplerssreader.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Feed;


public class DetalhesPerguntaDialogFeed {

	private Context context;
	private View view;
	private Feed feed;
	private String frase;
	
	public DetalhesPerguntaDialogFeed(Context context, View view, Feed feed, String frase) {
		super();
		this.context = context;
		this.view = view;
		this.feed = feed;
		this.frase = frase;
	}
	
	public View create(){
		TextView tituloDialogo = (TextView) ((view.findViewById(R.id.dialog_titulo_customizado)).findViewById(R.id.texto_titulo_dialogo_customizado));
		tituloDialogo.setText(feed.getTitulo());
		
		TextView frase = (TextView) view.findViewById(R.id.dialog_pergunta_feed__frase);
		frase.setText(this.frase);

		return view;
	}
}
