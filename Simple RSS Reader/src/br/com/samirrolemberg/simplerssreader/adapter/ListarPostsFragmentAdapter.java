package br.com.samirrolemberg.simplerssreader.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Post;
import br.com.samirrolemberg.simplerssreader.u.U;

public class ListarPostsFragmentAdapter extends BaseAdapter{
	final ListView listView;
	final List<Post> posts;
	
	public ListarPostsFragmentAdapter(List<Post> posts, ListView listView) {
		this.posts = posts;
		this.listView = listView;
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
		//View view = activity.getLayoutInflater().inflate(R.layout.activity_lista_feeds_principal, null);
		View v = View.inflate(listView.getContext(), R.layout.fragment_list_item_listar_posts, null);
		//
		TextView titulo = (TextView) v.findViewById(R.id.titulo__exibir_post_fragment_list_item);
		TextView data = (TextView) v.findViewById(R.id.data__exibir_post_fragment_list_item);
		TextView autor = (TextView) v.findViewById(R.id.autor__exibir_post_fragment_list_item);
				
		titulo.setText(posts.get(position).getTitulo());
		//CharSequence dateFormat = DateFormat.format("HH:mm:ss dd/MM/yyyy", posts.get(position).getData_publicacao());
		//data.setText(posts.get(position).getData_publicacao().toString());
		
		data.setText(U.time_24_date_mask(posts.get(position).getData_publicacao(), v.getContext()));
		autor.setText(posts.get(position).getAutor());
		
		return v;
	}
	
}
