package com.it.acumen.acumeneventslocal;

/**
 * Created by pavan on 3/2/2018.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class QRCodeScanActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler,AsyncResponse {
    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;
    SendPostRequest asyncTask =new SendPostRequest(QRCodeScanActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "onCreate");

        mScannerView = new ZXingScannerView(this);

      //  EditText x = new EditText(this,)
       // mScannerView.addView(new EditText(this),60,60);
        setContentView(mScannerView);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                //Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

            } else {
                requestPermission();
            }
        }
        asyncTask.delegate = this;
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRCodeScanActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        final String result = rawResult.getText();
        Log.d("QRCodeScanner", rawResult.getText());
        Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());


        int a = getIntent().getIntExtra("requestCode",0);
        if(a==1){
            JSONObject url = new JSONObject();
            JSONObject values = new JSONObject();
            String gameId = getIntent().getStringExtra("gameId");
            try {
                url.put("url","http://www.acumenit.in/andy/events/addplayer");
                values.put("qId", result);
                values.put("gId",gameId);

            }catch (JSONException e)
            {

            }
            asyncTask.execute(url,values);
        }
        if(a==2){
            JSONObject url = new JSONObject();
            JSONObject values = new JSONObject();

            try {
                url.put("url","http://www.acumenit.in/andy/events/newgame");
                values.put("qId", result);
                values.put("eId","ALP");
            }catch (JSONException e)
            {

            }
            asyncTask.execute(url,values);
        }





    }
    @Override
    public void processFinish(String result){
        int a = getIntent().getIntExtra("requestCode",0);
        //Toast.makeText(QRCodeScanActivity.this,"Request code: "+a,Toast.LENGTH_LONG).show();
        if(a==1)
        {
            int groupPosition = getIntent().getIntExtra("groupPosition",0);
            int childPosition = getIntent().getIntExtra("childPosition",0);
            Intent returnIntent = new Intent();
          //  returnIntent.putExtra("result",result);
            returnIntent.putExtra("groupPosition",groupPosition);
            returnIntent.putExtra("childPosition",childPosition);
            returnIntent.putExtra("requestCode",1);
            returnIntent.putExtra("Result",result);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        }
        else if(a==2)
        {
            Intent returnIntent = new Intent();
           // returnIntent.putExtra("result",result);
            returnIntent.putExtra("Result",result);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

    }
}