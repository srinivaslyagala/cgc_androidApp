package com.rolvatech.cgc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.rolvatech.cgc.APIClient;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsyncDownloader extends AsyncTask<Object, Void, Bitmap> {

    private ImageView imv;
    private Long id;
    private Context context;

    public AsyncDownloader(Context context, ImageView imv, Long id) {
        this.imv = imv;
        this.id = id;
        this.context=context;
    }

    public AsyncDownloader(Context context,ImageView imv){
        this.imv=imv;
        this.context=context;
        this.id=0L;
    }

    @Override
    protected Bitmap doInBackground(Object... imageViews) {
        File sdcard = Environment.getDownloadCacheDirectory();
        Boolean exists=FileUtils.isFileExists(sdcard.getAbsolutePath()+"/cgc/images/"+this.id+".temp");
        if(exists){
            //readFrom File
            InputStream stream=FileUtils.getFileFromSDcard(sdcard.getAbsolutePath()+"/cgc/images/",id+".temp");
            String base64Image="";
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imv.setImageBitmap(decodedByte);
        }else{
            new APIClient(context).getApi().getProfileImage("Bearer " + PrefUtils.getStringPreference(context, PrefUtils.TOKEN),""+this.id).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        String base64=response.body();
                        String base64Image = base64.split(",")[1];
                        Log.i("Image Async Download", "onResponse: Image String downloaded"+base64Image.length()+" file"+sdcard.getAbsolutePath()+"/cgc/images/"+id+".temp");
                        FileUtils.writeFile(base64Image.getBytes(),sdcard.getAbsolutePath()+"/cgc/images/",id+".temp");
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imv.setImageBitmap(decodedByte);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }
        return null;
    }
}