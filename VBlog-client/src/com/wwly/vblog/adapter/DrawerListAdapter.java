package com.wwly.vblog.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wwly.vblog.R;
import com.wwly.vblog.model.DrawerListModel;

public class DrawerListAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context mContext;
	private LayoutInflater mInflater;
	private List<DrawerListModel> mItems;

	public DrawerListAdapter(Context context, List<DrawerListModel> items) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mItems = items;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (mItems != null)
			count = mItems.size();
		return count;
	}

	@Override
	public DrawerListModel getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHodler holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
			holder = new ViewHodler();
			holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHodler) convertView.getTag();
		}

		final DrawerListModel model = mItems.get(position);
		holder.mIcon.setImageResource(model.getIconRes());
		holder.mName.setText(model.getName());
		return convertView;
	}

	private class ViewHodler {
		ImageView mIcon;
		TextView mName;
	}

}
