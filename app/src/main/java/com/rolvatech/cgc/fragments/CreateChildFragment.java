package com.rolvatech.cgc.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontSize;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Image;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Video;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.collect.Range;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.dataobjects.InstituteDTO;
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
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class CreateChildFragment extends Fragment {
    TextInputEditText edtName, edtAge, edtParentName, edtParentEmail, edtContactNo, edtOccupation, edtTimeSlot;
    AREditText edtAddress;
    CircleImageView child_image;
    String profileImage;
    Button btnRegisterChild;
    AlertDialogManager alertDialogManager = new AlertDialogManager();
    IARE_Toolbar mToolbar;
    private boolean scrollerAtEnd;
    private AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_child, container, false);
        edtName = root.findViewById(R.id.edtName);
        awesomeValidation.addValidation(edtName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", "fFirstName and Last Name is not valid!");
        edtAge = root.findViewById(R.id.edtAge);
        awesomeValidation.addValidation(edtAge, Range.closed(0, 60), "Age should be in 0 to 60");
        edtParentName = root.findViewById(R.id.edtParentName);
        awesomeValidation.addValidation(edtParentName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", "Parent Name is not valid!");
        edtParentEmail = root.findViewById(R.id.edtParentEmail);
        awesomeValidation.addValidation(edtParentEmail, Patterns.EMAIL_ADDRESS, "Gaurdian Email is not valid");
        edtContactNo = root.findViewById(R.id.edtContactNo);
        awesomeValidation.addValidation(edtContactNo, "^[0-9+()\\s]{0,12}", "Not valid ContactNo");
        edtOccupation = root.findViewById(R.id.edtOccupation);
        awesomeValidation.addValidation(edtOccupation, "^[a-zA-Z\\s]{0,30}", "Occupation is Not valid");
        edtTimeSlot = root.findViewById(R.id.edtTimeSlot);
        awesomeValidation.addValidation(edtTimeSlot, "^[0-9-:\\s]{0,15}", "Time slot should be in 10:00 - 11:00");
        edtAddress = root.findViewById(R.id.edtAddress);
        initToolBar(root);
        child_image = root.findViewById(R.id.child_image);
        btnRegisterChild = root.findViewById(R.id.btnRegisterChild);

        edtTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mFromTime = Calendar.getInstance();
                int hour = mFromTime.get(Calendar.HOUR_OF_DAY);
                int minute = mFromTime.get(Calendar.MINUTE);
                TimePickerDialog mFromTimePicker;
                mFromTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int fromselectedHour, int fromselectedMinute) {
                        TimePickerDialog mToTimePicker;
                        mToTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int toselectedHour, int toselectedMinute) {
                                edtTimeSlot.setText(fromselectedHour + ":" + fromselectedMinute + "-" + toselectedHour + ":" + toselectedMinute);
                            }
                        }, hour, minute, true);//Y
                        mToTimePicker.setTitle("Select To Time");
                        mToTimePicker.show();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mFromTimePicker.setTitle("Select From Time");
                mFromTimePicker.show();
            }
        });
        child_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnRegisterChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChild();
            }
        });
        return root;
    }

    public void registerChild() {
        if(awesomeValidation.validate()){
            String firstLastName=edtName.getText().toString();
            Log.e("splash","AboutChild Value:"+edtAddress.getHtml());
            UserDTO user=new UserDTO();
            user.setAboutMe(edtAddress.getHtml());
            user.setAge(Integer.parseInt(edtAge.getText().toString()));
            user.setContact(edtContactNo.getText().toString());
            user.setEmail(edtParentEmail.getText().toString());
            user.setOccupation(edtOccupation.getText().toString());
            user.setParentName(edtParentName.getText().toString());
            user.setTimeSlot(edtTimeSlot.getText().toString());
            user.setUserName(edtParentEmail.getText().toString());
            InstituteDTO instituteDTO=new InstituteDTO();
            instituteDTO.setId(Long.parseLong(PrefUtils.getStringPreference(getContext(),PrefUtils.INSTITUTE_ID)));
            user.setInstitute(instituteDTO);
            if(firstLastName.indexOf(" ")>0) {
                user.setFirstName(firstLastName.substring(0, firstLastName.indexOf(" ")));
                user.setLastName(firstLastName.substring(firstLastName.indexOf(" ")));
            }else{
                user.setFirstName(firstLastName);
                user.setLastName("");
            }
            user.setProfileImage(profileImage);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMax(100);
            progressDialog.setMessage("Processing....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            new APIClient(getActivity()).getApi().registerChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user)
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful() || response.code() == 200) {
                                alertDialogManager.showAlertDialog(getActivity(), "Success", "Child Registered successfully", true);
                            } else {
                                alertDialogManager.showAlertDialog(getActivity(), "Failure", "Child Registration failed", false);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getContext(),"Fields are not valid",Toast.LENGTH_LONG).show();
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
                    child_image.setImageBitmap(bitmap);
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
                child_image.setImageBitmap(thumbnail);
            }
        }
    }

    private void initToolBar(View root) {
        mToolbar = root.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem fontSize = new ARE_ToolItem_FontSize();
        IARE_ToolItem fontColor = new ARE_ToolItem_FontColor();
        IARE_ToolItem backgroundColor = new ARE_ToolItem_BackgroundColor();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem hr = new ARE_ToolItem_Hr();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
        IARE_ToolItem image = new ARE_ToolItem_Image();
        IARE_ToolItem video = new ARE_ToolItem_Video();
        IARE_ToolItem at = new ARE_ToolItem_At();

        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(fontSize);
        mToolbar.addToolbarItem(fontColor);
        mToolbar.addToolbarItem(backgroundColor);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(hr);
        mToolbar.addToolbarItem(link);
        mToolbar.addToolbarItem(subscript);
        mToolbar.addToolbarItem(superscript);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);
        mToolbar.addToolbarItem(image);
        mToolbar.addToolbarItem(video);
        mToolbar.addToolbarItem(at);

        edtAddress.setToolbar(mToolbar);
        initToolbarArrow(root);
    }

    private void initToolbarArrow(View root) {
        final ImageView imageView = root.findViewById(R.id.arrow);
        if (this.mToolbar instanceof ARE_ToolbarDefault) {
            ((ARE_ToolbarDefault) mToolbar).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollX = ((ARE_ToolbarDefault) mToolbar).getScrollX();
                    int scrollWidth = ((ARE_ToolbarDefault) mToolbar).getWidth();
                    int fullWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();

                    if (scrollX + scrollWidth < fullWidth) {
                        imageView.setImageResource(R.drawable.arrow_right);
                        scrollerAtEnd = false;
                    } else {
                        imageView.setImageResource(R.drawable.arrow_left);
                        scrollerAtEnd = true;
                    }
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollerAtEnd) {
                    ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(-Integer.MAX_VALUE, 0);
                    scrollerAtEnd = false;
                } else {
                    int hsWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();
                    ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(hsWidth, 0);
                    scrollerAtEnd = true;
                }
            }
        });
    }
}
