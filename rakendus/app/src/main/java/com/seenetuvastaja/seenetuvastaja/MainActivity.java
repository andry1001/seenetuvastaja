package com.seenetuvastaja.seenetuvastaja;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.seenetuvastaja.seenetuvastaja.model.Classifier;
import com.seenetuvastaja.seenetuvastaja.model.Mushroom;
import com.seenetuvastaja.seenetuvastaja.model.MushroomDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CHOOSE = 2;

    static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    private Button takePictureButton;
    private Button choosePictureButton;

    private MushroomDAO mushDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mushDAO = new MushroomDAO();

        takePictureButton = findViewById(R.id.takePicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(v);
            }
        });
        choosePictureButton = findViewById(R.id.choosePicture);
        choosePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(v);
            }
        });
    }

    public void runAsyncIdentification(final Bitmap image) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                runIdentification(image);
            }
        });
    }

    public void runIdentification(Bitmap image) {
        try {
            final float[][] output = Classifier.classify(image, MainActivity.this);
            int[] top5Indexes = Classifier.getTopNIndexes(output, 5);
            final float[] top5Probabilities = Classifier.getTopNProbabilities(output, 5);
            final List<Mushroom> top5Mushrooms = mushDAO.getById(top5Indexes);
            Log.i("Prediction: ", Arrays.toString(output[0]));
            /*
            Abiks võetud kood lehelt:
            https://stackoverflow.com/questions/6681217/help-passing-an-arraylist-of-objects-to-a-new-activity
             */
            runOnUiThread(new Runnable() {
                public void run() {
                    Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList("mushrooms", (ArrayList) top5Mushrooms);
                    b.putFloatArray("probabilities", top5Probabilities);
                    resultIntent.putExtras(b);
                    startActivity(resultIntent);
                }
            });
        } catch (IOException e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    String errorMessage = getResources().getString(R.string.model_loading_ioexception);
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            final Bitmap image = (Bitmap) extras.get("data");
            runAsyncIdentification(image);
        }
        else if (requestCode == REQUEST_IMAGE_CHOOSE  && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                final Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                runAsyncIdentification(image);
            } catch(FileNotFoundException e) {
                String errorMessage = getResources().getString(R.string.file_not_found_exception);
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                String errorMessage = getResources().getString(R.string.unidentified_exception);
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void takePicture(View view) {
        if (checkPermission(Manifest.permission.CAMERA)) {
            activateTakePictureIntent();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public void activateTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void choosePicture(View view) {
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            activateChoosePictureIntent();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void activateChoosePictureIntent() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseImageIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(chooseImageIntent, "Vali Pilt"), REQUEST_IMAGE_CHOOSE);
    }

    public boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else return true;

    }

}
