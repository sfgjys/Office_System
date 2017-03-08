package com.minlu.baselibrary.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.minlu.baselibrary.R;
import com.minlu.baselibrary.util.ViewsUitls;


public abstract class ContentPage extends FrameLayout {

    // 正在加载对应的状态
    public static final int STATE_LOADING = 0;
    // 正在加载对应的状态
    public static final int STATE_EMPTY = 1;
    // 正在加载对应的状态
    public static final int STATE_ERROR = 2;
    // 正在加载对应的状态
    public static final int STATE_SUCCESS = 3;

    // 标识当前状态
    private int state;

    private View loadingView;
    private View emptyView;
    private View errorView;
    private View successView;
    private LayoutParams params;

    public ContentPage(Context context) {
        super(context);

        // 设置本FrameLayout的宽高为match_parent
        params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        initView();
        showPage();
    }

    // 初始化布局
    private void initView() {
        loadingView = ViewsUitls.inflate(R.layout.layout_loading);
        // 添加控件进本FrameLayout，并且传上其宽高限制
        addView(loadingView, params);

        emptyView = ViewsUitls.inflate(R.layout.layout_empty);
        emptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataAndRefresh();
            }
        });
        addView(emptyView, params);

        errorView = ViewsUitls.inflate(R.layout.layout_error);
        errorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataAndRefresh();
            }
        });

        addView(errorView, params);

        state = STATE_LOADING;
    }

    /**
     * 根据当前状态展示对应的界面
     */
    private void showPage() {

        // 之所以要判断加载是否成功，在成功后才添加控件是因为成功界面需要网络请求来的一些数据
        // 这个判断是用来添加控件的，本来放在initView中比较应景，
        // 在网络请求没有得出结果前走了本类控件，并显示加载中页面时，这个判断也不会走无所谓方法哪，
        // 但是当请求网络有了结果后，需要重现走showPage方法，显示对应结果的界面，有可能是成功，所以在显示前取要添加控件
        if (state == STATE_SUCCESS) {
            successView = onCreateSuccessView();
            if (successView != null) {
                // 在创建添加进来的View时不用在考虑其宽高，在此处已经考虑过了，并传递了参数进来
                addView(successView, params);
            }
        }

        if (loadingView != null) {
            loadingView.setVisibility(state == STATE_LOADING ? View.VISIBLE
                    : View.GONE);
        }

        if (emptyView != null) {
            emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.GONE);
        }

        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.GONE);
        }
        // 为了不出现以外所有界面控件在一开始都要隐藏，有且只有一个是不隐藏
        // 且这个成功的界面的是否显示判断必须在添加了控件后，否则会无法显示
        if (successView != null) {
            successView.setVisibility(state == STATE_SUCCESS ? View.VISIBLE
                    : View.GONE);
        }
    }

    public void loadDataAndRefresh() {

        // 当第一次加载本类时state状态是STATE_LOADING，符合此判断可进入请求网络数据，
        // 当滑动ViewPager时没有超出其缓存时,state状态是STATE_SUCCESS，不符合此判断，所以变不会请求网络数据并添加控件
        // 当滑动ViewPager超出其缓存时，则本类被销毁，在此回到本类时便需要重新在走一遍
        if (state != STATE_SUCCESS) {
            // 第一次进入本来是加载中，但是第一次以后的网络请求有结果后state的状态便会改变，所以需要重新赋值状态
            state = STATE_LOADING;
            // 在重新请求网络前在显示一次加载界面
            showPageSafe();

            new Thread() {
                public void run() {
                    state = onLoad().getValue();
                    // 根据新state更新UI
                    showPageSafe();
                }
            }.start();
        }
    }

    /**
     * 在主线程更新UI
     */
    protected void showPageSafe() {
        ViewsUitls.runInMainThread(new Runnable() {
            @Override
            public void run() {
                showPage();
            }
        });
    }

    public void showPageHM(ResultState resultState) {
        state = resultState.getValue();
        showPage();
    }

    public abstract View onCreateSuccessView();

    public abstract ResultState onLoad();

    public enum ResultState {
        STATE_LOADING(0), STATE_EMPTY(1), STATE_ERROR(2), STATE_SUCCESS(3);

        private int value;

        private ResultState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
