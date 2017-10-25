package model;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/27.
 */

public class Regmodel{
  public  void reg(String phone, String password, final Reglogin reglogin){
          Okhttputils.requer("http://120.27.23.105/user/reg?mobile="+phone+"&&password="+password, new Okhttputils.Backquer() {
          @Override
          public void onfailure(Call call, IOException e) {
              reglogin.onfailure(call,e);
          }
          @Override
          public void onresponse(Call call, Response response) {
              try {
                  JSONObject json=new JSONObject(response.body().string());
                  String code = json.optString("code");
                  String msg = json.optString("msg");
                  if(code.equals("0")){
                       reglogin.suessreg(code,msg);
                  }else {
                       reglogin.faillreg(null,msg);
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      });
  }

  public  interface  Reglogin{
    void   onfailure(Call call, IOException e);
    void  suessreg(String code, String msg);
    void  faillreg(String code, String msg);
  }
}
