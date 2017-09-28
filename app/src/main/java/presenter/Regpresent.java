package presenter;

import android.text.TextUtils;

import com.liu.asus.dianshang.Login;
import com.liu.asus.dianshang.Reg;

import java.io.IOException;

import model.Loginmodel;
import model.Regmodel;
import okhttp3.Call;

/**
 * Created by asus on 2017/9/27.
 */

public class Regpresent {
    private Regmodel regmode;
    private Reg reg;
    public Regpresent(Reg reg){
        regmode=new Regmodel();
        this.reg=reg;
    }
public  void Reg(String phone,String password) {
    if (TextUtils.isEmpty(phone)) {
        reg.nameError("手机号码为空");
        return;
    }
    if (TextUtils.isEmpty(password)) {
        reg.passError("密码为空");
        return;
    }
    regmode.reg(phone, password, new Regmodel.Reglogin() {
        @Override
        public void onfailure(Call call, IOException e) {
            reg.onfailure(call,e);
        }

        @Override
        public void suessreg(String code, String msg) {
           reg.successreg(code,msg);
        }

        @Override
        public void faillreg(String code, String msg) {
            reg.faillreg(code,msg);
        }
    });

}
}
