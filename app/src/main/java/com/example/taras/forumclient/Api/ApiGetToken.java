package com.example.taras.forumclient.Api;

import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 04.12.2016.
 */

public class ApiGetToken {

    final private String myURL = "http://10.0.3.2:8000/api/auth";
    private String email;
    private String password;
    private String token;

    public ApiGetToken(String username, String password){
        this.email = username;
        this.password = password;
    }
    public void connect(){
        token = null;

        String params_post = "email="+email + "&password="+password;
        byte[] data = null;
        InputStream is = null;

        try {
            URL url = new URL(myURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Length", "" + Integer.toString(params_post.getBytes().length));
            OutputStream os = conn.getOutputStream();
            data = params_post.getBytes("UTF-8");
            os.write(data);
            data = null;

            conn.connect();
            int responseCode= conn.getResponseCode();
            Log.v("code: "+responseCode,"tokenapi");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            is = conn.getInputStream();

            byte[] buffer = new byte[8192]; // Такого вот размера буфер
            // Далее, например, вот так читаем ответ
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            data = baos.toByteArray();
            String resultString = new String(data, "UTF-8");

            JSONObject jObject = new JSONObject(resultString);
            token = jObject.getString("token");

            Log.d("token length: "+resultString,"tokenapi");

        }
        catch(Exception e){
            e.printStackTrace();
            Log.v(e.getMessage(),"tokenapi error");
        }
    }
    public String getToken(){

        return token;
    }
}
