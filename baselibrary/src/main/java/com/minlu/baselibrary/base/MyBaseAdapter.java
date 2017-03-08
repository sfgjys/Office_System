package com.minlu.baselibrary.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.minlu.baselibrary.util.ViewsUitls;

import java.util.List;


public abstract class MyBaseAdapter<T> extends BaseAdapter {

    // 一般条目对应的类型码
    public static final int LIST_VIEW_ITEM = 0;
    // 加载更多条目对应的类型码
    public static final int LIST_VIEW_ITEM_MORE = 1;
    // 标题条目对应的类型码
    public static final int LIST_VIEW_TITLE = 2;

    // 创建适配器传递过来的数据集合
    private List<T> list;

    private BaseHolder<T> baseHolder;

    private MoreHolder moreHolder;

    public int position;

    public MyBaseAdapter(List<T> list) {
        this.list = list;
    }

    public int getDataSize() {
        return list.size();
    }

    @Override
    public int getCount() {
        // +1,是加载更多这个条目的本身
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        /**
         * Holder的作用：其实质是在Holder初始化出来一个条目View对象，并且给这个View进行数据赋值，
         * 然后在通过方法将这个View返回给convertView，
         * 而要想返回的View和convertView一直是绑定在一起的，可以不断复用，不用在重新初始化View，
         * 可使用setTag方法将Holder与convertView绑定在一起，然后在通过方法获取Holder中的View
         * 这里要注意的是，因为每次绑定过后其赋值的值也是一起绑定在一起的，所以在通过getTag获取绑定的Holder时需要接着重新进行赋值操作
         */

        baseHolder = null;
        if (convertView == null) {
            if (getItemViewType(position) == LIST_VIEW_ITEM_MORE) {
                baseHolder = getMoreHolder();
            } else {
                // 获取Holder的同时进行初始化View并获取相应的子控件，因为不知道要获取哪个Holder所以用抽象让子类去决定
                baseHolder = getHolder();
            }
        } else {
            baseHolder = (BaseHolder) convertView.getTag();
        }

        setItemData(position);

        // 如果baseHolder是加载更多的Holder则在getRootView方法中会进行网络请求加载
        return baseHolder.getRootView();
    }

    public void setItemData(int position) {

        // 由于多了个加载更多的条目，所以数据源对于最后的这个条目是没有数据的，如果用get获取会造成角标越界，所以要判断
        // 设置数据到非加载更多的条目上，当添加新的条目类型时，子适配器可以重写该方法
        if (getItemViewType(position) != LIST_VIEW_ITEM_MORE) {
            baseHolder.setPosition(position);

            baseHolder.setData(list.get(position));
        }
    }

    // 获取加载更多的Holder
    public BaseHolder getMoreHolder() {
        // 只有在第一次显示加载时才创建MoreHolder，并在其构造方法中给MoreHolder的setData方法设置加载情况的值
        // 一开始是HAS_MORE，所以可以在getRootView时请求网络数据
        // 然后在请求完数据后返回数据时，在根据返回数据的情况去调用setData方法设置加载情况的值
        // 在这些情况中有些是需要将返回的数据加入数据adapter的数据源并重新刷新，所以在这时我们如果在此将ListView拉到最下面，
        // 则会再次走本方法来获取MoreHolder对象，而如果不用这下面这个判断则会重新创建一个MoreHolder，
        // 则不管前面根据返回值设置的任何情况都会被其构造方法中的setData方法重新设置，而且是根据hasMore方法返回的值去设置
        // 则会和第一次一样继续请求网络加载数据，而只有使用旧的MoreHolder对象，这个对象的setData方法在请求网络记载数据时设置的
        // 会依据这时的设置来显示
        if (moreHolder == null) {
            moreHolder = new MoreHolder(hasMore(), this);
        }
        return moreHolder;
    }

    // 获取是否加载更多，可由子类决定
    public boolean hasMore() {
        return true;
    }

    public abstract BaseHolder getHolder();

    /**
     * 我们可以通过下面的getItemViewType方法在getView方法中来区分一般条目和加载更多条目，以此来显示对应类型的条目显示
     */
    // 获取对应position的条目的条目类型
    @Override
    public int getItemViewType(int position) {
        if (position == (getCount() - 1)) {
            // 显示加载更多界面条目所代表的类型
            return LIST_VIEW_ITEM_MORE;
        }
        // 除了加载更多的条目所代表的类型
        return getItemViewOtherType(position);
    }

    public int getItemViewOtherType(int position) {
        // 有可能还有其他类型的条目，后期重写该方法

        // 返回的是一般条目的类型
        return LIST_VIEW_ITEM;
    }

    // 获取条目类型的个数
    @Override
    public int getViewTypeCount() {
        // 原先有1中条目类型，现在再加一种显示加载更多的条目类型
        return super.getViewTypeCount() + 1;
    }

    /**
     * 网络请求
     */
    // 在加载更多条目显示的时候被调用
    public void loadMore() {

        new Thread() {
            public void run() {
                // 请求网络获取数据
                List<T> onLoadMore = onLoadMore();
                // 在子线程中使用获取到的数据更新Ui
                ViewsUitls.runInMainThread(new MyRunnable(onLoadMore));
            }
        }.start();
    }

    private class MyRunnable implements Runnable {
        private List<T> onLoadMore;

        public MyRunnable(List<T> onLoadMore) {
            this.onLoadMore = onLoadMore;
        }

        @Override
        public void run() {
            if (onLoadMore == null) {
                // 说明获取数据失败
                moreHolder.setData(MoreHolder.MORE_ERROR);
            } else {
                if (onLoadMore.size() < 10) {
                    moreHolder.setData(MoreHolder.NO_MORE);
                } else {
                    moreHolder.setData(MoreHolder.HAS_MORE);
                }
            }

            if (onLoadMore != null && onLoadMore.size() > 0) {
                list.addAll(onLoadMore);
                notifyDataSetChanged();
            }
        }
    }

    public abstract List<T> onLoadMore();

}
