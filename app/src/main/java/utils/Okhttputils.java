package utils;

import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import model.LogInterceptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by asus on 2017/9/27.
 */

public class Okhttputils extends AppCompatActivity{

 public static  void   requer(String url, final Backquer backquer ){
     OkHttpClient    client = new OkHttpClient.Builder()
             .addInterceptor(new LogInterceptor())
             .connectTimeout(20, TimeUnit.SECONDS)
             .readTimeout(20, TimeUnit.SECONDS)
             .writeTimeout(20, TimeUnit.SECONDS)
             .retryOnConnectionFailure(false)
             .build();
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
    public OkHttpClient setCard() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            String certificateAlias = Integer.toString(0);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(getAssets().open("kson_server.cer")));//拷贝好的证书
            SSLContext sslContext = SSLContext.getInstance("TLS");
            final TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.addInterceptor(new LogInterceptor());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }
}
