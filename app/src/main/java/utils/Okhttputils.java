package utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by asus on 2017/9/27.
 */

public class Okhttputils {

 public static  void   requer(String url, final Backquer backquer ){
     OkHttpClient client=new OkHttpClient();
     Request request=new Request.Builder().url(url).build();
     Call call = client.newCall(request);
     call.enqueue(new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
             backquer.onfailure(call,e);
         }
         @Override
         public void onResponse(Call call, Response response) throws IOException {
             backquer.onresponse(call,response);
         }
     });
 }
 public  interface  Backquer{
     void  onfailure(Call call, IOException e);
     void  onresponse(Call call, Response response);
 }

}
