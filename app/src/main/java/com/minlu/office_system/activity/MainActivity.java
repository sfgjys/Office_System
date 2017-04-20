package com.minlu.office_system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.minlu.baselibrary.customview.MyLinearLayout;
import com.minlu.baselibrary.manager.ThreadManager;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.FragmentShowHide;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.bean.NoticeList;
import com.minlu.office_system.fragment.HomePageFragment;
import com.minlu.office_system.fragment.MeFragment;
import com.minlu.office_system.fragment.SettingFragment;
import com.minlu.office_system.fragment.dialog.NoticeInformListDialog;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import okhttp3.Response;

public class MainActivity extends FragmentActivity {

    private FragmentShowHide fragmentShowHide;
    private int showWhichFragment = 0;
    private MyLinearLayout myLinearLayout;
    private FrameLayout mLoadingUI;
    private String mNoticeListResult;
    private List<NoticeList> mNoticeListData;

    /*private ViewPager mViewPager;
    private TextView tvTitle;
    private LinearLayout llContainer;

    private int[] mImageIds = new int[]{R.mipmap.a, R.mipmap.b,
            R.mipmap.c, R.mipmap.d, R.mipmap.e};

    // 图片标题集合
    private final String[] mImageDes = {"巩俐不低俗，我就不能低俗", "朴树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级", "乐视网TV版大派送", "热血屌丝的反杀"};

    private int mPreviousPos;// 上一个页面位置

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int currentItem = mViewPager.getCurrentItem();// 获取当前页面位置
            mViewPager.setCurrentItem(++currentItem);// 跳到下一个页面

            // 继续发送延时2秒的消息, 形成类似递归的效果, 使广告一直循环切换
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };*/
    private View mTopAdvertisement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopAdvertisement = findViewById(R.id.fl_top_advertisement);

/*        mViewPager = (ViewPager) findViewById(R.id.view_pager_top_advertisement);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);

        mViewPager.setAdapter(new MyAdapter());// 给viewpager设置数据
        // mViewPager.setCurrentItem(Integer.MAX_VALUE/2);
        mViewPager.setCurrentItem(mImageIds.length * 10000);

        // 设置滑动监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // 某个页面被选中
            @Override
            public void onPageSelected(int position) {
                int pos = position % mImageIds.length;
                tvTitle.setText(mImageDes[pos]);// 更新新闻标题

                // 更新小圆点
                llContainer.getChildAt(pos).setEnabled(true);// 将选中的页面的圆点设置为红色
                // 将上一个圆点变为灰色
                llContainer.getChildAt(mPreviousPos).setEnabled(false);

                // 更新上一个页面位置
                mPreviousPos = pos;
            }

            // 滑动过程中
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            // 滑动状态变化
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvTitle.setText(mImageDes[0]);// 初始化新闻标题

        // 动态添加5个小圆点
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setImageResource(R.drawable.selector_top_advertisement_point_);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i != 0) {// 从第2个圆点开始设置左边距, 保证圆点之间的间距
                params.leftMargin = 6;
                view.setEnabled(false);// 设置不可用, 变为灰色圆点
            }

            view.setLayoutParams(params);

            llContainer.addView(view);
        }

        // 延时2秒更新广告条的消息
        mHandler.sendEmptyMessageDelayed(0, 2000);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeCallbacksAndMessages(null);// 清除所有消息和Runnable对象
                        break;
                    case MotionEvent.ACTION_UP:
                        // 继续轮播广告
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                        break;

                    default:
                        break;
                }

                return false;// 返回false, 让viewpager原生触摸效果正常运行
            }
        });*/

        myLinearLayout = (MyLinearLayout) findViewById(R.id.custom_ll);
        mLoadingUI = (FrameLayout) findViewById(R.id.main_fl_loading);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_button);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.main_home_page_icon, "首页").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .addItem(new BottomNavigationItem(R.mipmap.main_me_icon, "我的").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .addItem(new BottomNavigationItem(R.mipmap.main_setting_icon, "设置").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .setFirstSelectedPosition(0)// 首次选择哪个
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {// 按下Tab时会调用
                showWhichFragment = position;
                System.out.println("onTabSelected: " + position);
                if (fragmentShowHide != null) {
                    fragmentShowHide.showFragment(position, R.id.fl_replace_fragment, getSupportFragmentManager().beginTransaction());
                }
                if(position!=0){
                    mTopAdvertisement.setVisibility(View.GONE);
                }else {
                    mTopAdvertisement.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(int position) {// 原先被按下过的Tab不再是被按下的会调用
                System.out.println("onTabUnselected: " + position);
            }

            @Override
            public void onTabReselected(int position) {
                System.out.println("onTabReselected: " + position);
            }
        });

        List<String> tagList = new ArrayList<>();
        tagList.add("fragmentTag_homePage");
        tagList.add("fragmentTag_me");
        tagList.add("fragmentTag_setting");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomePageFragment());
        fragmentList.add(new MeFragment());
        fragmentList.add(new SettingFragment());

        if (savedInstanceState == null) {
            System.out.println("第一次进入该界面，需要通过add来进行展示Fragment");
            showFragment(tagList, fragmentList, 0);
        } else {
            for (int i = 0; i < tagList.size(); i++) {
                Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tagList.get(i));
                if (fragmentByTag != null) {
                    fragmentList.remove(i);
                    fragmentList.add(i, fragmentByTag);
                }
            }
            System.out.println("第二次+++++++++++++++++++++++++++++");
            int saveWhichFragment = savedInstanceState.getInt("showWhichFragment", 0);
            bottomNavigationBar.selectTab(saveWhichFragment);
            showFragment(tagList, fragmentList, saveWhichFragment);
        }

        if (SharedPreferencesUtil.getBoolean(ViewsUitls.getContext(), StringsFiled.IS_FIRST_START_MAIN, false)) {
            SharedPreferencesUtil.saveBoolean(ViewsUitls.getContext(), StringsFiled.IS_FIRST_START_MAIN, false);
            startNoticeInformThread();
        }
    }

    private void startNoticeInformThread() {
        myLinearLayout.setIsInterruptTouch(true);
        mLoadingUI.setVisibility(View.VISIBLE);
        ThreadManager.getInstance().execute(new TimerTask() {
            @Override
            public void run() {
                showNoticeInform();
            }
        });
    }

    private void showNoticeInform() {
        Response response = OkHttpMethod.synPostRequest(IpFiled.NOTICE_LIST, null);

        if (response != null && response.isSuccessful()) {// 请求成功则获取返回结果字符串
            try {
                mNoticeListResult = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.interentIsNormal(mNoticeListResult)) {// 返回结果字符串正常就解析
            try {
                JSONObject jsonObject = new JSONObject(mNoticeListResult);
                if (jsonObject.has("rows")) {
                    mNoticeListData = new ArrayList<>();// 有total值说明返回json字符串格式正确,不管有没有公告 都先创建数据对象
                    JSONArray jsonArray = jsonObject.optJSONArray("rows");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject notice = jsonArray.optJSONObject(i);
                        JSONObject noticeDetail = notice.optJSONObject("cell");
                        mNoticeListData.add(new NoticeList(noticeDetail.optString("NOTICENAME"), noticeDetail.optString("NOTICEEMPNAME"), noticeDetail.optString("SDATE"), noticeDetail.optInt("ID"), false));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                cancelConfine();
                if (mNoticeListData != null) {
                    if (mNoticeListData.size() > 0) {
                        NoticeInformListDialog noticeInformListDialog = new NoticeInformListDialog();
                        noticeInformListDialog.setData(mNoticeListData);
                        noticeInformListDialog.show(getSupportFragmentManager(), "NoticeInformListDialog");
                    } else {
                        ToastUtil.showToast(ViewsUitls.getContext(), "无公告");
                    }
                } else {
                    ToastUtil.showToast(ViewsUitls.getContext(), "公告异常");
                }
            }
        });
    }

    /* 取消点击事件阻止和加载页面 */
    private void cancelConfine() {
        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                myLinearLayout.setIsInterruptTouch(false);
                mLoadingUI.setVisibility(View.GONE);
            }
        });
    }

    private void showFragment(List<String> tagList, List<Fragment> fragmentList, int which) {
        fragmentShowHide = new FragmentShowHide(fragmentList, tagList);
        fragmentShowHide.showFragment(which, R.id.fl_replace_fragment, getSupportFragmentManager().beginTransaction());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("showWhichFragment", showWhichFragment);// 将选择哪个Tab进行存储
        super.onSaveInstanceState(outState);
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /*class MyAdapter extends PagerAdapter {

        // 返回item的个数
        @Override
        public int getCount() {
            // return mImageIds.length;
            return Integer.MAX_VALUE;
        }

        // 判断当前要展示的view和返回的object是否是一个对象, 如果是,才展示
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 类似getView方法, 初始化每个item的布局, viewpager默认自动加载前一张和后一张图片, 保证始终保持3张图片,
        // 剩余的都需要销毁
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 0,1, 5->0, 6->1, 10->0
            int pos = position % mImageIds.length;

            ImageView view = new ImageView(MainActivity.this);
            // view.setImageResource(mImageIds[position]);
            view.setBackgroundResource(mImageIds[pos]);

            // 将item的布局添加给容器
            container.addView(view);
            // System.out.println("初始化item..." + pos);

            return view;// 返回item的布局对象
        }

        // item销毁的回调方法
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 从容器中移除布局对象
            container.removeView((View) object);
            // System.out.println("销毁item..." + position);
        }

    }*/
}
