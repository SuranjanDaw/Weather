package com.example.android.weather;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Random;

public class MyAsyncTask extends AsyncTask<String,Void,String > {

    TextView author ;
    TextView title;
    public MyAsyncTask(TextView title1, TextView author1){
        title = title1;
        author = author1;
    }
//    @Override
//    protected String doInBackground(Void... voids) {
//        Random r =new Random();
//        int rand = r.nextInt(11);
//        int s = rand*200;
//        try {
//            Thread.sleep(s);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return "Awake at last after sleeping for"+s+"miliseconds";
//    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtil.getBookInfo(strings[0]);
    }

    @Override
    protected  void onPostExecute(String s){

        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("weather");
            for (int i= 0; i< itemsArray.length(); i++){
                JSONObject book = itemsArray.getJSONObject(i);
                String titl = null;
                String auth = null;
                //String price = null;
                //JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                try{
                    titl = book.getString("main");
                    Log.d("aa",titl);
                    auth = book.getString("description");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                //int len = auth.length()-2;
                //Log.d("ab",price);
                if(title != null && auth !=null) {
                    author.setText(auth);
                    title.setText(titl);
                    return;
                }
            }
            title.setText("No Result Found");
            author.setText("");
        }catch (Exception e){
            title.setText("No Result Found");
            author.setText("");
            e.printStackTrace();
        }


    }
}
