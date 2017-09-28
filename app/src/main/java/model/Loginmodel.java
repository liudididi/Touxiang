package model;

import android.widget.Toast;

import com.liu.asus.dianshang.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/27.
 */

public class Loginmodel {
  public  void login(String phone, String password, final Ilogin ilogin){
      Okhttputils.requer("http://169.254.157.227:8888/user/login?mobile="+phone+"&&password="+password, new Okhttputils.Backquer() {
          @Override
          public void onfailure(Call call, IOException e) {
              ilogin.onfailure(call,e);
          }
          @Override
          public void onresponse(Call call, Response response) {
              try {
                  String str=response.body().string();
                  System.out.println("=========="+str);
                  JSONObject json=new JSONObject(str);
                  String code = json.optString("code");
                  String msg = json.optString("msg");
                  if(code.equals("1")){
                      ilogin.faillogin(code,msg);
                  }else if(code.equals("0")){
                      ilogin.suesslogin(code,msg);
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      });
  }

  public  interface  Ilogin{
    void onfailure(Call call, IOException e);
    void  suesslogin(String code,String msg);
    void  faillogin(String code,String msg);
  }

}
