package com.orinameh.googlemapdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orinameh.googlemapdemo.R;
import com.orinameh.googlemapdemo.model.Incidence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidevhade on 9/26/16.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;
//    private int[] images = {R.drawable.accident, R.drawable.flame, R.drawable.thief};
//    private String[] events;
    private List<Incidence> mList = new ArrayList<>();

    public GridAdapter(Context ctx, List<Incidence> list) {

        this.mContext = ctx;
        this.mList = list;

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.category, null);
        }
        Incidence incidence = new Incidence();
        ImageView ivCat = (ImageView) view. findViewById(R.id.category_iv);
        TextView tvCat = (TextView) view. findViewById(R.id.category_tv);

        return view;
    }
}
