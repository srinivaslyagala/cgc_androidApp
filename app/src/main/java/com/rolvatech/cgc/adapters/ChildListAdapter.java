package com.rolvatech.cgc.adapters;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.utils.AsyncDownloader;
import com.rolvatech.cgc.utils.FileUtils;
import com.rolvatech.cgc.utils.PrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ViewHolder> {
    List<Child> childArrayList = new ArrayList<>();

    public ChildListAdapter(List<Child> childArrayList) {
        this.childArrayList = childArrayList;
    }
    @NonNull
    @Override
    public ChildListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.child_list, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildListAdapter.ViewHolder holder, int position) {
        Child child = childArrayList.get(position);
       // holder.imageView.setImageBitmap(FileUtils.StringToBitMap(child.getProfileImage()));
        holder.textView.setText(child.getFirstName() + " " + child.getLastName());
        new APIClient(holder.imageView.getContext()).getApi().getProfileImage("Bearer " + PrefUtils.getStringPreference(holder.imageView.getContext(), PrefUtils.TOKEN),""+child.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    File sdcard = Environment.getDownloadCacheDirectory();
                    String base64=response.body();
                    Log.i("Image Async Download", "onResponse: Image String downloaded"+base64.length()+" file"+sdcard.getAbsolutePath()+"/cgc/images/"+child.getId()+".temp");
                    if(base64 != null) {
                        String[] images = base64.split(",");
                        if(images.length > 0){
                            String base64Image = images[images.length - 1];
                            FileUtils.writeFile(base64.getBytes(),sdcard.getAbsolutePath()+"/cgc/images/",child.getId()+".temp");
                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            holder.imageView.setImageBitmap(decodedByte);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return childArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.child_image);
            textView = view.findViewById(R.id.assigned_count);
        }
    }
}