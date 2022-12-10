package com.example.emg.employee.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toolbar;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.emg.MainActivity;
import com.example.emg.R;
import com.example.emg.base.EmployeeBase;
import com.example.emg.model.Attendance;
import com.example.emg.model.LeaveRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EmployeeCalendar extends EmployeeBase {
    CalendarView calendarView;
    List<Calendar> list_calendars = new ArrayList<>();
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    DatabaseReference database;
    List<EventDay> events = new ArrayList<>();
    String green = "#00FF00";
    String blue = "#0C6CDF";
    TableLayout tableLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        tableLayout = findViewById(R.id.leave_table);
        calendarView = (CalendarView) findViewById(R.id.calendar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        changeHeaderImage(navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.employeeNavigation(navigationView,drawerLayout,this);
//        list_calendars.add(calendar);
        populateHoliday();
        getLeaves();
//        events.add(new EventDay(testing, R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
//        events.add(new EventDay(calendar, R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
    }
    private void getLeaves() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Leave").child(auth.getCurrentUser().getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getValue(LeaveRequest.class).status.equals("APPROVE")){
                        LeaveRequest leaveRequest = dataSnapshot.getValue(LeaveRequest.class);
//                        events.add(new EventDay(stringToCalendar(leaveRequest.leave_date_from), R.drawable.ic_baseline_file_copy_24, Color.parseColor(blue)));
//                        events.add(new EventDay(stringToCalendar(leaveRequest.leave_date_to), R.drawable.ic_baseline_file_copy_24, Color.parseColor(blue)));
                        stringToCalendarMany(leaveRequest.leave_date_to,leaveRequest.leave_date_from);
                        addRow(leaveRequest.leave_type,leaveRequest.leave_date_from,leaveRequest.leave_date_to);
                    }
                }

                calendarView.setEvents(events);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void stringToCalendarMany(String date_to,String date_from)  {
//        Calendar from = Calendar.getInstance();
//        Calendar to = Calendar.getInstance();
          Date from;
          Date to;
        SimpleDateFormat df = new SimpleDateFormat("MM/d/yyyy", Locale.getDefault());
        df.format(new Date());
        try {
              from = df.parse(date_from);
              to = df.parse(date_to);
              Long diff = from.getTime() - to.getTime();
              diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
              events.add(new EventDay(stringToCalendar(date_from), R.drawable.ic_baseline_file_copy_24, Color.parseColor(blue)));
              events.add(new EventDay(stringToCalendar(date_to), R.drawable.ic_baseline_file_copy_24, Color.parseColor(blue)));
//            Calendar temp = stringToCalendar(date_from);
//            temp.add(Calendar.DATE, 1);

              for(int x=1;x<=Math.abs(diff);x++){
                  Calendar cal = Calendar.getInstance();
                  cal.setTime(df.parse(date_from));
                  cal.add(Calendar.DATE, x);
                  events.add(new EventDay(cal, R.drawable.ic_baseline_file_copy_24, Color.parseColor(blue)));
              }
//            from.setTime(df.parse(date_from));
//            to.setTime(df.parse(date_from));// all done
//            long diff = from.getTime() - to.getTime();
//            Long diff_days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//            from = df.getCalendar();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Calendar stringToCalendar(String date)  {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/d/yyyy", Locale.getDefault());
        df.format(new Date());
        try {
            cal.setTime(df.parse(date));// all done
            cal = df.getCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
    private void populateHoliday(){
        events.add(new EventDay(stringToCalendar("12/8/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
        events.add(new EventDay(stringToCalendar("12/22/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
        events.add(new EventDay(stringToCalendar("12/24/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
        events.add(new EventDay(stringToCalendar("12/25/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
        events.add(new EventDay(stringToCalendar("12/30/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
        events.add(new EventDay(stringToCalendar("12/31/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
        events.add(new EventDay(stringToCalendar("1/1/2023"), R.drawable.ic_baseline_celebration_24, Color.parseColor(green)));
//        events.add(new EventDay(stringToCalendar("12/15/2022"), R.drawable.ic_baseline_celebration_24, Color.parseColor(blue)));
    }
    private void addRow(String leaveType,String date,String date2){
        Date from;
        Date to;
        SimpleDateFormat df = new SimpleDateFormat("MM/d/yyyy", Locale.getDefault());
        df.format(new Date());
        Long diff;
        try {
            from = df.parse(date);
            to = df.parse(date2);
            diff = from.getTime() - to.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            diff = Math.abs(diff)+1;
            TableRow row = new TableRow(this);
            TextView type = new TextView(this);
            type.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,4f));
            type.setText(leaveType);
            type.setTextColor(Color.BLACK);
            type.setGravity(Gravity.CENTER_HORIZONTAL);
            type.setPadding(10,10,10,10);
            TextView days = new TextView(this);
            days.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,2f));
            days.setText(diff.toString());
            days.setGravity(Gravity.CENTER_HORIZONTAL);
            days.setPadding(10,10,10,10);
            TextView tv_date = new TextView(this);
            tv_date.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,4f));
            tv_date.setText(date);
            tv_date.setTextColor(Color.BLACK);
            tv_date.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_date.setPadding(10,10,10,10);
            row.addView(type);
            row.addView(tv_date);
            row.addView(days);
            tableLayout.addView(row);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}