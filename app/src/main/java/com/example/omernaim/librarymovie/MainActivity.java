package com.example.omernaim.librarymovie;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView myLV;
    //ArrayList<myMovies>myMoviesArrayList;
    //ArrayAdapter<myMovies>myMoviesArrayAdapter;

    Button addBtn;
    Cursor cursor;
    SQLhelper mySql;
   // SimpleCursorAdapter simpleCursorAdapter;

    int currentPosition;
CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            adapter = new CustomAdapter(this, cursor);
        myLV = (ListView)findViewById(R.id.myLV);
        mySql = new SQLhelper(this);
        cursor = mySql.getReadableDatabase().query(DBconstant.tablename_db,null,null,null,null,null,null);
        String [] myCulom =new String[] {DBconstant.moviename_db};
      //  int [] myText = new int[]{android.R.id.text1};
        int [] myText = new int[]{R.id.mycustumTV};
      //  simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,cursor, myCulom, myText);
        adapter = new CustomAdapter(this, cursor);
      //  simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.my_layout,cursor, myCulom, myText);
    //    myLV.setAdapter(simpleCursorAdapter);
        myLV.setAdapter(adapter);
        myLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(DBconstant.moviename_db, cursor.getString(cursor.getColumnIndex(DBconstant.moviename_db)));
                intent.putExtra(DBconstant.idname_db, cursor.getInt(cursor.getColumnIndex(DBconstant.idname_db)));
                intent.putExtra(DBconstant.description_db, cursor.getString(cursor.getColumnIndex(DBconstant.description_db)));
                intent.putExtra(DBconstant.url_db, cursor.getString(cursor.getColumnIndex(DBconstant.url_db)));
                DBconstant.isthisfirst = false;
                startActivity(intent);
            }
        });

        DBconstant.Imdbz=false;
        DBconstant.isthisfirst = true;
        registerForContextMenu(myLV);
        addBtn = (Button)findViewById(R.id.plusBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, addBtn);

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.witch_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.manualMenu){
                            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                            startActivity(intent);
                        }
                        else if(item.getItemId()==R.id.internetMenu){
                            Intent intent = new Intent(MainActivity.this, InternetActivity.class);
                            startActivity(intent);
                        }
                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        currentPosition= ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        getMenuInflater().inflate(R.menu.longclicked_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMenu:
              finish();
                break;
            case R.id.exitMenu:AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("THIS WILL DELETE ALL THE MOVIES !!!!")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mySql.getWritableDatabase().delete(DBconstant.tablename_db,null,null);
                              //  simpleCursorAdapter.swapCursor(cursor);
                                onResume();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("KEEP ALL MOVIES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alert.setTitle("WARNING");
                alert.create().show();

            //   mySql.getWritableDatabase().delete(DBconstant.tablename_db,null,null);
                onResume();
        }
        return true;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editMenu:
                cursor.moveToPosition(currentPosition);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(DBconstant.moviename_db, cursor.getString(cursor.getColumnIndex(DBconstant.moviename_db)));
                intent.putExtra(DBconstant.idname_db, cursor.getInt(cursor.getColumnIndex(DBconstant.idname_db)));
                intent.putExtra(DBconstant.description_db, cursor.getString(cursor.getColumnIndex(DBconstant.description_db)));
                DBconstant.isthisfirst = false;
                startActivity(intent);


                break;
            case R.id.deleteMenu:AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("THIS WILL DELETE THE MOVIE !!!!")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cursor.moveToPosition(currentPosition);
                                int id=cursor.getInt(cursor.getColumnIndex(DBconstant.idname_db));
                                String[] wherearg=new String[]{""+id};
                                mySql.getWritableDatabase().delete(DBconstant.tablename_db,"_id=?",wherearg);
                                cursor=   mySql.getReadableDatabase().query(DBconstant.tablename_db,null,null,null,null,null,null);
                               // simpleCursorAdapter.swapCursor(cursor);
                                  adapter.swapCursor(cursor);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("KEEP MISSION", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alert.setTitle("WARNING");
                alert.create().show();


                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        cursor = mySql.getReadableDatabase().query(DBconstant.tablename_db,null,null,null,null,null,null);
        adapter.swapCursor(cursor);
        super.onResume();
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


}

