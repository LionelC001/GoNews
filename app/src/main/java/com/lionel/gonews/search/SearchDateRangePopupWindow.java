package com.lionel.gonews.search;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.base.BasePopupWindow;
import com.lionel.gonews.util.DateConvertManager;

public class SearchDateRangePopupWindow extends BasePopupWindow {

    private TextView txtDateRangeFrom;
    private TextView txtDateRangeTo;
    private View viewPopupDate;
    private String oldValueTo, oldValueFrom;

    public interface IDateRangeCallback {
        void onDateRangeSelected(String from, String to);
    }

    private final IDateRangeCallback callback;
    private final Context context;

    public SearchDateRangePopupWindow(Context context, IDateRangeCallback callback) {
        super(context);

        this.context = context;
        this.callback = callback;

        initView();
        initDateTextView();
        initValue();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        viewPopupDate = inflater.inflate(R.layout.layout_popup_date, null, false);
        super.setView(viewPopupDate);
    }

    private void initDateTextView() {
        txtDateRangeFrom = viewPopupDate.findViewById(R.id.txtDateRangeFrom);
        txtDateRangeTo = viewPopupDate.findViewById(R.id.txtDateFromTo);
        txtDateRangeTo.setText(DateConvertManager.getTodayInStr());

        txtDateRangeFrom.setOnClickListener(new DatePickerHandler(txtDateRangeFrom));
        txtDateRangeTo.setOnClickListener(new DatePickerHandler(txtDateRangeTo));
    }

    private void initValue() {
        oldValueFrom = txtDateRangeFrom.getText().toString();
        oldValueTo = txtDateRangeTo.getText().toString();
    }

    public void show(View anchor){
        super.show(anchor);
    }

    @Override
    public void dismiss() {
        if (checkIsDateChanged()) {
            callback.onDateRangeSelected(txtDateRangeFrom.getText().toString(), txtDateRangeTo.getText().toString());
        }
        super.dismiss();
    }

    private boolean checkIsDateChanged() {
        boolean isValueChanged = false;
        String valueFrom = txtDateRangeFrom.getText().toString();
        String valueTo = txtDateRangeTo.getText().toString();

        if (!oldValueFrom.equals(valueFrom)) {
            oldValueFrom = valueFrom;
            isValueChanged = true;
        }

        if (!oldValueTo.equals(valueTo)) {
            oldValueTo = valueTo;
            isValueChanged = true;
        }

        return isValueChanged;
    }


    private class DatePickerHandler implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

        private final TextView targetView;
        private DatePickerDialog datePickDialog;

        private DatePickerHandler(TextView targetView) {
            this.targetView = targetView;

            initDatePickDialog();
        }

        private void initDatePickDialog() {
            int[] today = DateConvertManager.getTodayInArray();
            datePickDialog = new DatePickerDialog(context,
                    this,
                    today[0], today[1], today[2]);
        }

        @Override
        public void onClick(View v) {
            updateDatePickerDate();
            datePickDialog.show();
        }

        private void updateDatePickerDate() {
            if (targetView.getText() != null && !targetView.getText().toString().equals("")) {
                String txtDate = targetView.getText().toString();
                int[] dateIntArray = DateConvertManager.yyMMDDToIntArray(txtDate);
                datePickDialog.updateDate(dateIntArray[0], dateIntArray[1], dateIntArray[2]);
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            targetView.setText(DateConvertManager.intsToYYMMDD(year, month, dayOfMonth));
        }
    }
}
