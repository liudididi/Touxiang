package presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.liu.asus.dianshang.MainActivity;
import com.liu.asus.dianshang.MemessageActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import static android.app.Activity.RESULT_OK;
import static java.lang.String.valueOf;

/**
 * Created by asus on 2017/9/27.
 */

public class Changepresent {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    public  void  showChoosePicDialog(final Activity context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                       /* Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image*//*");*/
                        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        context.startActivityForResult(intent1, CHOOSE_PICTURE);
                       // context.startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        context.startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }
    public  void back(int requestCode, int resultCode, Intent data, MemessageActivity count, ImageView imgview, int uid){

        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri,count); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData(),count); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data,imgview,uid,count); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri, MemessageActivity count) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        count.startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    protected void setImageToView(Intent data, ImageView imgview, int uid, final MemessageActivity count) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            try {
                Bitmap photo = extras.getParcelable("data");
                imgview.setImageBitmap(photo);
                File file=new File("mnt/sdcard/icon.png");
                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream  outputStream = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                if(file!=null) {
                    String filename = file.getName();
                    Map<String, Object> params = new HashMap<>();
                    params.put("uid", uid);
                    OkHttpClient okHttpClient = new OkHttpClient();
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    builder.addFormDataPart("file", filename, requestBody);
                    if (params != null) {
                        // map 里面是请求中所需要的 key 和 value
                        for (Map.Entry entry : params.entrySet()) {
                            builder.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
                        }
                    }
                    Request request = new Request.Builder().url("http://120.27.23.105/file/upload").post(builder.build()).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try {
                                JSONObject json=new JSONObject(response.body().string());
                                final String msg = json.optString("msg");
                                count.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(count, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



}
