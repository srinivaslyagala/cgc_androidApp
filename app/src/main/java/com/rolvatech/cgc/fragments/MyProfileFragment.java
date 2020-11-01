package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.RecyclerTouchListener;
import com.rolvatech.cgc.activities.LoginActivity;
import com.rolvatech.cgc.adapters.ChildListAdapter;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.FileUtils;
import com.rolvatech.cgc.utils.PrefUtils;
import com.rolvatech.cgc.utils.UtilTransformer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.rolvatech.cgc.utils.Constants.REQUEST_IMAGE_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    RecyclerView recyclerView;
    TextInputEditText edtMobile, edtlastName, edtFirstName, edtEmail;
    CircleImageView staff_image;
    Button btnUpdateStaff;
    AlertDialogManager alertDialogManager = new AlertDialogManager();
    String profileImage;
    UserDTO staffDTO;
    private AwesomeValidation awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        String userId=PrefUtils.getStringPreference(this.getContext(), PrefUtils.USER_ID);
        edtMobile = root.findViewById(R.id.edtMobile);

        edtEmail = root.findViewById(R.id.edtEmail);

        edtEmail.setEnabled(false);

        edtFirstName = root.findViewById(R.id.edtFirstName);

        edtlastName=root.findViewById(R.id.edtlastName);


        staff_image = root.findViewById(R.id.staff_image);
        btnUpdateStaff = root.findViewById(R.id.btnUpdateStaff);

        awesomeValidation.addValidation(edtFirstName,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$","FirstName not valid!");
        awesomeValidation.addValidation(edtlastName,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$","Last Name not valid!");
        awesomeValidation.addValidation(edtMobile, Patterns.PHONE,"Mobile No not valid!");
        staffDTO=new UserDTO();
        getStaffDetails(userId,root);
        btnUpdateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStaff();
            }
        });
        return root;
    }

    public void updateStaff() {
        if(awesomeValidation.validate()) {
            UserDTO user = new UserDTO();
            user.setFirstName(edtFirstName.getText().toString());
            user.setLastName(edtlastName.getText().toString());
            user.setContact(edtMobile.getText().toString());
            user.setEmail(edtEmail.getText().toString());
            user.setProfileImage(profileImage);
            user.setId(staffDTO.getId());
            showDialog();
            new APIClient(getActivity()).getApi().updateProfile("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user)
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            hideDialog();
                            if (response.isSuccessful() || response.code() == 200) {
                                alertDialogManager.showAlertDialog(getActivity(), "Success", "Details updated successfully", true);
                            } else {
                                alertDialogManager.showAlertDialog(getActivity(), "Failure", "Details update failed", false);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {
                            hideDialog();
                        }
                    });
        }else{
            Toast.makeText(getContext(),"UserFields Validation failed",Toast.LENGTH_LONG).show();
        }
    }

    private void getStaffDetails(String userId, View root) {
        showDialog();
        new APIClient(getActivity()).getApi().getUserDetailsById("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),userId).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                hideDialog();
                if(response.code() == 200){
                    try {
                        UserDTO staffDetails = response.body();
                        staffDTO.setId(staffDetails.getId());
                        staffDTO.setFirstName(staffDetails.getFirstName());
                        staffDTO.setLastName(staffDetails.getLastName());
                        staffDTO.setContact(staffDetails.getContact());
                        staffDTO.setEmail(staffDetails.getEmail());
                        staffDTO.setProfileImage(staffDetails.getProfileImage());
                        edtEmail.setText(staffDetails.getUserName());
                        edtEmail.setEnabled(false);
                        edtMobile.setText(staffDetails.getContact());
                        edtFirstName.setText(staffDetails.getFirstName());
                        edtlastName.setText(staffDetails.getLastName());
                        String profileImage = staffDetails.getProfileImage();
                        if(profileImage != null) {
                            String[] images = profileImage.split(",");

                            if(images.length > 0){

                                String base64Image = images[images.length - 1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                staff_image.setImageBitmap(decodedByte);
                            }
                        }
                        staff_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showImgePicker();
                            }
                        });
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void showImgePicker() {
        String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};

        ImagePicker.Companion.with(this)
                .galleryMimeTypes(mimeTypes)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(512)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(500, 500)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(REQUEST_IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    profileImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    staff_image.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, System.currentTimeMillis() + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                profileImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Log.w("path of image from gallery......******************.........", picturePath+"");
                staff_image.setImageBitmap(thumbnail);

            } else if (requestCode == REQUEST_IMAGE_CODE) {
                //Image Uri will not be null for RESULT_OK

                if (data != null) {

                    File file = ImagePicker.Companion.getFile(data);
                    if (file != null) {
                        Glide.with(getActivity()).load(file.getAbsolutePath()).error(R.mipmap.ic_launcher).into(staff_image);
                        profileImage = FileUtils.generateImageData(file);
                    }
                }
            }
        }
    }
    AlertDialog spotsDialog;

    private void showDialog() {

        if (spotsDialog == null) {
            spotsDialog = new SpotsDialog.Builder()
                    .setContext(getActivity())
                    .setMessage("Loading...")
                    .build();
        }
        spotsDialog.show();
    }

    private void hideDialog() {
        if (spotsDialog != null) {
            spotsDialog.dismiss();
        }
    }
}
