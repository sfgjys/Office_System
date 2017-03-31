package com.minlu.office_system.fragment.form.formPremise;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;
import com.minlu.office_system.http.OkHttpMethod;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

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
    public void setWhichViewShowListPopupWindow(View anchorView, final List<String> date, final ShowListPopupItemClickListener clickListener, Context context) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight((date.size() > 3) ? ViewsUitls.dpToPx(200) : ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setAnchorView(anchorView);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        listPopupWindow.setModal(true);//设置是否是模式
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, date));

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


    /*
    * 将需要展示时间选择对话框的EditTextTimeSelector控件传入进行具体操作代码
    * */
    public void setEditTextOnClickShowTimePicker(final EditTextTimeSelector editTextTimeSelector) {
        editTextTimeSelector.setDayOrTimeOnClickListener(new EditTextTimeSelector.DayOrTimeOnClickListener() {
            @Override
            public void onTimeClick(View v) { // 点击24h的事件
                // 展示24h时间选择对话框
                showTimePickerDialog(new TimePickerFragment.SetTimeListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 点击对话框确认的方法回调
                        // TODO 点击时间选择对话框的时候设置String时间,需要注意格式,还有下面的
                        editTextTimeSelector.setTimeOfDayText(StringUtils.lessThanNineConvertString(hourOfDay) + ":" + StringUtils.lessThanNineConvertString(minute));
                    }
                });
            }

            @Override
            public void onDayClick(View v) {// 点击年月日的事件
                // 展示年月日时间选择对话框
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {// 点击对话框确认的方法回调
                        long selectTime = TimeTool.textTimeToLongTime(year + "-" + (month + 1) + "-" + dayOfMonth, "yyyy-MM-dd");
                        long nowTime = TimeTool.textTimeToLongTime(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH), "yyyy-MM-dd");
                        if (selectTime < nowTime) {// 选择的日期小于现在的是不可能的，要复原
                            year = Calendar.getInstance().get(Calendar.YEAR);
                            month = Calendar.getInstance().get(Calendar.MONTH);
                            dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        }
                        editTextTimeSelector.setDayOfYearText(year + "-" + (StringUtils.lessThanNineConvertString(month + 1)) + "-" + StringUtils.lessThanNineConvertString(dayOfMonth));
                    }
                });
            }
        });
    }


    public Response requestFormListItemDetail() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_PROCESS_ID, ""));
        hashMap.put("orderId", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_ORDER_ID, ""));
        hashMap.put("taskId", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_TASK_ID, ""));
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        return OkHttpMethod.synPostRequest(IpFiled.FORM_LIST_ITEM_DETAIL, hashMap);
    }

}
