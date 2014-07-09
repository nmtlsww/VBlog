package com.wwly.vblog.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wwly.vblog.R;
import com.wwly.vblog.model.CategoryModel;

public class CategoryListAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context mContext;
	private LayoutInflater mInflater;
	private List<CategoryModel> mItems;

	public CategoryListAdapter(Context context, List<CategoryModel> items) {
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
	public CategoryModel getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.category_list_item, null);

			holder = new ViewHodler();
			holder.mCategoryName = (TextView) convertView.findViewById(R.id.item_category_name);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHodler) convertView.getTag();
		}

		CategoryModel model = mItems.get(position);
		holder.mCategoryName.setText(model.getCategoryName());

		return convertView;
	}

	private class ViewHodler {
		TextView mCategoryName;
	}

}
