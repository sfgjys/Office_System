package com.minlu.office_system.fragment.form.formPremise;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.util.List;

public abstract class FormFragment extends BaseFragment {

    public interface ShowListPopupItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);

        void onAnchorViewClick(View v);

        void onListPopupDismiss();
    }

    public abstract void disAgreeOnClick(View v);

    public abstract void agreeOnClick(View v);

    public abstract void submitOnClick(View v);

    /*
    * 展示时间选择对话框，参数为点击时间选择对话框的确认后的监听回调
    * */
    public void showTimePickerDialog(TimePickerFragment.SetTimeListener setTimeListener) {
        DialogFragment timePickerFragment = new TimePickerFragment(setTimeListener);
        timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    /*
    * 展示日期选择对话框，参数为点击日期选择对话框的确认后的监听回调
    * */
    public void showDatePickerDialog(DatePickerFragment.SetDateListener setDateListener) {
        DialogFragment datePickerFragment = new DatePickerFragment(setDateListener);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    /*
    * 在参数一控件下，展示参数二集合中的文本数据，参数三是自定义点击文本条目的监听事件
    * */
    public void showListPopupWindow(View anchorView, final List<String> date, final ShowListPopupItemClickListener clickListener) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, date));
        listPopupWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight(ViewsUitls.dpToPx(200));
        listPopupWindow.setAnchorView(anchorView);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        listPopupWindow.setModal(true);//设置是否是模式
        anchorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow.show();
                clickListener.onAnchorViewClick(v);
            }
        });
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickListener.onItemClick(parent, view, position, id);
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clickListener.onListPopupDismiss();
            }
        });
    }

    /*
    * 设置背景颜色变暗 参数0.0f~1.0f
    * */
    public void setBackGroundDarkColor(float alpha) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = alpha;
        getActivity().getWindow().setAttributes(layoutParams);
    }
}
