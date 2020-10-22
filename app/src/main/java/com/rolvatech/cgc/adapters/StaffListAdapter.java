package com.rolvatech.cgc.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.StaffDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.FileUtils;
import com.rolvatech.cgc.utils.PrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.ViewHolder> {

    List<UserDTO> staffDTOArrayList = new ArrayList<>();

    public StaffListAdapter(List<UserDTO> staffDTOArrayList) {
        this.staffDTOArrayList = staffDTOArrayList;
    }

    @NonNull
    @Override
    public StaffListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.staff_list, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StaffListAdapter.ViewHolder holder, int position) {
        UserDTO staffDTO = staffDTOArrayList.get(position);
        if(staffDTO.getProfileImage() != null) {
            String[] images = staffDTO.getProfileImage().split(",");
            if(images.length > 0){
                String base64Image = images[images.length - 1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imageView.setImageBitmap(decodedByte);
            }
        };
//        if (staffDTO.getProfileImage() != null && !staffDTO.getProfileImage().equals(""))
//            holder.imageView.setImageBitmap(FileUtils.StringToBitMap(staffDTO.getProfileImage()));
        holder.staffName.setText(staffDTO.getFirstName() + " " + staffDTO.getLastName());
        holder.textView.setText("Children Count:"+staffDTO.getAssignedChildCount());
    }

    @Override
    public int getItemCount() {
        return staffDTOArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView, staffName;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.staff_image);
            staffName = view.findViewById(R.id.staff_name);
            textView = view.findViewById(R.id.assigned_count);
        }
    }
}
