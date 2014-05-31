package br.com.samirrolemberg.simplerssreader.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Feed;

public class ListaFeedAdapter extends BaseAdapter {

	private final List<Feed> feeds;
	private final Activity activity;
	
	public ListaFeedAdapter(List<Feed> feeds, Activity activity) {
		this.feeds = feeds;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return feeds.size();
	}

	@Override
	public Object getItem(int position) {
		return feeds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return feeds.get(position).getIdFeed();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(R.layout.activity_lista_feeds_principal, null);

		Feed f = feeds.get(position);
		
		TextView nome = (TextView) view.findViewById(R.id.nome_feed);		
		nome.setText(f.getTitulo());
		TextView rss = (TextView) view.findViewById(R.id.url_feed);		
		rss.setText(f.getRss());			

		
		return view;
	}

}
