package com.wwly.vblog.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wwly.vblog.R;
import com.wwly.vblog.model.BlogModel;

public class BlogListAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context mContext;
	private LayoutInflater mInflater;
	private List<BlogModel> mItems;

	public BlogListAdapter(Context context, List<BlogModel> items) {
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
	public BlogModel getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.blog_list_item, null);

			holder = new ViewHodler();
			holder.mTile = (TextView) convertView.findViewById(R.id.item_blog_title);
			holder.mContent = (TextView) convertView.findViewById(R.id.item_blog_content);
			holder.mCategoryName = (TextView) convertView.findViewById(R.id.item_blog_category_name);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHodler) convertView.getTag();
		}

		BlogModel model = mItems.get(position);
		holder.mTile.setText(model.getTitle());
		holder.mContent.setText(model.getContent());
		holder.mCategoryName.setText(model.getCategoryName());

		return convertView;
	}

	private class ViewHodler {
		TextView mTile;
		TextView mContent;
		TextView mCategoryName;
	}

}
