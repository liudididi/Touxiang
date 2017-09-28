package com.liu.asus.dianshang;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by asus on 2017/9/27.
 */

public interface Reg {
    void  nameError(String msg);
    void  passError(String msg);
    void  successreg(String code, String msg);
    void  faillreg(String code, String msg);
    void  onfailure(Call call, IOException e);
}
