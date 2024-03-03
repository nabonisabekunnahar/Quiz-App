package com.example.trialproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonSubtopics extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView list;
    private JSONArray subtopicsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_subtopics);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list=findViewById(R.id.sub_list);

        String selectedCategoryJson = getIntent().getStringExtra("selectedCategory");

        try {
            JSONObject category = new JSONObject(selectedCategoryJson);
            subtopicsArray = category.getJSONArray("subtopics");

            ArrayList<String> subtopicNames = new ArrayList<>();

            for (int i = 0; i < subtopicsArray.length(); i++) {
                JSONObject subtopicObject = subtopicsArray.getJSONObject(i);
                String subtopicName = subtopicObject.getString("name");
                subtopicNames.add(subtopicName);
            }

            ArrayAdapter<String> subtopicsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, subtopicNames);

            list.setAdapter(subtopicsAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        JSONObject selectedSubtopic = subtopicsArray.getJSONObject(position);

                        // Get information about the selected subtopic
                        String subtopicName = selectedSubtopic.getString("name");
                        JSONArray resourcesArray = selectedSubtopic.getJSONArray("resources");

                        // Create an Intent to start a new activity (SubtopicResourcesActivity)
                        Intent intent = new Intent(JsonSubtopics.this, JsonSubtopicResources.class);

                        // Pass relevant information to the new activity
                        intent.putExtra("subtopicName", subtopicName);
                        intent.putExtra("resourcesArray", resourcesArray.toString());

                        // Start the new activity
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}