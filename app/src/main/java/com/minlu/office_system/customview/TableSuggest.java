package com.minlu.office_system.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.CountersignSuggestBean;

import java.util.List;


public class TableSuggest extends LinearLayout {

    private String mTableTitleText;
    private String mTableFirstLieText;
    private String mTableSecondLieText;
    private String mTableThirdLieText;
    private LinearLayout mTableSuggestView;

    public TableSuggest(Context context) {
        super(context);
    }

    public TableSuggest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TableSuggest(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TableSuggest);

        mTableTitleText = attributes.getString(R.styleable.TableSuggest_table_title);
        mTableFirstLieText = attributes.getString(R.styleable.TableSuggest_table_first_lie_name);
        mTableSecondLieText = attributes.getString(R.styleable.TableSuggest_table_second_lie_name);
        mTableThirdLieText = attributes.getString(R.styleable.TableSuggest_table_third_lie_name);

        // 使用了本方法，最后必须调用下面的这个方法
        attributes.recycle();

        initView(context);
    }

    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.custom_table_suggest, this);
        TextView title = (TextView) inflate.findViewById(R.id.tv_table_suggest_title);
        title.setText(mTableTitleText);
        TextView firstLie = (TextView) inflate.findViewById(R.id.tv_table_suggest_view_first_lie);
        firstLie.setText(mTableFirstLieText);
        TextView secondLie = (TextView) inflate.findViewById(R.id.tv_table_suggest_view_second_lie);
        secondLie.setText(mTableSecondLieText);
        TextView thirdLie = (TextView) inflate.findViewById(R.id.tv_table_suggest_view_third_lie);
        thirdLie.setText(mTableThirdLieText);
        mTableSuggestView = (LinearLayout) inflate.findViewById(R.id.ll_table_suggest_view);
    }

    public void addTableData(List<CountersignSuggestBean> data) {
        if (data.size() > 0) {
            this.setVisibility(VISIBLE);
            for (CountersignSuggestBean recordEndSuggestBean : data) {
                View inflate = ViewsUitls.inflate(R.layout.item_record_end_step_suggest);
                TextView name = (TextView) inflate.findViewById(R.id.item_record_end_step_suggest_name);
                name.setText(recordEndSuggestBean.getSuggestName());
                TextView time = (TextView) inflate.findViewById(R.id.item_record_end_step_suggest_time);
                time.setText(recordEndSuggestBean.getSuggestTime());
                TextView idea = (TextView) inflate.findViewById(R.id.item_record_end_step_suggest_idea);
                idea.setText(recordEndSuggestBean.getSuggestContent());
                mTableSuggestView.addView(inflate);
            }
        } else {
            this.setVisibility(GONE);
        }
    }
}
