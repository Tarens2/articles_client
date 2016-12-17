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

public class ApiGetMe {
    final private String myURL = "http://10.0.3.2:8000/api/auth/me";
    private String email;
    private String password;
    private String token;

    private String user;

    private final String USER_AGENT = "Mozilla/5.0";

    public ApiGetMe(String token){
        this.token = token;
    }

    public void connect(){

        String params_post = "token="+token;
        byte[] data = null;
        InputStream is = null;

        try {
            URL url = new URL(myURL+"?"+params_post);
            Log.v(myURL+"?"+params_post,"tokenapi");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            OutputStream os = conn.getOutputStream();
            params_post = "";
            data = params_post.getBytes("UTF-8");
            os.write(data);

            conn.connect();
            int responseCode= conn.getResponseCode();
            Log.v("code: "+responseCode,"tokenapi");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(responseCode != 200){

                is = conn.getErrorStream();

            }
            else{
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
                String user = jObject.getString("user");


                Log.d("token length: "+user,"tokenapi");
            }





        }
        catch(Exception e){
            e.printStackTrace();
            Log.v(e.getMessage(),"tokenapi error");
        }
    }

    public String getName(){
        return user;
    }
}
