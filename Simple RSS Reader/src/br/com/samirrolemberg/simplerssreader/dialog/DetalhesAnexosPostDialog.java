package br.com.samirrolemberg.simplerssreader.dialog;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.adapter.ListaAnexosPostAdapter;
import br.com.samirrolemberg.simplerssreader.conn.DatabaseManager;
import br.com.samirrolemberg.simplerssreader.dao.DAOAnexo;
import br.com.samirrolemberg.simplerssreader.model.Anexo;
import br.com.samirrolemberg.simplerssreader.model.Post;


public class DetalhesAnexosPostDialog {

	private Context context;
	private View view;
	private Post post;
	private ListaAnexosPostAdapter adapter;
	
	public DetalhesAnexosPostDialog(Context context, View view, Post post) {
		super();
		this.context = context;
		this.view = view;
		this.post = post;
	}
	
	public View create(){
		DAOAnexo daoAnexo = new DAOAnexo(context);
		List<Anexo> anexos = daoAnexo.listarTudo(post);
		adapter = new ListaAnexosPostAdapter(anexos, (Activity)context);
		DatabaseManager.getInstance().closeDatabase();
		ListView list = (ListView) view.findViewById(R.id.lista_dialogo_anexos_post);
		list.setAdapter(adapter);
		
		return view;
	}
}
