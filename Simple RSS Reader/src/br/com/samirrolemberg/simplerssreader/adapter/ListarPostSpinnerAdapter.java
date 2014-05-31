package br.com.samirrolemberg.simplerssreader.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Post;


public class ListarPostSpinnerAdapter extends BaseAdapter {

	private Activity activity;
	private List<Post> posts;
	
	public ListarPostSpinnerAdapter(List<Post> posts, Activity activity) {
		this.activity = activity;
		this.posts = posts;
	}

	@Override
	public int getCount() {
		return posts.size();
	}

	@Override
	public Object getItem(int position) {
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return posts.get(position).getIdPost();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(R.layout.spinner_listar_posts, null);

		Post p = posts.get(position);
		
		TextView nome = (TextView) view.findViewById(R.id.nome_post__exibir_post_spinner);		
		nome.setText(p.getTitulo());
		TextView link = (TextView) view.findViewById(R.id.url_post__exibir_post_spinner);		
		link.setText(p.getLink());			

		return view;
	}
}
