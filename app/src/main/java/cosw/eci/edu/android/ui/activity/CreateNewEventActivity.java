package cosw.eci.edu.android.ui.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cosw.eci.edu.android.R;

public class CreateNewEventActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button lenguages;
    private TextView lenguagesValue;
    private String[] listLenaguages;
    private boolean[] listLeanguagesChose;
    ArrayList<Integer> mLenguagesItems = new ArrayList<>();
    private Context context;
    private FloatingActionButton ubication;
    private TextView ubicationValue;
    private ImageView image;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private FloatingActionButton imageButton;
    public static final int REQUEST_IMAGE_CAPTURE = 256;
    public static final int SELECT_IMAGE = 285;
    //for camera intent
    private Uri imageUri;
    private final CharSequence[] dialogItems = {"Take picture", "Select picture"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        image = (ImageView) findViewById(R.id.image);
        imageButton = (FloatingActionButton) findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddPhoto(view);
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final Button pickDate = (Button) findViewById(R.id.pick_date);
        final Button pickTime = (Button) findViewById(R.id.pick_time);
        lenguages = (Button) findViewById(R.id.lenguages);
        lenguagesValue = (TextView) findViewById(R.id.lenguages_value);
        ubication = (FloatingActionButton) findViewById(R.id.ubication_button);
        ubicationValue = (TextView) findViewById(R.id.ubication);
        context = this;
        final TextView textViewDate = (TextView) findViewById(R.id.date);
        final TextView textViewTime = (TextView) findViewById(R.id.time);


        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                textViewTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, mHour, mMinute, true);
                tpd.show();
            }
        });

        listLenaguages = getResources().getStringArray(R.array.lenguages);
        listLeanguagesChose = new boolean[listLenaguages.length];

        lenguages = (Button) findViewById(R.id.lenguages);

        lenguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pick your lenguages");

                builder.setMultiChoiceItems(listLenaguages, listLeanguagesChose, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index, boolean isChecked) {

                        if (isChecked) {
                            if (!mLenguagesItems.contains(index)) mLenguagesItems.add(index);
                            else {
                                try {
                                    mLenguagesItems.remove(index);
                                } catch (Exception e) {

                                }
                            }
                        }

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = "";
                        mLenguagesItems = new ArrayList<>();
                        for (int j = 0; j < listLeanguagesChose.length; j++) {
                            if (listLeanguagesChose[j]) {
                                item = item + listLenaguages[j] + ", ";
                                mLenguagesItems.add(j);
                            }
                        }
                        lenguagesValue.setText(item.substring(0, item.length() - 2));
                    }
                });
                builder.show();
            }
        });

        ubication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geo = new Geocoder(context);
                try {
                    List<Address> addresses = geo.getFromLocationName(ubicationValue.getText().toString(), 1);
                    /*for (int i=0; i<addresses.size();i++){
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! "+i+" "+addresses.get(i));
                    }*/
                    if (!addresses.isEmpty()) {
                        googleMap.clear();
                        Address exactUbicaion = addresses.get(0);
                        System.out.println("Lugar "+ exactUbicaion);
                        Location targetLocation = new Location(ubicationValue.getText().toString());//provider name is unnecessary
                        targetLocation.setLatitude(exactUbicaion.getLatitude());
                        targetLocation.setLongitude(exactUbicaion.getLongitude());
                        addMarkerAndZoom(targetLocation, ubicationValue.getText().toString(), 15);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void addMarkerAndZoom(Location location, String title, int zoom) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myLocation).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
    }

    public void onClickAddPhoto(View v) {

        final DialogInterface.OnClickListener selectedListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //take picture
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            dispatchTakePictureIntent();
                        }
                        break;

                    case 1:
                        //select a picture
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                        break;
                }
                dialog.dismiss();
            }
        };

        createSingleChoiceAlertDialog(context, "Select option", dialogItems, selectedListener, null).show();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @NonNull
    public static Dialog createSingleChoiceAlertDialog(@NonNull Context context, @Nullable String title,
                                                       @NonNull CharSequence[] items,
                                                       @NonNull DialogInterface.OnClickListener optionSelectedListener,
                                                       @Nullable DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme);
        builder.setItems(items, optionSelectedListener);
        if (cancelListener != null) {
            builder.setNegativeButton(R.string.cancel, cancelListener);
        }
        builder.setTitle(title);
        return builder.create();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " " + resultCode + " ");
        switch (requestCode) {
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    image.setImageURI(null);
                    image.setImageURI(imageUri);

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    image.setImageURI(null);
                    image.setImageURI(imageUri);

                }
                break;
        }

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
        this.googleMap = googleMap;
    }

}
