package com.seenetuvastaja.seenetuvastaja;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.seenetuvastaja.seenetuvastaja.model.Mushroom;
import com.seenetuvastaja.seenetuvastaja.model.MushroomAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {

    private ListView predictionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        predictionList = findViewById(R.id.predictionList);
    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            Bundle b = getIntent().getExtras();
            if(b != null) {
                /*
                Abiks võetud kood lehtedelt:
                https://stackoverflow.com/questions/6681217/help-passing-an-arraylist-of-objects-to-a-new-activity
                https://abhiandroid.com/ui/adapter
                 */
                ArrayList<Mushroom> mushrooms = b.getParcelableArrayList("mushrooms");
                float[] probabilities = b.getFloatArray("probabilities");
                MushroomAdapter adapter = new MushroomAdapter(this, mushrooms, probabilities);
                predictionList.setAdapter(adapter);
                Log.i("Result", "Set prediction!");
            }

        } catch (Exception e) {
            String errorMessage = getResources().getString(R.string.result_exception);
            Toast.makeText(ResultActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            Log.i("Result", "Error loading results!");
        }
    }

}
