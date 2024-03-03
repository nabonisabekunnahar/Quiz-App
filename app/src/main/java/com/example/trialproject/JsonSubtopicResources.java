package com.example.trialproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonSubtopicResources extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_subtopic_resources);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list=findViewById(R.id.sub_res_list);

        String subtopicName = getIntent().getStringExtra("subtopicName");
        String resourcesArrayString = getIntent().getStringExtra("resourcesArray");

        try {
            JSONArray resourcesArray = new JSONArray(resourcesArrayString);

            ArrayList<String> resourceTitles = new ArrayList<>();
            final ArrayList<String> resourceUrls = new ArrayList<>(); // Keep track of resource URLs

            for (int i = 0; i < resourcesArray.length(); i++) {
                JSONObject resourceObject = resourcesArray.getJSONObject(i);
                String resourceTitle = resourceObject.getString("title");
                String resourceUrl = resourceObject.getString("url");

                resourceTitles.add(resourceTitle);
                resourceUrls.add(resourceUrl);
            }

            ArrayAdapter<String> resourcesAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, resourceTitles);

            list.setAdapter(resourcesAdapter);

            // Handle item clicks to open URLs in a web browser
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = resourceUrls.get(position);
                    openUrlInBrowser(url);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Open a URL in a web browser
    private void openUrlInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }

}