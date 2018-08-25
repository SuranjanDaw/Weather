package com.example.android.weather;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyAsyncTask extends AsyncTask<double[],Void,String > {

    //TextView wname ;
    TextView wdetails;
    public MyAsyncTask(TextView weather1){
        //wname = title1;
        wdetails = weather1;
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
    protected String doInBackground(double[]... doubles) {
        return NetworkUtil.getBookInfo(doubles[0]);
    }

    @Override
    protected  void onPostExecute(String s){

        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("weather");
            for (int i= 0; i< itemsArray.length(); i++){
                JSONObject wea = itemsArray.getJSONObject(i);
                //String weaName = null;
                String weaDetails = null;
                //String price = null;
                //JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                try{
                    //weaName = wea.getString("main");

                    weaDetails = wea.getString("main");
                    Log.d("aa",weaDetails);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                //int len = auth.length()-2;
                //Log.d("ab",price);
                if(weaDetails !=null) {
                    wdetails.setText(weaDetails);
                    //wname.setText(weaName);
                    return;
                }
            }
            //wname.setText("No Result Found");
            wdetails.setText("No Result Found");
        }catch (Exception e){
            //wname.setText("No Result Found");
            wdetails.setText("No Result Found");
            e.printStackTrace();
        }


    }
}
