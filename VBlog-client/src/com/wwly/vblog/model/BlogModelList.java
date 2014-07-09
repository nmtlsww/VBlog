package com.wwly.vblog.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BlogModelList
{
    private final ArrayList<BlogModel> mList = new ArrayList<BlogModel>();

    public BlogModelList()
    {
    }

    public void parse(JSONObject object)
    {
        clear();
        JSONArray jsonArray = object.optJSONArray("blog_list");
        for (int i = 0; i < jsonArray.length(); i++)
        {
        	BlogModel model = new BlogModel(jsonArray.optJSONObject(i));
        	addModel(model);
        }
    }

    private void addModel(BlogModel model)
    {
        for (BlogModel itemModel : mList)
        {
            if (model.getId() == itemModel.getId())
            {
                return;
            }
        }
        mList.add(model);
    }

    public ArrayList<BlogModel> getList()
    {
        return mList;
    }

    public void clear()
    {
    	mList.clear();
    }

}