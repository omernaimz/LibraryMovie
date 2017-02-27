package com.example.omernaim.librarymovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InternetActivity extends AppCompatActivity {
    TextView searchTV;
    EditText searchET;
    Button cancelBtn, searchBtn;
    ArrayList<myMovies>myMoviesArrayList;
    ArrayAdapter<myMovies> myMoviesArrayAdapter;
    ListView internetLV;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        searchTV = (TextView) findViewById(R.id.searchTV);
        searchET = (EditText) findViewById(R.id.searchET);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        internetLV = (ListView)findViewById(R.id.internetLV);
        internetLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              DBconstant.Imdbz= true;
               myMovies movie=  myMoviesArrayList.get(i);
            Intent intent = new Intent(InternetActivity.this, AddEditActivity.class);
                intent.putExtra("kaki",movie.IMDB);
                intent.putExtra("shilshol", movie.name);
                intent.putExtra("theUrl",movie.Url);

            startActivity(intent);

        }
        });
        myMoviesArrayList = new ArrayList<>();
        myMoviesArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,myMoviesArrayList);
        internetLV.setAdapter(myMoviesArrayAdapter);

        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchET= (EditText) findViewById(R.id.searchET);


                DownloadWebsite downloadWebsite= new DownloadWebsite();


                try {
                    String toSpace = searchET.getText().toString();
                    String toSpaceUrl = URLEncoder.encode(toSpace,"UTF-8");
                    downloadWebsite.execute("http://omdbapi.com/?s="+toSpaceUrl);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //downloadWebsite.execute("http://omdbapi.com/?s="+searchET.getText().toString());

            }
        });







    }
    public class DownloadWebsite extends AsyncTask<String, Long, String> {


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(InternetActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder response = null;

            try {
                URL website = new URL(params[0]);

                URLConnection connection = website.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();

            } catch (Exception ee) {

                ee.printStackTrace();
            }

            return response.toString();
        }


        protected void onPostExecute(String resultJSON) {
            try {
                JSONObject mainObject = new JSONObject(resultJSON);


                JSONArray resultsArray = mainObject.getJSONArray("Search");
                Log.d("dsd", "dsds");

                myMoviesArrayList.clear();

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject currentObject = resultsArray.getJSONObject(i);
                    String name = currentObject.getString("Title");
                    String Url = currentObject.getString("Poster");
                    String IMDB = currentObject.getString("imdbID");

                    myMovies aNewMovie = new myMovies(name, Url, IMDB);
                    myMoviesArrayList.add(aNewMovie);

                }

                myMoviesArrayAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}










