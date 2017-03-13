package com.minlu.office_system.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.adapter.PagerBaseAdapter;
import com.minlu.office_system.customview.GuidePager;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends Activity implements OnPageChangeListener {

    private GuidePager mPager;// 轮播图控件
    private LinearLayout mGuidePoints;// 轮播图下的点父控件
    private RelativeLayout mIsGonePoint;// 这个控件可确认是否隐藏轮播图下的点
    private Button mIsSkip;// 跳转到MainActivity的按钮

    private View mScrollPoint;// 滑动的点

    private int[] mPagerData = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};// 轮播图图片数据id数组
    private ArrayList<ImageView> mGuideList; // 轮播图转换为Image后存储的集合

    private int mMoveX;// 滑动的点移的标准距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        SharedPreferencesUtil.saveBoolean(GuideActivity.this, "is_first_login", false);

        mPager = (GuidePager) findViewById(R.id.vp_guide);
        mGuidePoints = (LinearLayout) findViewById(R.id.ll_guide_point);
        mIsGonePoint = (RelativeLayout) findViewById(R.id.rl_guide_point);
        mIsSkip = (Button) findViewById(R.id.bt_skip);


        // 初始化数据
        initData();
        // 初始化控件
        initView();

    }

    // 获取了一个存储有所有轮播图图片ImageView的集合
    private void initData() {
        int i = 0;

        // 将id数组转换成ImageView存储进集合中
        mGuideList = new ArrayList<>();
        for (int id : mPagerData) {
            // 此处代码是用来将轮播的图片存储进集合中
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setBackgroundResource(id);
            mGuideList.add(imageView);

            // TODO 添加了相应数量的轮播图下的点
            // 根据存储轮播图的循环次数动态生成相对应数量的点
            View point = new View(GuideActivity.this);
            point.setBackgroundResource(R.drawable.shape_guide_point_normal);// 轮播图下的连续的点 (默认状态)
            // 设置我们点在线性布局中的宽高属性以及其他属性
            LayoutParams params = new LayoutParams(
                    ViewsUitls.dpToPx(9),
                    ViewsUitls.dpToPx(9));// 轮播图下的点的大小
            if (i != 0) {
                params.leftMargin = ViewsUitls.dpToPx(10);// 轮播图下点之间的距离
            }
            i++;
            point.setLayoutParams(params);
            mGuidePoints.addView(point);
        }
        // TODO 显示点的整套布局
        mIsGonePoint.setVisibility(View.VISIBLE);
    }

    // 设置轮播图的适配器
    private void initView() {
        // 获取轮播图适配器,并给Pager设置适配器
        GuidePagerAdapter guidePagerAdapter = new GuidePagerAdapter(mGuideList);
        mPager.setAdapter(guidePagerAdapter);
        // TODO 可以在此处设置轮播图的动画效果
        // mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        // 监听
        mPager.setOnPageChangeListener(this);

        // 设置跳转按钮的一些属性
        setButtonParams();

        // TODO 获取要移动的标准距离
        setScrollPoint();
    }

    // TODO 设置跳转按钮的一些属性
    private void setButtonParams() {
        mIsSkip.setBackgroundColor(Color.TRANSPARENT);
        mIsSkip.setText("立即跳转");
        mIsSkip.setTextColor(Color.RED);
        mIsSkip.setTextSize(ViewsUitls.dpToPx(9));
    }

    // 获取要移动的标准距离
    private void setScrollPoint() {
        mScrollPoint = findViewById(R.id.v_guide_scroll_point);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewsUitls.dpToPx(10), ViewsUitls.dpToPx(10));
        mScrollPoint.setLayoutParams(params);
        mScrollPoint.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMoveX = mGuidePoints.getChildAt(1).getLeft()
                        - mGuidePoints.getChildAt(0).getLeft();
            }
        }, 26);
    }

    // 自定义的轮播图适配器
    public class GuidePagerAdapter extends PagerBaseAdapter<ImageView> {

        GuidePagerAdapter(List<ImageView> list) {
            super(list);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((GuidePager) container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((GuidePager) container).removeView(mList.get(position));
        }
    }

    // 滑动时调用 参数一 滑动到的图片位置
    // 参数二 时时改变的值0到1，是前一张图到后一张图之间时时递增到1
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (null != mScrollPoint) {
            mScrollPoint.setTranslationX(mMoveX * (position + positionOffset));
        }
    }

    // 滑动选中
    @Override
    public void onPageSelected(int position) {
        if (position == (mGuideList.size() - 1)) {
            mIsSkip.setVisibility(View.VISIBLE);
        } else {
            mIsSkip.setVisibility(View.GONE);
        }
    }

    // 滑动状态发生改变
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    // 跳转至主界面
    public void guideskip(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
