package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.InstituteDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.FileUtils;
import com.rolvatech.cgc.utils.PrefUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.rolvatech.cgc.utils.Constants.REQUEST_IMAGE_CODE;

public class CreateStaffFragment extends Fragment {
    TextInputEditText edtMobile, edtlastName, edtFirstName, edtEmail, edtPassword;
    CircleImageView staff_image;
    Button btnRegisterStaff;
    String profileImage;
    AlertDialogManager alertDialogManager = new AlertDialogManager();
    ProgressDialog progressDialog;
    private AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_staff, container, false);
        edtFirstName = root.findViewById(R.id.edtFirstName);
        edtlastName = root.findViewById(R.id.edtlastName);
        edtMobile = root.findViewById(R.id.edtMobile);
        edtEmail = root.findViewById(R.id.edtEmail);
        edtPassword = root.findViewById(R.id.edtPassword);
        staff_image = root.findViewById(R.id.staff_image);

        awesomeValidation.addValidation(edtFirstName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", "FirstName not valid!");
        awesomeValidation.addValidation(edtlastName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", "Last Name not valid!");
        awesomeValidation.addValidation(edtMobile, Patterns.PHONE, "Mobile No not valid!");
        awesomeValidation.addValidation(edtEmail, Patterns.EMAIL_ADDRESS, "Email not valid!");
        awesomeValidation.addValidation(edtPassword, "^[a-zA-Z0-9@_]{1,}", "Password Should be minimum 6 characters and contain a-zA-Z0-9@_.");

        btnRegisterStaff = root.findViewById(R.id.btnRegisterStaff);
        staff_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                showImgePicker();
            }
        });
        btnRegisterStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStaff();
            }
        });

        return root;
    }

    public void registerStaff() {

        if (awesomeValidation.validate()) {
            UserDTO user = new UserDTO();
            user.setFirstName(edtFirstName.getText().toString());
            user.setLastName(edtlastName.getText().toString());
            user.setContact(edtMobile.getText().toString());
            user.setEmail(edtEmail.getText().toString());
            user.setUserName(edtEmail.getText().toString());
            user.setPassword(edtPassword.getText().toString());
            user.setProfileImage(profileImage);
            InstituteDTO instituteDTO = new InstituteDTO();
            instituteDTO.setId(Long.parseLong(PrefUtils.getStringPreference(getContext(), PrefUtils.INSTITUTE_ID)));
            user.setInstitute(instituteDTO);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMax(100);
            progressDialog.setMessage("Processing....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            new APIClient(getActivity()).getApi().registerStaff("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user)
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful() || response.code() == 200) {
                                alertDialogManager.showAlertDialog(getActivity(), "Success", "Staff Registered successfully", true);
                            } else {
                                alertDialogManager.showAlertDialog(getActivity(), "Failure", "Staff Registration failed", true);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
        } else {

        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    getActivity().startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
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



    private void showImgePicker() {
        String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};

        ImagePicker.Companion.with(this)
                .galleryMimeTypes(mimeTypes)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(512)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(500, 500)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(REQUEST_IMAGE_CODE);
    }


}
