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
                ArrayList<Float> suitedProbs = new ArrayList<>();
                for (float prob : probabilities) {
                    if (prob > 0.01f) suitedProbs.add(prob);
                }
                ArrayList<Mushroom> suitedShrooms = new ArrayList<>();
                for (int i = 0; i < suitedProbs.size(); i++) {
                    suitedShrooms.add(mushrooms.get(i));
                }
                float[] finalProbs = new float[suitedProbs.size()];
                for (int i = 0; i < suitedProbs.size(); i++) {
                    finalProbs[i] = suitedProbs.get(i);
                }
                MushroomAdapter adapter = new MushroomAdapter(this, suitedShrooms, finalProbs);
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
