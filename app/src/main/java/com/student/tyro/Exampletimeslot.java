package com.student.tyro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Adapter.TimeSlotsAdapter;
import com.student.tyro.Model.TimeSlot;
import com.student.tyro.Util.ApiCallInterface;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;


public class Exampletimeslot extends AppCompatActivity {
    Button bookslot;
    final String DATE_FORMAT = "yyyy-MM-dd";
    LinearLayout linear, linearnoslots;
    int red = 0;
    int green = 1;
    List<String> redDateList;
    List<String> greenDateList;
    MaterialCalendarView calendarView;
    String User_id;
    public TimeSlotsAdapter timeSlotsAdapter;
    RecyclerView time_slot_recyclerview;
    ArrayList<TimeSlot> timeSlotModels;
    String get_time;
    String setting_date, setting_date_one;
    TextView time_slot_txt;
    Date date1;
    Date date_main;
    String date_two;
    String Time_id = "";
    String Instruct_id = "";
    String strttme = "";
    String endtme = "";
    String total_hrs = "";
    String price = "";
    ImageView back_icon;
    //TextView add_time_slot;
    TextView from_time_slot, to_time_slot;
    String format;
    String formattedDate;
    String bookingdate;

    // ArrayList<TypeOfService>typeOfServices;
    Spinner schedule_selection;
    String type_id = "";

    CalendarView simpleCalendarView;

    String current_date;
    TextView tv_date, tv_avail;
    String instructid, instructfname, instructlname, instructlocation, instructlanguage, instructrating, instructimg, longtude, lat;
    //reschedule data
    String paymentype = "";
    String id, bookinghrs, lat_tude, long_tude;
    NetworkConnection networkConnection;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");
        System.out.println("User_id" + User_id);
        lat_tude = sharedPreferences.getString("lat", "");
        long_tude = sharedPreferences.getString("long", "");
        networkConnection = new NetworkConnection(Exampletimeslot.this);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            instructid = (String) b.get("instruct_id");
            instructfname = (String) b.get("fname");
            instructlname = (String) b.get("lname");
            instructlocation = (String) b.get("location");
            instructlanguage = (String) b.get("language");
            instructrating = (String) b.get("rating");
            instructimg = (String) b.get("image");
            longtude = (String) b.get("longitude");
            lat = (String) b.get("latitude");
            id = (String) b.get("id");
            bookinghrs = (String) b.get("booking_hrs");
            paymentype = (String) b.get("payment_type");
            //Toast.makeText(getApplicationContext(),""+paymentype,Toast.LENGTH_LONG).show();
        }
        time_slot_recyclerview = findViewById(R.id.time_slot_recyclerview);
        time_slot_txt = findViewById(R.id.time_slot_txt);
        tv_date = findViewById(R.id.date);
        tv_avail = findViewById(R.id.txt_avail);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.setDateSelected(CalendarDay.today(), true);
        linear = (LinearLayout) findViewById(R.id.linear);
        bookslot = (Button) findViewById(R.id.btnBookNow);
        back_icon = (ImageView) findViewById(R.id.ivBack);
        linearnoslots = findViewById(R.id.no_slots_linear);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiverTime,
                new IntentFilter("student-timeslot"));

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(CalendarDay.today().getYear(),
                        CalendarDay.today().getMonth(), CalendarDay.today().getDay()))
                .commit();

        bookslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = Exampletimeslot.this.getSharedPreferences("student_details", Context.MODE_PRIVATE);
                bookingdate = sharedPreferences.getString("booking_date", "");
                System.out.println("Button click value" + Time_id + Instruct_id + strttme + endtme + User_id + bookingdate);

                if (strttme.isEmpty() && endtme.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select slot", Toast.LENGTH_LONG).show();
                } else if (paymentype.equals("1")) {

                 /*   System.out.println("Button click value"+id);
                    System.out.println("Button student id"+User_id);
                    System.out.println("Button instructid"+Instruct_id);
                    System.out.println("Button bookinghrs "+bookinghrs);
                    System.out.println("Button  bookingdate"+bookingdate);
                    System.out.println("Button strttme "+strttme);
                    System.out.println("Button endtme "+endtme);
                    System.out.println("Button longtude "+long_tude);
                    System.out.println("Button lat "+lat_tude);*/
                    if (networkConnection.isConnectingToInternet()) {
                        Rescheduleslot();
                    } else {
                        Toast.makeText(Exampletimeslot.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                    }

                } else if (strttme.isEmpty() && endtme.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Please select slot", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(Exampletimeslot.this, AcceptTermsPolicyActivity.class);
                    i.putExtra("student_id", User_id);
                    i.putExtra("instruct_id", Instruct_id);
                    i.putExtra("username", instructfname + " " + instructlname);
                    i.putExtra("location", instructlocation);
                    i.putExtra("language", instructlanguage);
                    i.putExtra("rating", instructrating);
                    i.putExtra("time_id", Time_id);
                    i.putExtra("strttme", strttme);
                    i.putExtra("endtme", endtme);
                    i.putExtra("total_hrs", total_hrs);
                    i.putExtra("price", price);
                    i.putExtra("User_id", User_id);
                    i.putExtra("bookingdate", bookingdate);
                    i.putExtra("image", instructimg);
                    i.putExtra("longitude", longtude);
                    i.putExtra("lat", lat);
                    startActivity(i);
                    finish();
                    // Toast.makeText(getApplicationContext(),"selected slot is"+bookingdate,Toast.LENGTH_LONG).show();
                }

            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (networkConnection.isConnectingToInternet()) {
            get_time_calnder();
        } else {
            Toast.makeText(Exampletimeslot.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


        calendarView.invalidateDecorators();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        String month_name = month_date.format(cal.getTime());
        Log.i("Month name", "" + month_name);
        System.out.println("main_date" + CalendarDay.today());
        String date = "" + CalendarDay.today();
        String date_one = date.replace("{", "");
        System.out.println("main_date" + date_one);
        date_two = date_one.replace("}", "");
        System.out.println("main_date" + date_two);
        get_time_slot(date_two);


        String send_date = date_two.replace("CalendarDay", "");
        System.out.println("maximum_date_main" + send_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date date1;
            date1 = dateFormat.parse(send_date);
            System.out.println("maximum_date" + date1);

            SimpleDateFormat df2 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                df2 = new SimpleDateFormat("EEEE,dd MMM YYYY", Locale.getDefault());
            }
            date1 = new Date();
            setting_date = df2.format(date1);
            System.out.println("Formated_date" + setting_date);
            time_slot_txt.setText(setting_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat df2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df2 = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
        }
        date1 = new Date();
        setting_date = df2.format(date1);
        System.out.println("Formated_date" + setting_date);
        current_date = setting_date;

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                time_slot_txt.setText(" ");
                strttme = "";
                endtme = "";
                String date_o = "" + date;
                String date_one = date_o.replace("{", "");
                date_two = date_one.replace("}", "");
                String send_dateone = date_two.replace("CalendarDay", "");
                System.out.println("send_dateone" + send_dateone);
                get_time_slot(date_two);

                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    outputFormat = new SimpleDateFormat("EEEE,dd MMM YYYY");
                }
                String inputDateStr = send_dateone;
                Date date2 = null;
                try {
                    date2 = inputFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date2);
                time_slot_txt.setText(outputDateStr);
            }

        });
    }


    void setEvent(List<String> dateList, int color) {
        List<LocalDate> localDateList = new ArrayList<>();

        for (String string : dateList) {
            LocalDate calendar = getLocalDate(string);
            if (calendar != null) {
                localDateList.add(calendar);
            }
            Log.i("the localDateList is", "" + localDateList);
        }


        List<CalendarDay> datesLeft = new ArrayList<>();
        List<CalendarDay> datesCenter = new ArrayList<>();
        List<CalendarDay> datesRight = new ArrayList<>();
        List<CalendarDay> datesIndependent = new ArrayList<>();


        for (LocalDate localDate : localDateList) {

            boolean right = false;
            boolean left = false;

            for (LocalDate day1 : localDateList) {


                if (localDate.isEqual(day1.plusDays(1))) {
                    left = true;
                }
                if (day1.isEqual(localDate.plusDays(1))) {
                    right = true;
                }
            }

            if (left && right) {
                datesCenter.add(CalendarDay.from(localDate));
            } else if (left) {
                datesLeft.add(CalendarDay.from(localDate));
            } else if (right) {
                datesRight.add(CalendarDay.from(localDate));
            } else {
                datesIndependent.add(CalendarDay.from(localDate));
            }
        }

        if (color == red) {
            setDecor(datesCenter, R.drawable.g_center);
            setDecor(datesLeft, R.drawable.g_center);
            setDecor(datesRight, R.drawable.g_center);
            setDecor(datesIndependent, R.drawable.g_center);

        } else {
            setDecor(datesCenter, R.drawable.g_independent);
            setDecor(datesLeft, R.drawable.g_independent);
            setDecor(datesRight, R.drawable.g_independent);
            setDecor(datesIndependent, R.drawable.g_independent);
        }
    }

    void setDecor(List<CalendarDay> calendarDayList, int drawable) {
        calendarView.addDecorators(new EventDecorator(Exampletimeslot.this
                , drawable
                , calendarDayList));
    }


    LocalDate getLocalDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            Date input = sdf.parse(date);
            Calendar cal = getInstance();
            cal.setTime(input);
            return LocalDate.of(cal.get(YEAR),
                    cal.get(MONTH) + 1,
                    cal.get(DAY_OF_MONTH));


        } catch (NullPointerException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }


    public void get_time_calnder() {
        //RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), User_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_slot(instructid);

        KProgressHUD hud = KProgressHUD.create(Exampletimeslot.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();
                if (response.isSuccessful()) {
                    Log.e("slots current date", response.body().toString());
                    System.out.println("data is" + response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String chefDate = jsonObject.optString("result");
                            JSONArray jsonArray = new JSONArray(chefDate);
                            redDateList = new ArrayList<>();
                            greenDateList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                // String time_id=jsonObject1.getString("time_id");
                                String date = jsonObject1.getString("date");
                                String sts = jsonObject1.getString("status");
                                Log.i("the sts is", "" + sts);
                                if (sts.equals("Busy")) {
                                    redDateList.add(date);
                                } else {
                                    greenDateList.add(date);
                                }
                               /* if(sts.equals("Busy") || sts.equals("Availble"))
                                {
                                    //redDateList.add(date);
                                    greenDateList.add(date);
                                }
                                else
                                {
                                    redDateList.add(date);
                                   // greenDateList.add(date);
                                }*/
                               /*if(date.equals(current_date))
                                {
                                    redDateList.add(date);
                                }
                                else
                                    {
                                greenDateList.add(date);
                                }*/

                                String current = jsonObject.optString("currentSlots");
                                Log.i("current slot", "" + current);
                                JSONArray jsonArray1 = new JSONArray(current);
                                System.out.println("array is" + jsonArray1);
//                                linearnoslots.setVisibility(View.GONE);
//                                linear.setVisibility(View.VISIBLE);
                                time_slot_recyclerview.setLayoutManager(new GridLayoutManager(Exampletimeslot.this, 2));
                                timeSlotModels = new ArrayList<>();
//                                for (int j = 0; j < jsonArray1.length(); j++) {
//                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//                                    String time_id = jsonObject2.getString("id");
//                                    String sp_id = jsonObject2.getString("instructor_id");
//                                    String date1 = jsonObject2.getString("booking_date");
//                                    String from_time = jsonObject2.getString("start_time");
//                                    String to_time = jsonObject2.getString("end_time");
//                                    String total_hr = jsonObject2.getString("total_hour");
//                                    String price = jsonObject2.getString("price");
//                                    String status_type = jsonObject2.getString("status");
//                                    timeSlotModels.add(new TimeSlot(time_id, sp_id, from_time, to_time, total_hr, price, status_type));
//                                    //  Log.i("the  data is", "" + faqcatgories);
//                                    SharedPreferences sharedPreferences = getSharedPreferences("student_details", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString("booking_date", date1);
//                                    editor.commit();
//                                    editor.apply();
//                                    tv_date.setText(date1);
//                                    tv_avail.setText(status_type);
//                                }
                            }

//                            if (timeSlotModels.size() > 0) {
////                                timeSlotsAdapter = new TimeSlotsAdapter(Exampletimeslot.this, timeSlotModels);
//////                                time_slot_recyclerview.setHasFixedSize(true);
////                                time_slot_recyclerview.setAdapter(timeSlotsAdapter);
//                            } else {
//                                linear.setVisibility(View.GONE);
//                                linearnoslots.setVisibility(View.VISIBLE);
//                            }
                            setEvent(redDateList, red);
                            setEvent(greenDateList, green);
                            Log.i("the redlist is", "" + redDateList);
                            Log.i("the greenlist is", "" + greenDateList);
                        } else {
                            //Toast.makeText(Exampletimeslot.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            linear.setVisibility(View.GONE);
                            linearnoslots.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }

    public void get_time_slot(String date) {

        System.out.println("maximum_date" + date);
        String send_date = date.replace("CalendarDay", "");
        System.out.println("date formate chaged" + send_date);
        if (networkConnection.isConnectingToInternet()) {
            ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
            Call<JsonElement> call = apiClass.get_timeslot(instructid, send_date);
          KProgressHUD  hud = KProgressHUD.create(Exampletimeslot.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setBackgroundColor(R.color.colorPrimary)
                    .show();
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    hud.dismiss();
                    if (response.isSuccessful()) {
                        hud.dismiss();
                        Log.e("spprofileee", response.body().toString());
                        System.out.println("data_image" + response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            int status = jsonObject.getInt("status");
                            if (status == 1) {

                          /*  JSONObject jsonObject2 = new JSONObject("data");
                            System.out.println("data is"+jsonObject2);*/

                                JSONObject jsonObject3 = jsonObject.getJSONObject("data");
                                System.out.println("dataaaaa is" + jsonObject3);

                                String booking = jsonObject3.getString("booking_date");
                                System.out.println("Booking_date is" + booking);

                                String get_timeslots = jsonObject3.optString("slot_data");
                                JSONArray jsonArray = new JSONArray(get_timeslots);
                                System.out.println("slot_data is" + jsonArray);
                                linearnoslots.setVisibility(View.GONE);
                                linear.setVisibility(View.VISIBLE);
                                tv_date.setText(booking);
                                SharedPreferences sharedPreferences = getSharedPreferences("student_details", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("booking_date", booking);
                                editor.commit();
                                editor.apply();
                                tv_avail.setText("Available");
                                // time_slot_recyclerview.setVisibility(View.VISIBLE);
                                time_slot_recyclerview.setLayoutManager(new GridLayoutManager(Exampletimeslot.this, 2));
                                timeSlotModels = new ArrayList<TimeSlot>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String time_id = jsonObject1.getString("id");
                                    String sp_id = jsonObject1.getString("instructor_id");
                                    //String date=jsonObject1.getString("booking_date");
                                    String from_time = jsonObject1.getString("start_time");
                                    String to_time = jsonObject1.getString("end_time");
                                    String total_hr = jsonObject1.getString("total_hour");
                                    String price = jsonObject1.getString("price");
                                    String status_type = jsonObject1.getString("status");

                                    timeSlotModels.add(new TimeSlot(time_id, sp_id, from_time,
                                            to_time, total_hr, price, status_type));
                                }
                                timeSlotsAdapter = new TimeSlotsAdapter(Exampletimeslot.this,
                                        timeSlotModels);
//                                time_slot_recyclerview.setHasFixedSize(true);
                                time_slot_recyclerview.setAdapter(timeSlotsAdapter);
                                hud.dismiss();

                            } else {
                                //Toast.makeText(Exampletimeslot.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                linear.setVisibility(View.GONE);
                                linearnoslots.setVisibility(View.VISIBLE);
                                //time_slot_recyclerview.setVisibility(View.GONE);
                                // timeSlotModels.clear();
                           /* time_slot_recyclerview.setLayoutManager(new GridLayoutManager(Exampletimeslot.this,1));
                            timeSlotsAdapter=new TimeSlotsAdapter(Exampletimeslot.this,timeSlotModels);*/
                                hud.dismiss();

                            }

                            hud.dismiss();

                        } catch (Exception e) {
                            hud.dismiss();

                            e.printStackTrace();
                            Log.e("dskfsdf ", e.toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    hud.dismiss();

                    Log.e("sdfdsd ", t.toString());
                }
            });
        } else {
            Toast.makeText(Exampletimeslot.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public BroadcastReceiver mMessageReceiverTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            // Time_id=""+intent.getExtras().getString("instruct_id");
            Instruct_id = intent.getExtras().getString("instruct_id");
            strttme = intent.getExtras().getString("strt_time");
            endtme = intent.getExtras().getString("end_time");
            total_hrs = intent.getExtras().getString("total_hrs");
            price = intent.getExtras().getString("Price");
            System.out.println("value" + Time_id + Instruct_id + strttme + endtme + price);

        }
    };

    private void Rescheduleslot() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = apiClass.reschedule_slot(id, User_id, Instruct_id, bookinghrs, bookingdate, strttme, endtme, lat_tude, long_tude);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //  progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.optInt("status");
                        String message = jsonObject.optString("message");
                        if (status == 1) {
                            Toast.makeText(Exampletimeslot.this, "" + message, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Exampletimeslot.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Toast.makeText(Exampletimeslot.this, "" + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }/*else {
                    Toast.makeText(activity, ""+response.message(), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //progressDoalog.dismiss();
//                Toast.makeText(Exampletimeslot.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
