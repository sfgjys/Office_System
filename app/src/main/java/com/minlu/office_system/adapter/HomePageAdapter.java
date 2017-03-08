package com.minlu.office_system.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.HomePageItem;

import java.util.List;

/**
 * Created by user on 2017/2/27.
 */

public class HomePageAdapter extends BaseAdapter {

    private final List<HomePageItem> date;

    public HomePageAdapter(List<HomePageItem> date) {
        this.date = date;
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View inflate = ViewsUitls.inflate(R.layout.item_home_page);

        TextView textView = (TextView) inflate.findViewById(R.id.tv_home_page_item);
        textView.setText(date.get(position).getFunctionName());
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_home_page_item);
        imageView.setImageResource(date.get(position).getFunctionIconId());

        return inflate;
    }
}
