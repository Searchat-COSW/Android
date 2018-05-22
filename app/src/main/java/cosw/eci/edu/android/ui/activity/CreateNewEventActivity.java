package cosw.eci.edu.android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cosw.eci.edu.android.Network.Network;
import cosw.eci.edu.android.Network.NetworkException;
import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.data.entities.Lenguage;
import cosw.eci.edu.android.data.entities.User;
import cosw.eci.edu.android.ui.fragment.ListAllFragment;
import cosw.eci.edu.android.ui.fragment.ListOwnedFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateNewEventActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button lenguages;
    private TextView lenguagesValue;
    private String[] listLenaguages;
    private boolean[] listLeanguagesChose;
    private ArrayList<Integer> mLenguagesItems = new ArrayList<>();
    private Context context;
    private FloatingActionButton ubication;
    private TextView ubicationValue;
    private TextView name;
    private TextView description;
    private double longitude;
    private double latitude;
    private ImageView image;
    private TextView price;
    private SupportMapFragment mapFragment;
    private User user;
    private String location;
    private GoogleMap googleMap;
    private FloatingActionButton imageButton;
    private Button saveButton;
    public static final int REQUEST_IMAGE_CAPTURE = 256;
    public static final int SELECT_IMAGE = 285;
    //for camera intent
    private Uri imageUri;
    private final CharSequence[] dialogItems = {"Take picture", "Select picture"};

    private RetrofitNetwork retrofitNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        saveButton = (Button) findViewById(R.id.save_button);
        image = (ImageView) findViewById(R.id.image);
        retrofitNetwork = new RetrofitNetwork();
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
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
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
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
                            if (!mLenguagesItems.contains(index)) {mLenguagesItems.add(index);}

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
                        System.out.println(addresses.get(i).getAddressLine(0).split(",")[1].trim());
                    }*/
                    if (!addresses.isEmpty()) {
                        googleMap.clear();
                        Address exactUbicaion = addresses.get(0);
                        location = exactUbicaion.getAddressLine(0).split(",")[1].trim();
                        longitude = exactUbicaion.getLongitude();
                        latitude = exactUbicaion.getLatitude();
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                //Obtain the object
                user=(User) intent.getBundleExtra(BaseActivity.PASS_USER).getSerializable(BaseActivity.PASS_USER_OBJECT);
                List<Lenguage> lenguagesList = new ArrayList<>();
                for (int i =0 ; i< mLenguagesItems.size();i++){
                    lenguagesList.add(new Lenguage(listLenaguages[mLenguagesItems.get(i)]));
                }
                String[] dateList = textViewDate.getText().toString().split("-");
                String[] timeList = textViewTime.getText().toString().split(":");

                String h = timeList[0].length()==2 ? timeList[0]:"0"+timeList[0];
                String m = timeList[1].length()==2 ? timeList[1]:"0"+timeList[1];

                String activityDate = dateList[2]+"-"+dateList[1]+"-"+dateList[0]+" " + textViewTime.getText().toString();

                Long priceEvent =  new Long(price.getText().toString());


                Event newEvent = new Event(name.getText().toString(),description.getText().toString(),user,lenguagesList,getCleanString(location),formatDate(textViewDate.getText().toString())+" "+formatTime(textViewTime.getText().toString()),new ArrayList<User>(), priceEvent, longitude,latitude,null);
                System.out.println(newEvent);
                retrofitNetwork.createEvent(newEvent, new Network.RequestCallback<Event>() {
                    @Override
                    public void onSuccess(Event response) {
                        ListAllFragment.NEED_TO_UPDATE = true;
                        ListOwnedFragment.NEED_TO_UPDATE = true;
                        finish();
                        //uploading the image
                        try {
                            //create a new file
                            File imageFile = createImageFile();
                            //save the image in the file
                            BitmapDrawable draw = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = draw.getBitmap();
                            FileOutputStream outStream = null;
                            File sdCard = Environment.getExternalStorageDirectory();
                            outStream = new FileOutputStream(imageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                            // create RequestBody instance from file
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("uploaded_file", imageFile.getName(), requestFile);
                            retrofitNetwork.updateImagEvent(response.getId(),body, new Network.RequestCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean response) {
                                    Intent intent = new Intent(context, BaseActivity.class);
                                    //Start the new activity using the intent.
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailed(NetworkException e) {
                                    finish();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(NetworkException e) {
                        System.out.println("ERROR");
                        finish();
                    }
                });
            }
        });

    }


    private String getCleanString(String cityLocation) {
        cityLocation = Normalizer.normalize(cityLocation, Normalizer.Form.NFD);
        cityLocation = cityLocation.replaceAll("[^\\p{ASCII}]", "");
        cityLocation = cityLocation.toLowerCase();

        return cityLocation;
    }

    private String formatDate(String date){
        String[] splitted = date.trim().split("-");
        String ans ="";
        ans+=  splitted[0].length()==1? "0"+splitted[0]:splitted[0];
        ans+="-";
        ans+=  splitted[1].length()==1? "0"+splitted[1]:splitted[1];
        ans+="-";
        ans+=splitted[2];
        return ans;
    }

    private String formatTime(String time){
        String[] splitted = time.trim().split(":");
        String ans ="";
        ans+=  splitted[0].length()==1? "0"+splitted[0]:splitted[0];
        ans+=":";
        ans+=  splitted[1].length()==1? "0"+splitted[1]:splitted[1];
        return ans;
    }

    public int toInt(String s){
        return Integer.parseInt(s);
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
