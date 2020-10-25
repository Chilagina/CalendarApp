package com.example.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class NewCalendarActivity extends AppCompatActivity {

    CalendarView newCalendar;
    TextView myDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calendar);

        newCalendar = (CalendarView) findViewById(R.id.newCalendarView);
        myDate = (TextView) findViewById(R.id.myDate);

        newCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                String date = (i1 + 1) + "/" + i2 + "/" + i;
                myDate.setText(date);

            }
        });
    }
}