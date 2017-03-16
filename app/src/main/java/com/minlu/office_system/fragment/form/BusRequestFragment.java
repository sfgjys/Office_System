package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TimePicker;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/7.
 */
public class BusRequestFragment extends FormFragment {

    private ArrayList<String> excessive;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        // 因为本fragment是通过R.id.sv_replace_form控件replace开启的，但是R.id.sv_replace_form控件是居中属性，所以再次我们要使得居中属性去除
        FormActivity formActivity = (FormActivity) getContext();
        if (formActivity != null) {
            formActivity.setScrollViewNoGravity();
        }

        final ArrayList<String> busNumberData = new ArrayList<>();
        busNumberData.add("苏D G23098F");
        busNumberData.add("苏D N2435U9");
        busNumberData.add("苏D F2398F2");
        busNumberData.add("苏D 23RF124");
        busNumberData.add("苏D SE380AS");
        busNumberData.add("苏D SD20342");
        busNumberData.add("苏D MRE45OI");
        busNumberData.add("苏D G23098F");
        busNumberData.add("苏D N2435U9");
        busNumberData.add("苏D F2398F2");

        View inflate = ViewsUitls.inflate(R.layout.form_bus_request);
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_bus_request_title);
        EditTextItem office = (EditTextItem) inflate.findViewById(R.id.form_bus_request_office);
        EditTextItem busNumber = (EditTextItem) inflate.findViewById(R.id.form_bus_request_bus_number);
        final EditText busNumberEditText = busNumber.getCustomEditTextRight();
        setWhichViewShowListPopupWindow(busNumberEditText, busNumberData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                busNumberEditText.setText(busNumberData.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.7f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        }, getActivity());

        EditTextItem startTime = (EditTextItem) inflate.findViewById(R.id.form_bus_request_start_time);
        final EditText startTimeEditText = startTime.getCustomEditTextRight();
        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(new TimePickerFragment.SetTimeListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        String hour = (hourOfDay >= 0 && hourOfDay <= 9) ? ("0" + hourOfDay) : (hourOfDay + "");
                        String minute = (minuteOfDay >= 0 && minuteOfDay <= 9) ? ("0" + minuteOfDay) : (minuteOfDay + "");
                        startTimeEditText.setText(hour + " : " + minute);
                    }
                });
            }
        });

        EditTextItem destination = (EditTextItem) inflate.findViewById(R.id.form_bus_request_destination);
        EditTextItem cause = (EditTextItem) inflate.findViewById(R.id.form_bus_request_cause);

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), busNumber.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), destination.getCustomEditTextLeft());

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        excessive = new ArrayList<>();
        excessive.add("excessive");
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {
        System.out.println("BusRequestFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("BusRequestFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("BusRequestFragment-submitOnClick");
    }
}