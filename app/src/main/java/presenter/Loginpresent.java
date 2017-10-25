package presenter;

import android.text.TextUtils;

import com.liu.asus.dianshang.Login;

import java.io.IOException;

import model.Loginmodel;
import okhttp3.Call;

/**
 * Created by asus on 2017/9/27.
 */

public class Loginpresent{
    private Loginmodel loginmodel;
    private Login login;
    public  Loginpresent(Login login){
        loginmodel=new Loginmodel();
        this.login=login;
    }
public  void login(String phone,String password){
     login.showprogressbar();
    if(TextUtils.isEmpty(phone)){
        login.nameError("手机号码为空");
        login.hindprogressbar();
        return;
    }
    if(TextUtils.isEmpty(password)){
        login.passError("密码为空");
        login.hindprogressbar();
        return;
    }
    loginmodel.login(phone,password,new Loginmodel.Ilogin() {
        @Override
        public void onfailure(Call call, IOException e) {
            login.onfailure(call,e);
        }
        @Override
        public void suesslogin(String code, String msg,int uid) {
            login.successlogin(code,msg,uid);
            login.hindprogressbar();
        }
        @Override
        public void faillogin(String code, String msg) {
             login.faillogin(code,msg);
             login.hindprogressbar();
        }
    });
}
}
