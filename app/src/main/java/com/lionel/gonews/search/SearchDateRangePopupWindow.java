package com.lionel.gonews.search;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.base.BasePopupWindow;

import java.util.Calendar;

public class SearchDateRangePopupWindow extends BasePopupWindow {

    private TextView txtDateRangeFrom;
    private TextView txtDateRangeTo;
    private View viewPopupDate;

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
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        viewPopupDate = inflater.inflate(R.layout.layout_popup_date, null, false);
        super.setView(viewPopupDate);
    }

    private void initDateTextView() {
        txtDateRangeFrom = viewPopupDate.findViewById(R.id.txtDateRangeFrom);
        txtDateRangeTo = viewPopupDate.findViewById(R.id.txtDateFromTo);

        txtDateRangeFrom.setOnClickListener(new DatePickerHandler(txtDateRangeFrom));
        txtDateRangeTo.setOnClickListener(new DatePickerHandler(txtDateRangeTo));
    }

    @Override
    public void dismiss() {
        callback.onDateRangeSelected(txtDateRangeFrom.getText().toString(), txtDateRangeTo.getText().toString());
        super.dismiss();
    }

    private class DatePickerHandler implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

        private final TextView targetView;
        private DatePickerDialog datePickDialog;

        private DatePickerHandler(TextView targetView) {
            this.targetView = targetView;

            initDatePickDialog();
        }

        private void initDatePickDialog() {
            Calendar calendar = Calendar.getInstance();
            datePickDialog = new DatePickerDialog(context,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        }


        @Override
        public void onClick(View v) {
            updateDatePickerDate();
            datePickDialog.show();
        }

        private void updateDatePickerDate() {

            if (targetView.getText() != null && !targetView.getText().toString().equals("")) {
                String txtDate = targetView.getText().toString();
                String[] arrayTxtDate = txtDate.split("/");
                datePickDialog.updateDate(
                        parseStringToInt(arrayTxtDate[0]),
                        parseStringToInt(arrayTxtDate[1]),
                        parseStringToInt(arrayTxtDate[2]));
            }
        }

        private int parseStringToInt(String str) {
            return Integer.parseInt(str);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            targetView.setText(year + "/" + month + "/" + dayOfMonth);
        }
    }
}
