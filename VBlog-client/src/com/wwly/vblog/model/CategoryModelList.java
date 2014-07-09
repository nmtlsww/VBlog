package com.wwly.vblog.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CategoryModelList
{
    private final ArrayList<CategoryModel> mList = new ArrayList<CategoryModel>();

    public CategoryModelList()
    {
    }

    public void parse(JSONObject object)
    {
        clear();
        JSONArray jsonArray = object.optJSONArray("category_list");
        for (int i = 0; i < jsonArray.length(); i++)
        {
        	CategoryModel model = new CategoryModel(jsonArray.optJSONObject(i));
        	addModel(model);
        }
    }

    private void addModel(CategoryModel model)
    {
        for (CategoryModel itemModel : mList)
        {
            if (model.getId() == itemModel.getId())
            {
                return;
            }
        }
        mList.add(model);
    }

    public ArrayList<CategoryModel> getList()
    {
        return mList;
    }

    public void clear()
    {
    	mList.clear();
    }

}