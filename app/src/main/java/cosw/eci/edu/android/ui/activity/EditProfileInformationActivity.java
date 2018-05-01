package cosw.eci.edu.android.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.FitWindowsViewGroup;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cosw.eci.edu.android.Network.Network;
import cosw.eci.edu.android.Network.NetworkException;
import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Lenguage;
import cosw.eci.edu.android.data.entities.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileInformationActivity extends AppCompatActivity {

    private Button nationality;
    private Button lenguages;
    private Button saveButton;
    private FloatingActionButton imageButton;
    private TextView name;
    private TextView lastName;
    private TextView nationalityValue;
    private TextView lenguagesValue;
    private TextView aboutYou;
    private ImageView image;
    private Context context;
    private String[] listLenaguages;
    private boolean[] listLeanguagesChose;
    ArrayList<Integer> mLenguagesItems = new ArrayList<>();

    public static final int REQUEST_IMAGE_CAPTURE = 256;
    public static final int SELECT_IMAGE= 285;
    //public static final int RESULT_OK = 785;

    //for camera intent
    private Uri imageUri;
    private final CharSequence[] dialogItems = {"Take picture", "Select picture"};

    //User
    User user;

    private RetrofitNetwork retrofitNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_information);

        context=this;
        retrofitNetwork = new RetrofitNetwork();
        Intent intent = getIntent();
        //Obtain the object
        user=(User) intent.getBundleExtra(BaseActivity.PASS_USER).getSerializable(BaseActivity.PASS_USER_OBJECT);

        retrofitNetwork.getUser(user.getUsername(), new Network.RequestCallback<User>() {
            @Override
            public void onSuccess(User response) {
                user = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initComponents();
                    }
                });


            }

            @Override
            public void onFailed(NetworkException e){

                finish();
            }
        });

    }

    private void initComponents() {
        image = (ImageView) findViewById(R.id.image);
        imageButton = (FloatingActionButton) findViewById(R.id.image_button);
        saveButton = (Button) findViewById(R.id.save_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddPhoto(view);
            }
        });
        Picasso.with(context).load(RetrofitNetwork.BASE_URL+"user/"+user.getUsername()+"/image").into(image, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
                image.setImageResource(R.drawable.no_user);
            }
        });

        name = (TextView) findViewById(R.id.name);
        lastName = (TextView) findViewById(R.id.last_name);
        nationalityValue = (TextView) findViewById(R.id.nationality_value);
        lenguagesValue = (TextView) findViewById(R.id.lenguages_value);

        aboutYou = (TextView) findViewById(R.id.about_you);
        name.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        aboutYou.setText(user.getProfileInformation().getAboutYou());

        // NATIONALITY

        String value = user.getProfileInformation().getNationality()==null || user.getProfileInformation().getNationality().equals("") ? "Choose..." : user.getProfileInformation().getNationality();
        nationalityValue.setText(value);


        nationality  = (Button) findViewById(R.id.nationality);
        nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence countries[] = new CharSequence[] {"Colombia","United States","France","Italy","Germany","Japan","Russia","United Kingdom","Argentina","Mexico","Brazil"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pick a country");

                builder.setItems(countries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nationalityValue.setText(countries[which]);
                    }
                });
                builder.show();
            }
        });

        // LENGUAGES

        listLenaguages =  getResources().getStringArray(R.array.lenguages);
        listLeanguagesChose = new boolean[listLenaguages.length];

        lenguages  = (Button) findViewById(R.id.lenguages);

        String lenguagesString="";

        if (user.getProfileInformation().getLanguages().isEmpty()){
            lenguagesString = "Choose...";
        }
        else{
            for(int k=0;k<user.getProfileInformation().getLanguages().size();k++){
                lenguagesString = lenguagesString + user.getProfileInformation().getLanguages().get(k).getLenguage();
                if (k!= user.getProfileInformation().getLanguages().size()-1) lenguagesString = lenguagesString + ",";

                if (Arrays.asList(listLenaguages).contains(user.getProfileInformation().getLanguages().get(k).getLenguage())){
                    listLeanguagesChose[Arrays.asList(listLenaguages).indexOf(user.getProfileInformation().getLanguages().get(k).getLenguage())] = true;
                    mLenguagesItems.add(Arrays.asList(listLenaguages).indexOf(user.getProfileInformation().getLanguages().get(k).getLenguage()));
                }
            }
        }

        lenguagesValue.setText(lenguagesString);

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setFirstname(name.getText().toString());
                user.setLastname(lastName.getText().toString());
                user.getProfileInformation().setAboutYou(aboutYou.getText().toString());
                if(!nationalityValue.getText().toString().equals("Choose..."))user.getProfileInformation().setNationality(nationalityValue.getText().toString());
                List<Lenguage> lenguagesToSend = new ArrayList<>();
                for(int i =0 ; i< mLenguagesItems.size();i++){
                    lenguagesToSend.add(new Lenguage(listLenaguages[mLenguagesItems.get(i)]));
                }

                user.getProfileInformation().setLanguages(lenguagesToSend);

                System.out.println(user + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                retrofitNetwork.updateUser(user, new Network.RequestCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        finish();
                    }

                    @Override
                    public void onFailed(NetworkException e) {
                        finish();//for the moment
                    }
                });
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

                    retrofitNetwork.updateImageUser(user.getUsername(), body, new Network.RequestCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean response) {
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
        });
    }

    public void onClickAddPhoto(View v){

        final DialogInterface.OnClickListener selectedListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //take picture
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                            dispatchTakePictureIntent();
                        }
                        break;

                    case 1:
                        //select a picture
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
                        break;
                }
                dialog.dismiss();
            }
        };

        createSingleChoiceAlertDialog(context ,"Select option", dialogItems, selectedListener,null).show();
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
                                                       @Nullable DialogInterface.OnClickListener cancelListener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( context, R.style.AppTheme );
        builder.setItems( items, optionSelectedListener );
        if ( cancelListener != null )
        {
            builder.setNegativeButton( R.string.cancel, cancelListener );
        }
        builder.setTitle( title );
        return builder.create();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " "+ resultCode + " ");
        switch(requestCode){
            case SELECT_IMAGE:
                if(resultCode==RESULT_OK){
                    imageUri = data.getData();
                    image.setImageURI(null);
                    image.setImageURI(imageUri);

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode==RESULT_OK){
                    image.setImageURI(null);
                    image.setImageURI(imageUri);

                }
                break;
        }
    }


}
