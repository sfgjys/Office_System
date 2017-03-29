package com.minlu.office_system.bean;

/**
 * Created by user on 2017/3/29.
 */

public class NoticeList {

    private String mNoticeTitle;
    private String mNoticeIssuer;
    private String mNoticeTime;
    private int mNoticeContentId;
    private boolean isShowTimeAndIssuer;

    public NoticeList(String mNoticeTitle, String mNoticeIssuer, String mNoticeTime, int mNoticeContentId, boolean isShowTimeAndIssuer) {
        this.mNoticeTitle = mNoticeTitle;
        this.mNoticeIssuer = mNoticeIssuer;
        this.mNoticeTime = mNoticeTime;
        this.mNoticeContentId = mNoticeContentId;
        this.isShowTimeAndIssuer = isShowTimeAndIssuer;
    }

    public String getmNoticeTitle() {
        return mNoticeTitle;
    }

    public void setmNoticeTitle(String mNoticeTitle) {
        this.mNoticeTitle = mNoticeTitle;
    }

    public String getmNoticeIssuer() {
        return mNoticeIssuer;
    }

    public void setmNoticeIssuer(String mNoticeIssuer) {
        this.mNoticeIssuer = mNoticeIssuer;
    }

    public String getmNoticeTime() {
        return mNoticeTime;
    }

    public void setmNoticeTime(String mNoticeTime) {
        this.mNoticeTime = mNoticeTime;
    }

    public int getmNoticeContentId() {
        return mNoticeContentId;
    }

    public void setmNoticeContentId(int mNoticeContentId) {
        this.mNoticeContentId = mNoticeContentId;
    }

    public boolean isShowTimeAndIssuer() {
        return isShowTimeAndIssuer;
    }

    public void setShowTimeAndIssuer(boolean showTimeAndIssuer) {
        isShowTimeAndIssuer = showTimeAndIssuer;
    }
}
