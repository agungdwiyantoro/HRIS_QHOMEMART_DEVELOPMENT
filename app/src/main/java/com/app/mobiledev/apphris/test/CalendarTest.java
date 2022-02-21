package com.app.mobiledev.apphris.test;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mobiledev.apphris.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarTest extends AppCompatActivity {

    TextView tvDateSelected, tvGetDate;
    Button btnDate, btnOpen;

    ArrayList<String> list = new ArrayList<String>();
    LinearLayout llViewCalendar, llViewGetDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);

        tvDateSelected = findViewById(R.id.tvDateSelected);
        tvGetDate = findViewById(R.id.tvGetDate);

        btnDate = findViewById(R.id.btnGetDates);
        btnOpen = findViewById(R.id.btnOpen);

        Calendar prevMonth = Calendar.getInstance();

        prevMonth.add(Calendar.MONTH, -1);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(prevMonth.getTime(), today)
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(Date date) {

                SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :"+clearFormatDate);

                Toast.makeText(CalendarTest.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: "+clearFormatDate);
                //tvDateSelected.setText(clearFormatDate);

                list.add(clearFormatDate);

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateUnselected(Date date) {
                SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :"+clearFormatDate);

                Toast.makeText(CalendarTest.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: "+clearFormatDate);
                //tvDateSelected.setText(clearFormatDate);

                list.remove(clearFormatDate);
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setVisibility(View.VISIBLE);
                llViewCalendar.setVisibility(View.VISIBLE);
                llViewGetDates.setVisibility(View.VISIBLE);
            }
        });

        btnDate.setOnClickListener(v -> {
            tvDateSelected.setText(list.toString().replace("[","" ).replace("]",""));
            tvGetDate.setText(list.toString().replace("[","" ).replace("]",""));
            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
            Log.d("TAG_DATE_ARRAY", "onCreate: "+list.toString().replace("[","" ).replace("]",""));
        });


    }
}