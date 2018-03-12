package com.it.acumen.acumeneventslocal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class ExpandableListAdapter extends BaseExpandableListAdapter implements AsyncResponse {
    private String result;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private ArrayList<ViewGroup> parentList = new ArrayList<ViewGroup>();
    private HashMap<String, Game> _listDataChild;
    private final int minDelta = 300;           // threshold in ms
    private long focusTime = 0;                 // time of last touch
    private View focusTarget = null;
    SendPostRequest asyncTask;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String,Game> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.result = null;


    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Game childItem = (Game) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        final View csView = convertView;

        Toast.makeText(_context,"Group Position: "+groupPosition+"Number : "+childItem.getPlayerList().size(),Toast.LENGTH_SHORT).show();

        final TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        if(childItem.getStatus() == 1)
        {
            csView.setBackgroundColor(Color.parseColor("#9E9E9E"));
            String Name = childItem.getPlayerList().get(0).getPlayerName().substring(1);
            txtListChild.setText(childItem.getPlayerList().get(0).getPlayerName());

        }


//        String gameName = childItem.getGameId()+"\t"+childItem.getPlayerList().get(0).getPlayerName();
        else {
            csView.setBackgroundColor(Color.WHITE);
            txtListChild.setText(childItem.getPlayerList().get(0).getPlayerName());
        }
        Button addPlayer = (Button) convertView.findViewById(R.id.AddPlayer);
        if(((Game)getChild(groupPosition,0)).getPlayerList().size()==1)
        {

            final LinearLayout mainLL2 = (LinearLayout) csView.findViewById(R.id.mainlinear);
            ((ViewGroup) mainLL2).removeAllViews();
        }
        else
        {
            final LinearLayout mainLL = (LinearLayout) csView.findViewById(R.id.mainlinear);
            ((ViewGroup) mainLL).removeAllViews();
            for(int k=1;k< ((Game)getChild(groupPosition,0)).getPlayerList().size();k++)
            {
                final LinearLayout ll = new LinearLayout(_context);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams but = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button remove = new Button(_context);
                remove.setText("x");
                remove.setLayoutParams(params);

                TextView newPlayer = new TextView(_context);
                newPlayer.setText(((Game)getChild(groupPosition,0)).getPlayerList().get(k).getPlayerName());
                but.weight = 1.0f;
                but.setMargins(40,10,0,0);
                newPlayer.setLayoutParams(but);
                ll.addView(newPlayer);
                ll.addView(remove);
                mainLL.addView(ll);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLL.removeView(ll);
                    }
                });
            }
        }
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                txtListChild.setText(newPlayer.getText().toString());
//                String result = txtListChild.getText().toString();
//                result = result + "\n" + newPlayer.getText().toString();
//
//                txtListChild.setText(result);
                 Intent i = new Intent(_context, QRCodeScanActivity.class);
                 Activity origin = (Activity) _context;
                 i.putExtra("requestCode",1);
                 i.putExtra("groupPosition",groupPosition);
                 i.putExtra("childPosition",childPosition);
                 i.putExtra("gameId",childItem.getGameId());
                 origin.startActivityForResult(i, 1);


            }
        });

        Button update = (Button) convertView.findViewById(R.id.update);
        final View cView = convertView;

        final EditText R1 = (EditText) cView.findViewById(R.id.roundone);
        final EditText R2 = (EditText) cView.findViewById(R.id.roundtwo);
        final EditText R3 = (EditText) cView.findViewById(R.id.roundthree);
        if(childItem.getRound1Score()!=0)
            R1.setText(String.valueOf(childItem.getRound1Score()));
        else
            R1.setText("");
        if(childItem.getRound2Score()!=0)
            R2.setText(String.valueOf(childItem.getRound2Score()));
        else
            R2.setText("");

        if(childItem.getRound3Score()!=0)
            R3.setText(String.valueOf(childItem.getRound3Score()));
        else
            R3.setText("");

        if(childItem.getRound1Score()!=0) {
            R1.setEnabled(false);
        }
        else
            R1.setEnabled(true);
        if(childItem.getRound2Score()!=0){
            R2.setEnabled(false);
        }
        else
            R2.setEnabled(true);
        if(childItem.getRound3Score()!=0){
            R3.setEnabled(false);
        }
        else
            R3.setEnabled(true);
        R1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                long t = System.currentTimeMillis();
                long delta = t - focusTime;
                if (hasFocus) {     // gained focus
                    if (delta > minDelta) {
                        focusTime = t;
                        focusTarget = view;

                    }
                }
                else {              // lost focus
                    if (delta <= minDelta  &&  view == focusTarget) {
                        focusTarget.post(new Runnable() {   // reset focus to target
                            public void run() {
                                focusTarget.requestFocus();
                            }
                        });
                    }
                }
            }
        });
        R2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                long t = System.currentTimeMillis();
                long delta = t - focusTime;
                if (hasFocus) {     // gained focus
                    if (delta > minDelta) {
                        focusTime = t;
                        focusTarget = view;
                    }
                }
                else {              // lost focus
                    if (delta <= minDelta  &&  view == focusTarget) {
                        focusTarget.post(new Runnable() {   // reset focus to target
                            public void run() {
                                focusTarget.requestFocus();
                            }
                        });
                    }
                }
            }

        });
        R3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                long t = System.currentTimeMillis();
                long delta = t - focusTime;
                if (hasFocus) {     // gained focus
                    if (delta > minDelta) {
                        focusTime = t;
                        focusTarget = view;
                    }
                }
                else {              // lost focus
                    if (delta <= minDelta  &&  view == focusTarget) {
                        focusTarget.post(new Runnable() {   // reset focus to target
                            public void run() {
                                focusTarget.requestFocus();
                            }
                        });
                    }
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                R2.setInputType(InputType.TYPE_CLASS_NUMBER);
//                R3.setInputType(InputType.TYPE_CLASS_NUMBER);

                int score1=0,score2=0,score3=0;
                if(R1.getText().toString().compareTo("")!=0)
                    score1 = Integer.parseInt(R1.getText().toString());

                if(R2.getText().toString().compareTo("")!=0)
                    score2 =Integer.parseInt(R2.getText().toString());
                if(R3.getText().toString().compareTo("")!=0)
                    score3 = Integer.parseInt(R3.getText().toString());

                if(R1.isEnabled() && score1 != 0) {
                    childItem.setRound1Score(score1);
                    R1.setEnabled(false);

                }

                if(R2.isEnabled() && score2!= 0)
                {
                    childItem.setRound2Score(score2);
                    R2.setEnabled(false);
                }

                if(R3.isEnabled() && score3!= 0)
                {
                    childItem.setRound3Score(score3);
                    R3.setEnabled(false);
                }

            }
        });

        final int gPosition = groupPosition;
        Button submit = (Button) convertView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Are you sure you want to Submit?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               // String name = _listDataHeader.get(gPosition);
                                //_listDataHeader.set(gPosition,"Submitted");
                                childItem.setStatus(1);
                                Toast.makeText(_context, "Submitted!!", Toast.LENGTH_SHORT).show();
                                JSONObject url = new JSONObject();
                                JSONObject values = new JSONObject();

                                try {
                                    url.put("url","http://www.acumenit.in/andy/events/endgame");
                                    values.put("gId", childItem.getGameId());
                                    values.put("Total",childItem.getRound1Score()+childItem.getRound2Score()+childItem.getRound3Score());

                                }catch (JSONException e)
                                {

                                }
                                asyncTask =new SendPostRequest((Activity)_context);
                                asyncTask.delegate = ExpandableListAdapter.this;
                                asyncTask.execute(url,values);
                                notifyDataSetChanged();
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
            }
        });

        if(childItem.getStatus() == 1)
        {
            addPlayer.setEnabled(false);
            submit.setEnabled(false);
            update.setEnabled(false);
            R1.setEnabled(false);
            R2.setEnabled(false);
            R3.setEnabled(false);
        }

        else
        {
            addPlayer.setEnabled(true);
            submit.setEnabled(true);
            update.setEnabled(true);
            if(childItem.getRound1Score() != 0)
                R1.setEnabled(false);
            else
                R1.setEnabled(true);
            if(childItem.getRound2Score() != 0)
                R2.setEnabled(false);
            else
                R2.setEnabled(true);
            if(childItem.getRound3Score() != 0)
                R3.setEnabled(false);
            else
                R3.setEnabled(true);

        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        Game childItem = (Game) getChild(groupPosition,0);
        int a =0;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        LinearLayout grouplinearlayout = (LinearLayout) convertView.findViewById(R.id.grouplinearlayout);
        if(childItem!=null) {
            if(childItem.getStatus() == 1)
            {
                grouplinearlayout.setBackgroundColor(Color.parseColor("#00C853"));
            }

            else
                grouplinearlayout.setBackgroundColor(Color.parseColor("#4ba3c7"));
            String headerTitle = _listDataHeader.get(groupPosition);//0+childItem.getGameId() + "\t" + childItem.getPlayerList().get(0).getPlayerName();
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            if(childItem.getStatus() == 1)
            {
                //String headerTitle2 = "Submitted";
                //_listDataHeader.add(0, headerTitle);
                //_listDataHeader.remove(groupPosition);
                //notifyDataSetChanged();
            }

        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                result=data.getStringExtra("Result");

                String playerName = null;
                String playerId = null;

                try{
                    JSONArray arr = new JSONArray(result);
                    playerId = arr.getJSONObject(0).getJSONObject("fields").getString("QId");
                    playerName = arr.getJSONObject(0).getJSONObject("fields").getString("name");
                }catch (JSONException e){

                }
                Log.e(TAG,"result : "+result);
                Toast.makeText(_context,result,Toast.LENGTH_SHORT).show();
                int gp = data.getIntExtra("groupPosition",0);
                String groupName = (String) getGroup(gp);
                List<PlayerDetails> playerList=((Game)getChild(gp,0)).getPlayerList();
                playerList.add(new PlayerDetails(playerId,playerName));
                //_listDataChild.get(groupName).getPlayerList().add(new PlayerDetails("6784848",result));
                _listDataChild.get(groupName).setPlayerList(playerList);
                notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    @Override
    public void processFinish(String result){

    }
}