package com.minlu.baselibrary.base;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.R;
import com.minlu.baselibrary.util.ViewsUitls;


public class MoreHolder extends BaseHolder<Integer> {

    // 有更多对应的状态
    public static final int HAS_MORE = 0;
    // 没有更多对应的状态
    public static final int NO_MORE = 1;
    // 加载失败对应的状态
    public static final int MORE_ERROR = 2;

    private LinearLayout hasMore;
    private TextView moreError;
    private MyBaseAdapter adapter;

    public MoreHolder(boolean hasMore2, MyBaseAdapter myAdapter) {
        this.adapter = myAdapter;

        // 在普通条目中是传递要展示的数据，在次则是传递状态数据
        // 设置数据调用setData方法中的setRelfshData方法，根据传递参数来决定控件的显示
        setData(hasMore2 ? HAS_MORE : NO_MORE);
    }

    @Override
    public View initView() {
        View view = ViewsUitls.inflate(R.layout.layout_load_more);
        hasMore = (LinearLayout) view.findViewById(R.id.has_more);
        moreError = (TextView) view.findViewById(R.id.more_error);
      /*  moreError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("加载失败后，重新点击加载更多");
                setRelfshData(HAS_MORE, -1);
                adapter.loadMore();
            }
        });*/
        return view;
    }

    @Override
    public void setRelfshData(Integer mData, int position) {
        if (mData == HAS_MORE) {
            hasMore.setVisibility(View.VISIBLE);
            moreError.setVisibility(View.GONE);
        }
        if (mData == NO_MORE) {
            hasMore.setVisibility(View.GONE);
            moreError.setVisibility(View.GONE);
        }
        if (mData == MORE_ERROR) {
            hasMore.setVisibility(View.GONE);
            moreError.setVisibility(View.VISIBLE);
        }
    }

    // 这个方法是在要显示条目获取条目View的时候调用
    @Override
    public View getRootView() {
        // 第一次显示加载更多条目时如果在子适配器中重写了hasMore()方法并传递了false过来则在此就不会执行加载更多的网络请求
        // 而且在这里限制是否加载网络可以在notifyDataSetChanged后在此下拉到最后的条目时，
        // 根据其在使用notifyDataSetChanged方法前重新设置的setData方法来判断是否要加载数据
        if (getData() == HAS_MORE && adapter != null) {
            adapter.loadMore();
        }
        // 在执行下面父类的方法前先执行MyAdapter中的网络加载更多的请求
        // 父类中是返回我们初始话并设置好值的View控件
        return super.getRootView();
    }

}
