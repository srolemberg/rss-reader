package br.com.samirrolemberg.simplerssreader.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Anexo;

public class ListaAnexosPostAdapter extends BaseAdapter {

	private final List<Anexo> anexos;
	private final Activity activity;
	
	public ListaAnexosPostAdapter(List<Anexo> anexos, Activity activity) {
		this.anexos = anexos;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return anexos.size();
	}

	@Override
	public Object getItem(int position) {
		return anexos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return anexos.get(position).getIdAnexo();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(R.layout.dialog_anexos_post_conteudo, null);

		LinearLayout linear = (LinearLayout) view.findViewById(R.id.dialog_anexos_post__linear);
		TextView tamanho = (TextView) view.findViewById(R.id.dialog_anexos_post__tamanho);
		TextView tipo = (TextView) view.findViewById(R.id.dialog_anexos_post__tipo);
		TextView url = (TextView) view.findViewById(R.id.dialog_anexos_post__url);
		
		//if (anexos!=null || anexos.size()>0) {
			final Anexo anexo = anexos.get(position);
			
			tamanho.setText(anexo.getTamanho()+"");
			tipo.setText(anexo.getTipo());
			url.setText(anexo.getUrl());
			
			linear.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Toast.makeText(activity, "Click: "+anexos.size(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(anexo.getUrl()));
					activity.startActivity(intent);
				}
			});
		//}
		return view;
	}

}
