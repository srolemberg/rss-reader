package br.com.samirrolemberg.simplerssreader.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import br.com.samirrolemberg.simplerssreader.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> header;//header
	private Map<String, List<String>> child;//child
	
	public ExpandableListAdapter(Context context, List<String> header, Map<String, List<String>> child){
		super();
		this.context = context;
		this.header = header;
		this.child = child;
	}
	
	@Override
	public int getGroupCount() {
		return this.header.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.child.get(this.header.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.header.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.child.get(this.header.get(groupPosition));
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
//		if (convertView==null) {
//			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(R.layout.grupo_activity_ajuda_lista, null);
//		}
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.grupo_activity_ajuda_lista, null);
		
		TextView textViewHeader = (TextView) convertView.findViewById(R.id.expandable_ajuda_texto_grupo_header);
		textViewHeader.setTypeface(null, Typeface.BOLD);
		textViewHeader.setText(headerTitle);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		final ArrayList<String> childText = (ArrayList<String>) getChild(groupPosition, childPosition);
		
		final String child = childText.get(childPosition);
		
//		if (convertView!=null) {
//			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(R.layout.grupo_activity_ajuda, null);
//		}
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.grupo_activity_ajuda, null);
		
		TextView textViewChild = (TextView) convertView.findViewById(R.id.expandable_ajuda_texto_grupo_child);
		textViewChild.setText(child);
		
		return convertView;
	}


}
