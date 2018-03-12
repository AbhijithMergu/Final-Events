package com.it.acumen.acumeneventslocal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, Game> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, Game>();

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

      //  listAdapter.notifyDataSetChanged();
        expListView.setAdapter(listAdapter);

        Button newGame = (Button) findViewById(R.id.add_game);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,QRCodeScanActivity.class);
                i.putExtra("requestCode",2);
                startActivityForResult(i,2);
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {


        // Adding child data
        listDataHeader.add("737-101 \t Sravya");
        listDataHeader.add("737-073 \t Krushi");
        listDataHeader.add("737-314 \t Bhavani");


        List<PlayerDetails> playerDetails = new ArrayList<>();
        playerDetails.add(new PlayerDetails("player1","Abhijith"));
        listDataChild.put(listDataHeader.get(0),new Game("12345",playerDetails));

        playerDetails = new ArrayList<>();
        playerDetails.add(new PlayerDetails("player2","Abhijith2"));
        listDataChild.put(listDataHeader.get(1),new Game("13454",playerDetails));

        playerDetails = new ArrayList<>();
        playerDetails.add(new PlayerDetails("player3","Abhijith3"));
        listDataChild.put(listDataHeader.get(2),new Game("0876666",playerDetails));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            listAdapter.onActivityResult(requestCode, resultCode, data);
        else if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("Result");
//                Toast.makeText(this,"Result :"+result,Toast.LENGTH_LONG).show();

                listDataHeader.add(result);
                List<PlayerDetails> playerDetails = new ArrayList<>();
                playerDetails.add(new PlayerDetails("player4","Abhijith4"));
                listDataChild.put(result,new Game("12345",playerDetails));
                listAdapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do not Exit!");
        builder.setMessage("Are you sure you want to exit?\n     (You may lose data)");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // String name = _listDataHeader.get(gPosition);
                        //_listDataHeader.set(gPosition,"Submitted");
                        MainActivity.super.onBackPressed();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
       // super.onBackPressed();
    }
}