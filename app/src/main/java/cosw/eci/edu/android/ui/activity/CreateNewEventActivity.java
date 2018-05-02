package cosw.eci.edu.android.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import cosw.eci.edu.android.R;

public class CreateNewEventActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button lenguages;
    private TextView lenguagesValue;
    private String[] listLenaguages;
    private boolean[] listLeanguagesChose;
    ArrayList<Integer> mLenguagesItems = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        final Button pickDate = (Button) findViewById(R.id.pick_date);
        final Button pickTime = (Button) findViewById(R.id.pick_time);
        lenguages = (Button) findViewById(R.id.lenguages);
        lenguagesValue = (TextView) findViewById(R.id.lenguages_value);
        context=this;
        final TextView textViewDate = (TextView) findViewById(R.id.date);
        final TextView textViewTime = (TextView) findViewById(R.id.time);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textViewDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                myCalendar.set(Calendar.HOUR, hour);
                myCalendar.set(Calendar.MINUTE, min);
                String myFormat = "HH-mm"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textViewTime.setText(sdf.format(myCalendar.getTime()));
            }
        };

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(CreateNewEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear, mMonth, mDay);

                                textViewDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(CreateNewEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            int callCount = 0;   //To track number of calls to onTimeSet()
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                            {
                                textViewTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, mHour, mMinute, true);
                tpd.show();
            }
        });

        listLenaguages =  getResources().getStringArray(R.array.lenguages);
        listLeanguagesChose = new boolean[listLenaguages.length];

        lenguages  = (Button) findViewById(R.id.lenguages);

        lenguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pick your lenguages");

                builder.setMultiChoiceItems(listLenaguages, listLeanguagesChose, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index, boolean isChecked) {

                        if(isChecked){
                            if(!mLenguagesItems.contains(index)) mLenguagesItems.add(index);
                            else {
                                try{
                                    mLenguagesItems.remove(index);
                                }catch (Exception e){

                                }
                            }
                        }

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = "";
                        mLenguagesItems =  new ArrayList<>();
                        for (int j=0;j<listLeanguagesChose.length;j++){
                            if (listLeanguagesChose[j]){
                                item = item + listLenaguages[j]+", ";
                                mLenguagesItems.add(j);
                            }
                        }
                        lenguagesValue.setText(item.substring(0,item.length()-2));
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
