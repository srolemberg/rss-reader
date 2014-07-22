package br.com.samirrolemberg.simplerssreader.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;
import br.com.samirrolemberg.simplerssreader.model.Feed;


public class ListaFeedSpinnerAdapter extends BaseAdapter {

	private Activity activity;
	private List<Feed> feeds;
	
	public ListaFeedSpinnerAdapter(List<Feed> feeds, Activity activity) {
		this.activity = activity;
		this.feeds = feeds;
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
		View view = activity.getLayoutInflater().inflate(R.layout.spinner_listar_feeds, null);

		Feed f = feeds.get(position);
		
		TextView nome = (TextView) view.findViewById(R.id.nome_feed__exibir_feeds_spinner);		
		nome.setText(f.getTitulo());
		TextView rss = (TextView) view.findViewById(R.id.url_feed__exibir_feeds_spinner);		
		rss.setText(f.getRss());			
		
		return view;
	}
}
