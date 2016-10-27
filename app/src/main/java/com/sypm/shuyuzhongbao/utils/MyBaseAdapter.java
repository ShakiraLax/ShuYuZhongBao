package com.sypm.shuyuzhongbao.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class MyBaseAdapter extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List mList;

    private MyBaseAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public MyBaseAdapter(Context context, List list) {
        this(context);
        mList = list;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void refresh(List list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addAll(List list) {
        if (mList != null) {
            mList.addAll(list);
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
