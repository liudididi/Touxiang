package com.liu.asus.dianshang;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by asus on 2017/9/27.
 */

public interface Login {
    void  showprogressbar();
    void  hindprogressbar();
    void  nameError(String msg);
    void  passError(String msg);
    void  successlogin(String code, String msg,int uid);
    void  faillogin(String code, String msg);
    void  onfailure(Call call, IOException e);
}
