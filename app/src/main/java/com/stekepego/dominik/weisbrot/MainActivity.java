package com.stekepego.dominik.weisbrot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<AudioClip> clipData;
    private AudioClipAdapter audioClipAdapter;
    private ListView clipList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clipData = new ArrayList<>();

        setClipData();

        System.out.print("Größe von clipdata:");
        System.out.println(clipData.size());

        for (int i = 0; i < clipData.size(); i++){
            System.out.println(clipData.get(i).category);
            System.out.println(clipData.get(i).clipName);
            System.out.println(clipData.get(i).fileName + "\n");
        }

        audioClipAdapter = new AudioClipAdapter(this, R.layout.list_item, clipData);
        clipList = (ListView)findViewById(R.id.listView);
        clipList.setAdapter(audioClipAdapter);

    }

    private void setClipData(){
        // Reading json file from assets folder
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "trainer_ludwig.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String myjsonstring = sb.toString();
        // Try to parse JSON
        try {
            // Creating JSONObject from String
            JSONObject jsonObjMain = new JSONObject(myjsonstring);

            // Creating JSONArray from JSONObject
            JSONArray jsonArray = jsonObjMain.getJSONArray("clips");
            String currentCategory = jsonObjMain.getString("category");

            // JSONArray has x JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {

                // Creating JSONObject from JSONArray
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String currentFileName = jsonObj.getString("fileName");
                String currentClipName = jsonObj.getString("clipName");

                AudioClip audioClip = new AudioClip(currentFileName, currentClipName, currentCategory);

                int resId = this.getResources().getIdentifier(audioClip.fileName, "raw", this.getPackageName());

                if(resId != 0) {
                    clipData.add(audioClip);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(clipData, new ClipComparator());

    }
}

class ClipComparator implements Comparator<AudioClip> {

    @Override
    public int compare(AudioClip c1, AudioClip c2){
        if(c1.category.compareTo(c2.category) != 0)
            return c1.category.compareTo((c2.category));

        if(c1.clipName.compareTo(c2.clipName) != 0)
            return c1.clipName.compareTo(c2.clipName);

        return 0;
    }
}