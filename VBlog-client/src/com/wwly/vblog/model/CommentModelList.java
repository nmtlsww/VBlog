package com.wwly.vblog.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommentModelList
{
    private final ArrayList<CommentModel> mList = new ArrayList<CommentModel>();

    public CommentModelList()
    {
    }

    public void parse(JSONObject object)
    {
        clear();
        JSONArray jsonArray = object.optJSONArray("comment_list");
        for (int i = 0; i < jsonArray.length(); i++)
        {
        	CommentModel model = new CommentModel(jsonArray.optJSONObject(i));
        	addModel(model);
        }
    }

    private void addModel(CommentModel model)
    {
        for (CommentModel itemModel : mList)
        {
            if (model.getId() == itemModel.getId())
            {
                return;
            }
        }
        mList.add(model);
    }

    public ArrayList<CommentModel> getList()
    {
        return mList;
    }

    public void clear()
    {
    	mList.clear();
    }

}