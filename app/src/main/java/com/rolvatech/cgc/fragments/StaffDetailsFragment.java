package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputEditText;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.RecyclerTouchListener;
import com.rolvatech.cgc.adapters.ChildListAdapter;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.StaffDTO;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.utils.AlertDialogManager;
import com.rolvatech.cgc.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class StaffDetailsFragment extends Fragment {
    TextInputEditText edtMobile, edtlastName, edtFirstName, edtEmail, edtPassword;
    CircleImageView staff_image;
    Button btnUpdateStaff, btnDeleteStaff;
    RecyclerView childList;
    AlertDialogManager alertDialogManager = new AlertDialogManager();
    String profileImage;
    UserDTO staffDTO;
    ChildListAdapter childListAdapter;
    List<Child> childrensList=new ArrayList<>();

    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    private AwesomeValidation awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.staff_details, container, false);
        Bundle bundle = getArguments();
        staffDTO = (UserDTO) bundle.getSerializable("staff");
        edtMobile = root.findViewById(R.id.edtMobile);
        edtMobile.setText(staffDTO.getContact());
        edtEmail = root.findViewById(R.id.edtEmail);
        edtEmail.setText(staffDTO.getUserName());
        edtEmail.setEnabled(false);

        edtFirstName = root.findViewById(R.id.edtFirstName);
        edtFirstName.setText(staffDTO.getFirstName());
        edtlastName=root.findViewById(R.id.edtlastName);
        edtlastName.setText(staffDTO.getLastName());

        staff_image = root.findViewById(R.id.staff_image);
        btnDeleteStaff = root.findViewById(R.id.btnDeleteStaff);
        btnUpdateStaff = root.findViewById(R.id.btnUpdateStaff);
        childList = root.findViewById(R.id.assigned_child_List);

        awesomeValidation.addValidation(edtFirstName,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$","FirstName not valid!");
        awesomeValidation.addValidation(edtlastName,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$","Last Name not valid!");
        awesomeValidation.addValidation(edtMobile, Patterns.PHONE,"Mobile No not valid!");
        childList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        childList.setLayoutManager(mLayoutManager);
        childList.setItemAnimator(new DefaultItemAnimator());
        getStaffDetails(staffDTO,root);
        btnDeleteStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStaff();
            }
        });
        btnUpdateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStaff();
            }
        });
        return root;
    }

    public void deleteStaff() {
            new APIClient(getActivity()).getApi().deactivateUser("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), staffDTO.getId())
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            if (response.isSuccessful() || response.code() == 200) {
                                alertDialogManager.showAlertDialog(getActivity(), "Success", "staff deleted successfully", true);
                            } else {
                                alertDialogManager.showAlertDialog(getActivity(), "Failure", "staff deletion failed", false);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {

                        }
                    });

    }

    private void getStaffDetails(UserDTO staffDTO, View root) {
        Log.e("splash","StaffId:"+staffDTO.getId());
        new APIClient(getActivity()).getApi().getUserDetailsById("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),staffDTO.getId()).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if(response.code() == 200){
                    UserDTO staffDetails=response.body();
                    edtEmail.setText(staffDetails.getUserName());
                    edtEmail.setEnabled(false);
                    edtMobile.setText(staffDetails.getContact());
                    edtFirstName.setText(staffDetails.getFirstName());
                    edtlastName.setText(staffDetails.getLastName());
                    String profileImage=staffDetails.getProfileImage();
                    String base64Image = profileImage.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    staff_image.setImageBitmap(decodedByte);
                    staff_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectImage();
                        }
                    });
                }else{

                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {

            }
        });
        new APIClient(getActivity()).getApi().getChildListForStaff("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),staffDTO.getId()).enqueue(
                new Callback<List<Child>>() {
                    @Override
                    public void onResponse(Call<List<Child>> call, Response<List<Child>> response) {
                        if(response.code() == 200){
                            List<Child> childrensList=response.body();
                            childListAdapter = new ChildListAdapter(childrensList);
                            childList.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            childList.setLayoutManager(mLayoutManager);
                            childList.setItemAnimator(new DefaultItemAnimator());
                            childList.setAdapter(childListAdapter);
                            childList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), childList, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Child childDto = childrensList.get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("child", childDto);
                                    PrefUtils.setLongPreference(getContext(),PrefUtils.CHILD_ID,childDto.getId());
                                    fragment = new ChildDetailsFragment();
                                    fragment.setArguments(bundle);
                                    fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                                    fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                                    fragmentTransaction.replace(R.id.content_frame, fragment);

                                    fragmentTransaction.commit();
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Child>> call, Throwable t) {

                    }
                }
        );
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
            new APIClient(getActivity()).getApi().updateProfile("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user)
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            if (response.isSuccessful() || response.code() == 200) {
                                alertDialogManager.showAlertDialog(getActivity(), "Success", "Staff details updated successfully", true);
                            } else {
                                alertDialogManager.showAlertDialog(getActivity(), "Failure", "Staff details update failed", false);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {

                        }
                    });
        }else{
            Toast.makeText(getContext(),"UserFields Validation failed",Toast.LENGTH_LONG).show();
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
            }
        }
    }

}
