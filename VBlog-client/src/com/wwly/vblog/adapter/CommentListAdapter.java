package com.wwly.vblog.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wwly.vblog.R;
import com.wwly.vblog.model.CommentModel;

public class CommentListAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context mContext;
	private LayoutInflater mInflater;
	private List<CommentModel> mItems;

	public CommentListAdapter(Context context, List<CommentModel> items) {
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
	public CommentModel getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.comment_list_item, null);

			holder = new ViewHodler();
			holder.mUsername = (TextView) convertView.findViewById(R.id.item_comment_username);
			holder.mContent = (TextView) convertView.findViewById(R.id.item_comment_content);
			holder.mCreateTime = (TextView) convertView.findViewById(R.id.item_comment_create_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHodler) convertView.getTag();
		}

		CommentModel model = mItems.get(position);
		holder.mUsername.setText(model.getUsername());
		holder.mContent.setText(model.getContent());
		holder.mCreateTime.setText(model.getCreateTime());

		return convertView;
	}

	private class ViewHodler {
		TextView mUsername;
		TextView mContent;
		TextView mCreateTime;
	}

}
