package com.minlu.office_system.holder;

import android.view.View;
import android.widget.TextView;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;

/**
 * Created by user on 2017/3/28.
 */

public class NoticeInformHolder extends BaseHolder {

    private TextView noticeInformText;

    @Override
    public View initView() {

        View inflate = ViewsUitls.inflate(R.layout.item_notice_inform);
        noticeInformText = (TextView) inflate.findViewById(R.id.item_notice_inform_text);

        return inflate;
    }

    @Override
    public void setRelfshData(Object mData, int postion) {
    }
}
