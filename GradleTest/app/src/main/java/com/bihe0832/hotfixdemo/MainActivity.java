package com.bihe0832.hotfixdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bihe0832.api.MySDKApi;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "bihe0832 Gradle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySDKApi.onCreate(this);

        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        MyOnClickListener myOnClickListener = new MyOnClickListener();

        Button upperMD5Btn = (Button)findViewById(R.id.UpperMD5Btn);
        upperMD5Btn.setOnClickListener(myOnClickListener);

        Button lowerMD5Btn = (Button)findViewById(R.id.LowerMD5Btn);
        lowerMD5Btn.setOnClickListener(myOnClickListener);

        Button startUpdateBtn = (Button)findViewById(R.id.sdkUpdateBtn);
        startUpdateBtn.setOnClickListener(myOnClickListener);
        Button clsUpdateBtn = (Button)findViewById(R.id.sdkClsUpdateBtn);
        clsUpdateBtn.setOnClickListener(myOnClickListener);

    }

   private class MyOnClickListener implements View.OnClickListener {

       /**
        * Called when a view has been clicked.
        *
        * @param v The view that was clicked.
        */
       @Override
       public void onClick(View v) {
           switch (v.getId()){
               case R.id.UpperMD5Btn:
                   MySDKApi.testHotfix();
                   showResult(MySDKApi.getUpperMD5(getUserInput()));
                   break;
               case R.id.LowerMD5Btn:
                   MySDKApi.testHotfix();
                   showResult(MySDKApi.getLowerMD5(getUserInput()));
                   break;
               case R.id.sdkUpdateBtn:
                   MySDKApi.testHotfix();
                   MySDKApi.startUpdate();
                   break;
               case R.id.sdkClsUpdateBtn:
                   MySDKApi.testHotfix();
                   MySDKApi.clearUpdate();
                   break;
               default:
                   Log.d(LOG_TAG,"bad button");
           }
       }

       private String getUserInput(){
           EditText md5KeyEdit = (EditText)findViewById(R.id.md5String);
           String md5Key = md5KeyEdit.getText().toString();
           if(null != md5Key){
               Log.d(LOG_TAG,"getUserInput:" + md5Key);
               return md5Key;
           }else{
               Toast.makeText(MainActivity.this,"md5 key is bad!",Toast.LENGTH_LONG).show();
               return "";
           }
       }

       private void showResult(String s){
           Log.d(LOG_TAG,"showResult:" + s);
           if(!s.equals("")){
               TextView md5ResultTextView = (TextView)findViewById(R.id.md5Result);
               md5ResultTextView.setText("MD5:"+ s);
           }
       }
   }
}
