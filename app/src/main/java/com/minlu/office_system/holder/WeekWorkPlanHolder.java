package com.minlu.office_system.holder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.WeekWorkEachData;

/**
 * Created by user on 2017/5/23.
 */

public class WeekWorkPlanHolder extends BaseHolder<WeekWorkEachData> {

    private EditText mWorkContent;
    private int mSelectPosition;

    @Override
    public View initView() {
        View inflate = ViewsUitls.inflate(R.layout.item_week_work_plan);

        mWorkContent = (EditText) inflate.findViewById(R.id.week_work_plan_item_work_content);

        return inflate;
    }

    @Override
    public void setRelfshData(final WeekWorkEachData mData, final int postion) {

        mWorkContent.setText(mData.getWorkContent() + "~~~" + postion);
        mWorkContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSelectPosition = postion;
                return false;
            }
        });
        mWorkContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSelectPosition == postion) {
                    System.out.println("afterTextChanged: " + postion);
                }
            }
        });
    }
}
