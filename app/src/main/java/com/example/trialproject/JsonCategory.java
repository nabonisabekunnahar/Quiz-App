package com.example.trialproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonCategory extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_category);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list=findViewById(R.id.cat_list);
        extractData();
    }

    public void extractData()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="https://api.myjson.online/v1/records/7604e208-1a6a-4c40-9a79-c4e6318891f4";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                jsonParse(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    public void jsonParse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray categoryArray = dataObject.getJSONArray("category");

            ArrayList<String> names = new ArrayList<String>();
            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryObject = categoryArray.getJSONObject(i);
                String categoryName = categoryObject.getString("name");
                names.add(categoryName);
            }

            ArrayAdapter arrayAdapter=new ArrayAdapter(this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,names);

            list.setAdapter(arrayAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject selectedCategory = categoryArray.optJSONObject(position);

                    if (selectedCategory != null) {
                        // Debug: Log the selected category JSON
                        Log.d("SelectedCategory", selectedCategory.toString());

                        Intent intent = new Intent(JsonCategory.this, JsonSubtopics.class);
                        intent.putExtra("selectedCategory", selectedCategory.toString());
                        startActivity(intent);
                    } else {
                        // Handle the case where the selected category is null or doesn't contain subtopics
                        Toast.makeText(JsonCategory.this, "Invalid category", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}