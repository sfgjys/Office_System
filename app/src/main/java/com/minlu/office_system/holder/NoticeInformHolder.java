package com.minlu.office_system.holder;

import android.view.View;
import android.widget.TextView;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.NoticeList;

/**
 * Created by user on 2017/3/28.
 */

public class NoticeInformHolder extends BaseHolder<NoticeList> {

    private TextView noticeInformText;
    private TextView noticeInformIssuer;
    private TextView noticeInformTime;
    private View noticeInformBottomLine;

    @Override
    public View initView() {

        View inflate = ViewsUitls.inflate(R.layout.item_notice_inform);
        noticeInformText = (TextView) inflate.findViewById(R.id.item_notice_inform_title);
        noticeInformIssuer = (TextView) inflate.findViewById(R.id.item_notice_inform_issuer);
        noticeInformTime = (TextView) inflate.findViewById(R.id.item_notice_inform_time);
        noticeInformBottomLine = inflate.findViewById(R.id.item_notice_inform_bottom_line);

        return inflate;
    }

    @Override
    public void setRelfshData(NoticeList mData, int postion) {
        noticeInformText.setText(mData.getmNoticeTitle());
        if (mData.isShowTimeAndIssuer()) {
            noticeInformTime.setVisibility(View.VISIBLE);
            noticeInformTime.setText(mData.getmNoticeTime());
            noticeInformIssuer.setVisibility(View.VISIBLE);
            noticeInformIssuer.setText("(" + mData.getmNoticeIssuer() + ")");
            noticeInformBottomLine.setVisibility(View.VISIBLE);
        } else {
            noticeInformTime.setVisibility(View.GONE);
            noticeInformIssuer.setVisibility(View.GONE);
            noticeInformBottomLine.setVisibility(View.GONE);
        }
    }
}
