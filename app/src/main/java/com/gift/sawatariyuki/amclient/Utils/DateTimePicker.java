package com.gift.sawatariyuki.amclient.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.gift.sawatariyuki.amclient.AddEventActivity;

import java.util.Calendar;

/***
 * 时间日期选择器
 */
public class DateTimePicker {
    /***
     * 当仅选择
     * @param timeET_1 点击后弹出时间日期选择器的EditText
     * @param timeET_2 另一个需要填写时间日期的EditText
     * @param length 需要填写时长的EditText
     */
    public static void GetDateTimePicker(final EditText timeET_1, final EditText timeET_2, final EditText length, final Context context){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                calendar.set(year, month, dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                        String time = (String) DateFormat.format("yyyy-MM-dd HH:mm", calendar);
                        timeET_1.setText(time);
                        if ("".equals(timeET_2.getText().toString())) {
                            timeET_2.setText(time);
                        }
                        if ("".equals(length.getText().toString())) {
                            length.setText("0");
                        }
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static void GetDatePicker(final EditText editText, final Context context){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                String time = (String) DateFormat.format("yyyy-MM-dd", calendar);
                editText.setText(time);
                editText.setSelection(time.length());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
