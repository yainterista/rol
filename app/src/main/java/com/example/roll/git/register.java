package com.example.roll.git;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.FormBody.*;

public class register extends AppCompatActivity {


    private EditText etdis;
    private EditText etconpass;
    private EditText etpass;
    private EditText etusern;
    private Button btpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etdis = (EditText) findViewById(R.id.tvdisRE);
        etconpass = (EditText) findViewById(R.id.tvconpasswordRE);
        etpass = (EditText) findViewById(R.id.tvpasswirdRE);
        etusern = (EditText) findViewById(R.id.tvusernameRE);
        btpass = (Button) findViewById(R.id.btnpassword);

        setlistener();
    }

    private void setlistener() {

        btpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(validate()){
                new Regis(etusern.getText().toString(),
                        etpass.getText().toString(),
                        etconpass.getText().toString(),
                        etdis.getText().toString()).execute();
            }else{
                Toast.makeText(register.this,"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT);
            }
            }
        });


    }

    private boolean validate() {
        String username = etusern.getText().toString();
        String password = etpass.getText().toString();
        String passwordcon = etconpass.getText().toString();
        String displayname = etdis.getText().toString();

        if (username.isEmpty()) return false;
        if (password.isEmpty()) return false;
        if (passwordcon.isEmpty()) return false;
        if (!password.equals(passwordcon)) return false;
        if (displayname.isEmpty()) return false;

        return true;
    }
      private class Regis extends AsyncTask<Void,Void,String>{

          private  String username;
          private  String password;
          private  String passwordCon;
          private  String displayname;

          public Regis(String username, String password, String passwordCon,  String displayname) {
              this.username = username;
              this.password = password;
              this.passwordCon = passwordCon;
              this.displayname = displayname;

          }


          @Override
          protected void onPreExecute() {
              super.onPreExecute();
          }


          @Override
          protected String doInBackground(Void... params) {
              OkHttpClient client = new OkHttpClient();
              Request request;
              Response response;

              RequestBody requestbody = new FormBody.Builder()
                      .add("username" , username )
                      .add("password" , password )
                      .add("password_con" , passwordCon )
                      .add("display_name" , displayname )
                      .build();


              request = new Request.Builder()
                      .url("http://kimhun55.com/pollservices/signup.php")
                      .post(requestbody)
                      .build();
              try{
                  response = client.newCall(request).execute();
                  if(response.isSuccessful()){
                      return response.body().string();
                  }
              }catch (IOException ex){
                  ex.printStackTrace();
              }


              return null;
          }


          @Override
          protected void onPostExecute(String s) {
              super.onPostExecute(s);
              Toast.makeText(register.this, s, Toast.LENGTH_SHORT).show();


              //{
              //  "result": {
              //      "result": -7,
              //             "result_desc": " duplicate username"
              //   }


              try {
                  JSONObject rootobj = new JSONObject(s);
                  if(rootobj.has("result")){
                      JSONObject resultobj = rootobj.getJSONObject("result");
                      if(resultobj.getInt("result")==1){
                          Toast.makeText(register.this , resultobj.getString("result_desc"),Toast.LENGTH_SHORT).show();
                          finish();
                      }else{
                          Toast.makeText(register.this , resultobj.getString("result_desc"),Toast.LENGTH_SHORT).show();
                      }
                  }
              }catch (JSONException ex){

              }
          }
          }
      }

