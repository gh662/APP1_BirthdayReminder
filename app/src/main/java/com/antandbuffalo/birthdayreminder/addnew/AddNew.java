package com.antandbuffalo.birthdayreminder.addnew;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.antandbuffalo.birthdayreminder.Constants;
import com.antandbuffalo.birthdayreminder.DateOfBirth;
import com.antandbuffalo.birthdayreminder.R;
import com.antandbuffalo.birthdayreminder.Util;
import com.antandbuffalo.birthdayreminder.database.DateOfBirthDBHelper;

import java.util.Calendar;
import java.util.Date;

public class AddNew extends Activity {
    EditText name;
    DatePicker datePicker;
    Intent intent = null;
    TextView selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new);

        selectedDate = (TextView)findViewById(R.id.selectedDate);
        selectedDate.setText(Util.getStringFromDate(new Date(), Constants.ADD_NEW_DATE_FORMAT));
        name = (EditText)findViewById(R.id.personName);
        datePicker = (DatePicker)findViewById(R.id.perosnDateOfBirth);
        datePicker.setMaxDate(new Date().getTime());

        datePicker.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.setText(Util.getStringFromDate(Util.getDateFromString(year + "-" + (month + 1) + "-" + dayOfMonth), Constants.ADD_NEW_DATE_FORMAT));
            }
        });

        intent = new Intent();
        intent.putExtra(Constants.IS_USER_ADDED, Constants.FLAG_FAILURE.toString());
        setResult(RESULT_OK, intent);

        Button save = (Button)findViewById(R.id.save);
        save.setBackgroundResource(R.drawable.save_button);

        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setBackgroundResource(R.drawable.cancel_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("inside on click");
                String plainName = name.getText().toString().trim();
                Calendar cal = Util.getCalendar();
                cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                Date plainDate = cal.getTime();

                if(plainName.equalsIgnoreCase("")) {
                    //show error
                }
                else {
                    DateOfBirth dob = new DateOfBirth();
                    dob.setName(plainName);
                    dob.setDobDate(plainDate);

                    DateOfBirthDBHelper.insertDOB(dob);
                    System.out.println("Inserted successfully");

                    Toast toast = Toast.makeText(getApplicationContext(), Constants.NOTIFICATION_ADD_MEMBER_SUCCESS, Toast.LENGTH_SHORT);
                    toast.show();
                    intent.putExtra(Constants.IS_USER_ADDED, Constants.FLAG_SUCCESS.toString());
                    setResult(RESULT_OK, intent);
                    clearInputs();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void clearInputs() {
        name.setText("");
    }
}