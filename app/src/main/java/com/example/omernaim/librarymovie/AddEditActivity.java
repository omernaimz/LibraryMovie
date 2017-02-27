package com.example.omernaim.librarymovie;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AddEditActivity extends AppCompatActivity {
EditText subjET, bodyET, urlET;
    Button okBtn, cancelBtn, showBtn;
    TextView subTV, bodyTV, urlTV;
    ImageView imageView;
    ArrayList<myMovies> myMoviesArrayList;
    ArrayAdapter<myMovies> myMoviesArrayAdapter;
    SQLhelper mysql;
    Cursor cursor;
    boolean existed = false;
    int id;
    String theStringBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        subjET = (EditText)findViewById(R.id.subjET);
        bodyET = (EditText)findViewById(R.id.bodyET);
        urlET = (EditText)findViewById(R.id.urlET);
        okBtn = (Button)findViewById(R.id.okBtn);
        showBtn = (Button)findViewById(R.id.showUrlBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        subTV = (TextView)findViewById(R.id.subjTV);
        bodyTV = (TextView)findViewById(R.id.bodyTV);
         urlTV = (TextView)findViewById(R.id.URLTV);
        imageView = (ImageView)findViewById(R.id.imageView);
        myMoviesArrayList = new ArrayList<>();
        myMoviesArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,myMoviesArrayList);
        mysql = new SQLhelper(this);
        cursor =mysql.getReadableDatabase().query(DBconstant.tablename_db,null,null,null,null,null,null);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(DBconstant.Imdbz==true){
            Intent intent = getIntent();
            String theIMDB =  intent.getStringExtra("kaki");
            String theName = intent.getStringExtra("shilshol");
            String theUrl = intent.getStringExtra("theUrl");
            subjET.setText(theName);
            bodyET.setText(theIMDB);
            urlET.setText(theUrl);

            DownloadBody downloadBody = new DownloadBody();
            downloadBody.execute("http://omdbapi.com/?i="+theIMDB);
            ///triyng to put all details in
         //   DBconstant.Imdbz=false;
            DBconstant.isthisfirst=true;
        }
        if(DBconstant.isthisfirst ==false) {
            Intent intent = getIntent();
            id = intent.getIntExtra(DBconstant.idname_db, 0);
            String theDESC = intent.getStringExtra(DBconstant.description_db);
            String theNamez = intent.getStringExtra(DBconstant.moviename_db);
            String theUrlz = intent.getStringExtra(DBconstant.url_db);
            subjET.setText(theNamez);
            bodyET.setText(theDESC);
            urlET.setText(theUrlz);
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(DBconstant.isthisfirst==true) {
                    subjET = (EditText) findViewById(R.id.subjET);
                    bodyET = (EditText) findViewById(R.id.bodyET);
                    urlET = (EditText) findViewById(R.id.urlET);

                    String theName = subjET.getText().toString();
                    String theDesc = bodyET.getText().toString();
                    String theUrl = urlET.getText().toString();
                    while (cursor.moveToNext()) {
                        if (theName.equals(cursor.getString(cursor.getColumnIndex(DBconstant.moviename_db)))) {
                            //Toast.makeText(AddEditActivity.this, "this movie is exist", Toast.LENGTH_SHORT).show();
                            existed = true;
                           // break;
                        }
                    }
                       if(existed == false){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBconstant.moviename_db, theName);
                            contentValues.put(DBconstant.description_db, theDesc);
                            contentValues.put(DBconstant.url_db, theUrl);
                           contentValues.put(DBconstant.bitmap64, theStringBitmap);
                            mysql.getWritableDatabase().insert(DBconstant.tablename_db, null, contentValues);
                            Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    if(existed==true){
                        Toast.makeText(AddEditActivity.this, "this movie is exist", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    }

                else if (DBconstant.isthisfirst==false){


                    String nsmET = subjET.getText().toString();
                    String todoET = bodyET.getText().toString();

                    //need to put here the option to do without a spaces with trim().


                    if(theStringBitmap== null) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBconstant.moviename_db, nsmET);
                        contentValues.put(DBconstant.description_db, todoET);
                        mysql.getWritableDatabase().update(DBconstant.tablename_db,contentValues,"_id=?",new String[]{""+id});
                        DBconstant.isthisfirst=true;
                    }
                    else{
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageBitmap(decodeBase64(theStringBitmap));
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBconstant.moviename_db, nsmET);
                        contentValues.put(DBconstant.description_db, todoET);
                        contentValues.put(DBconstant.bitmap64,theStringBitmap );
                        mysql.getWritableDatabase().update(DBconstant.tablename_db,contentValues,"_id=?",new String[]{""+id});
                        DBconstant.isthisfirst=true;
                    }

                 finish();
                }

            }
        });




    showBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         buttonClick(imageView);

        }
    });

    }
    public void buttonClick (View image)
    {
        String imageUrl= ((EditText) findViewById(R.id.urlET)).getText().toString();
        DownloadImgeTask downloadImgeTask= new DownloadImgeTask();
        downloadImgeTask.execute(imageUrl);

    }



    public class DownloadBody extends AsyncTask<String, Long, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if (DBconstant.Imdbz==true) {
                progressDialog = new ProgressDialog(AddEditActivity.this);
                progressDialog.setTitle("searching body");
                progressDialog.show();
                super.onPreExecute();
            }
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
            JSONObject mainObject = null;
            try {
                mainObject =new JSONObject(resultJSON);
                 String body = mainObject.getString("Plot");
                bodyET.setText(body);
                progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

            DBconstant.Imdbz=false;

        }
    }

    class DownloadImgeTask extends AsyncTask<String, Void, Bitmap> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AddEditActivity.this);
            dialog.setTitle("connecting");
            dialog.setMessage("please wait...");
            dialog.setCancelable(true);
            dialog.show();
            Log.d("pre", "exe");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap= null;

            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);
                // open a connection
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = (InputStream) url.getContent();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap downloadedImage) {
            Bitmap bitmap=downloadedImage;
            imageView.setImageBitmap(downloadedImage);
           theStringBitmap= encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,100);
            Log.d("cds","dsds");

            dialog.dismiss();

        }
    }
    public  String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



}


