package com.rolvatech.cgc.fragments;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.collect.Range;
import com.rolvatech.cgc.APIClient;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.adapters.Pager;
import com.rolvatech.cgc.dataobjects.AreaDTO;
import com.rolvatech.cgc.dataobjects.AreaTaskDTO;
import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.ChildTaskDTO;
import com.rolvatech.cgc.dataobjects.StaffDTO;
import com.rolvatech.cgc.dataobjects.TaskDTO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.rolvatech.cgc.utils.Constants.REQUEST_IMAGE_CODE;

public class ChildDetailsFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    TextInputEditText edtName, edtAge, edtParentName, edtParentEmail, edtContactNo, edtOccupation, edtTimeSlot, edtStaffNameAssigned;
    CircleImageView child_image;
    Button btnDeactivateChild, btnUpdateChild, btnDeAssignStaff;
    ViewPager viewPager;
    AlertDialogManager alertDialogManager = new AlertDialogManager();
    Child child;
    String profileImage;
    FloatingActionButton btnAssignTask;
    Pager adapter;
    private AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    UserDTO userDTO;
    List<UserDTO> staffDTOList = new ArrayList<>();
    List<AreaDTO> areaDtoList;
    List<TaskDTO> taskListDto;
    HashMap<String, List<TaskDTO>> expandableListDetail;
    Toolbar toolbar;
    Button btnExportPDF;

    private boolean staffAssigned;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.child_details, container, false);
        Bundle bundle = getArguments();
        if (null != bundle) {
            child = (Child) bundle.getSerializable("child");
        } else {
            child = new Child();
            child.setId(PrefUtils.getLongPreference(getContext(), PrefUtils.CHILD_ID, 0));
        }
        edtName = root.findViewById(R.id.edtName);
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
        edtStaffNameAssigned = root.findViewById(R.id.edtStaffNameAssigned);
        btnDeAssignStaff = root.findViewById(R.id.btnDeAssignStaff);
        getChildDetails(child);
//        if (child.getStaffAssigned()) {
//            edtStaffNameAssigned.setText(child.getStaff().getFirstname() + " " + child.getStaff().getLastName());
//            btnDeAssignStaff.setText("De-assign Staff");
//        } else {
//            btnDeAssignStaff.setText("Assign Staff");
//        }
        child_image = root.findViewById(R.id.child_image);

        btnDeactivateChild = root.findViewById(R.id.btnDeactivateChild);
        btnUpdateChild = root.findViewById(R.id.btnUpdateChild);

        viewPager = root.findViewById(R.id.view_pager);
        getAssignedTasks(child.getId());
        TabLayout tabs = root.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("About Child"));
        tabs.addTab(tabs.newTab().setText("Task List"));
        tabs.addTab(tabs.newTab().setText("Reports"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(viewPager);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        adapter = new Pager(fm, tabs.getTabCount());
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabs.setOnTabSelectedListener(this);
        btnAssignTask = root.findViewById(R.id.btnAssignTask);
        btnAssignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignNewTasks();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        btnUpdateChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChild();
            }
        });
        btnDeAssignStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deAssignStaff();
            }
        });
        btnDeactivateChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateChild();
            }
        });
        child_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImgePicker();
            }
        });
        return root;
    }

    private void getChildDetails(Child child) {

        showDialog("Get Child Info...");

        new APIClient(getActivity()).getApi().getUserDetailsById("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), child.getId()).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                hideDialog();
                if (response.code() == 200) {
                    userDTO = response.body();
                    edtName.setText(String.valueOf(userDTO.getFirstName() + " " + userDTO.getLastName()));
                    edtName.setEnabled(false);
                    edtAge.setText(String.valueOf(userDTO.getAge()));
                    edtParentName.setText(String.valueOf(userDTO.getParentName()));
                    edtParentEmail.setText(String.valueOf(userDTO.getEmail()));
                    edtContactNo.setText(String.valueOf(userDTO.getContact()));
                    edtOccupation.setText(String.valueOf(userDTO.getOccupation()));
                    edtTimeSlot.setText(String.valueOf(userDTO.getTimeSlot()));
                    UtilTransformer.transformUserDtoToChild(userDTO, child);
                    if (null != userDTO.getProfileImage()) {
                        String profileImage = userDTO.getProfileImage();
                        if (profileImage != null) {
                            String[] images = profileImage.split(",");
                            if (images.length > 0) {
                                String base64Image = images[images.length - 1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                child_image.setImageBitmap(decodedByte);
                            }
                        }
                    }
                    staffAssigned = child.getStaffAssigned();
                    if (child.getStaffAssigned()) {
                        edtStaffNameAssigned.setText(userDTO.getStaff().getFirstname() + " " + userDTO.getStaff().getLastName());
                        btnDeAssignStaff.setText("De-assign Staff");
                    } else {
                        btnDeAssignStaff.setText("Assign Staff");
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                hideDialog();
            }
        });
    }

    public void updateChild() {
        if (awesomeValidation.validate()) {
            UserDTO user = new UserDTO();
            String firstLastName = edtName.getText().toString();
            if (firstLastName.indexOf(" ") > 0) {
                user.setFirstName(firstLastName.substring(0, firstLastName.indexOf(" ")));
                user.setLastName(firstLastName.substring(firstLastName.indexOf(" ")));
            } else {
                user.setFirstName(firstLastName);
                user.setLastName("");
            }
            user.setAge(Integer.parseInt(edtAge.getText().toString()));
            user.setContact(edtContactNo.getText().toString());
            user.setEmail(edtParentEmail.getText().toString());
            user.setOccupation(edtOccupation.getText().toString());
            user.setParentName(edtParentName.getText().toString());
            user.setProfileImage(profileImage != null ? profileImage : child.getProfileImage());
            user.setTimeSlot(edtTimeSlot.getText().toString());
            user.setAboutMe(child.getAboutMe());
            user.setId(child.getId());

            new APIClient(getActivity()).getApi().updateProfile("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user)
                    .enqueue(new Callback<UserDTO>() {
                        @Override
                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            if (response.isSuccessful() || response.code() == 200) {
                                alertDialogManager.showAlertDialog(getActivity(), "Success", "Child Details updated successfully", true);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("child", child);
                                PrefUtils.setLongPreference(getContext(), PrefUtils.CHILD_ID, child.getId());
                                loadView(new ChildDetailsFragment());
                            } else {
                                alertDialogManager.showAlertDialog(getActivity(), "Failure", "Child Details update failed", false);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDTO> call, Throwable t) {

                        }
                    });
        }

    }

    public void deAssignStaff() {
        try {
            if (staffAssigned) {

                showDialog("De-Assign Staff ...");

                UserDTO user = new UserDTO();
                user.setId(child.getId());
                new APIClient(getActivity()).getApi().deAssignStafftoChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user)
                        .enqueue(new Callback<UserDTO>() {
                            @Override
                            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                                hideDialog();
                                if (response.isSuccessful() || response.code() == 200) {
                                    staffAssigned = false;
                                    alertDialogManager.showAlertDialog(getActivity(), "Success", "Staff de-Assigned successfully", true);
                                    edtStaffNameAssigned.setText("");
                                    btnDeAssignStaff.setText("Assign Staff");
                                } else {
                                    alertDialogManager.showAlertDialog(getActivity(), "Failure", "Staff de-Assigned failed", false);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserDTO> call, Throwable t) {
                                hideDialog();
                            }
                        });
            } else {

                loadStaffList();

                /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle("Select Staff");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
                new APIClient(getActivity()).getApi().getStaff("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<UserDTO>>() {
                    @Override
                    public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                        if (response.code() == 200) {
                            staffDTOList = new ArrayList<>();
                            assert response.body() != null;
                            //StaffDTO[] enums = gson.fromJson(yourJson, ChannelSearchEnum[].class);
                            staffDTOList =  response.body().subList(0, response.body().size());
                            for(UserDTO staff:staffDTOList){
                                arrayAdapter.add(staff.getId() +" "+staff.getFirstName()+" "+staff.getLastName());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                        Log.e("error", t.getMessage());
                    }
                });
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                int checkedItem=0;
                builderSingle.setSingleChoiceItems(arrayAdapter,checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        Log.i("AssignStaff", "onClick: Selected Staff Id:"+strName+" staffId:"+strName.substring(0,strName.indexOf(' ')));
                        String staffId=strName.substring(0,strName.indexOf(' '));
                        UserDTO user=new UserDTO();
                        StaffDTO staff=new StaffDTO();
                        staff.setId(Long.parseLong(staffId));
                        user.setStaff(staff);
                        user.setId(child.getId());
                        new APIClient(getActivity()).getApi().assignStafftoChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN),user).enqueue(new Callback<UserDTO>() {
                            @Override
                            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                                if (response.code() == 200) {
                                    if (response.isSuccessful() || response.code() == 200) {
                                        alertDialogManager.showAlertDialog(getActivity(), "Success", "Staff Assigned successfully", true);
                                        edtStaffNameAssigned.setText(strName.substring(strName.indexOf(' ')));
                                        btnDeAssignStaff.setText("De-assign Staff");
                                    } else {
                                        alertDialogManager.showAlertDialog(getActivity(), "Failure", "Staff Assigned failed", false);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<UserDTO> call, Throwable t) {
                                Log.e("error", t.getMessage());
                            }
                        });
                        dialog.dismiss();
                    }
                });
                builderSingle.show();*/


            }

        } catch (Exception e) {

        }

    }

    public void deactivateChild() {
        new APIClient(getActivity()).getApi().deactivateUser("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), child.getId())
                .enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        if (response.isSuccessful() || response.code() == 200) {
                            alertDialogManager.showAlertDialog(getActivity(), "Success", "Child deleted successfully", true);
                        } else {
                            alertDialogManager.showAlertDialog(getActivity(), "Failure", "Child deletion failed", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {

                    }
                });

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
                child_image.setImageBitmap(thumbnail);
            } else if (requestCode == REQUEST_IMAGE_CODE) {
                //Image Uri will not be null for RESULT_OK

                if (data != null) {

                    File file = ImagePicker.Companion.getFile(data);
                    if (file != null) {
                        Glide.with(getActivity()).load(file.getAbsolutePath()).error(R.mipmap.ic_launcher).into(child_image);
                        profileImage = FileUtils.generateImageData(file);
                    }
                }
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        Bundle bundle = new Bundle();
        bundle.putSerializable("child", child);
        if (tab.getPosition() == 1) {
            btnAssignTask.setVisibility(View.VISIBLE);
        } else {
            btnAssignTask.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void loadView(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void assignNewTasks() {

        ConcurrentMap<Long, TaskDTO> selectedTasks = new ConcurrentHashMap<>();
        ConcurrentMap<Long, TaskDTO> removedTasks = new ConcurrentHashMap<>();
        AlertDialog.Builder areaListDialog = new AlertDialog.Builder(getContext());
        areaListDialog.setTitle("Select Area");
        final ArrayAdapter<String> areaListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);

        new APIClient(getActivity()).getApi().getAreas("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<AreaDTO>>() {
            @Override
            public void onResponse(Call<List<AreaDTO>> call, Response<List<AreaDTO>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    areaDtoList = response.body().subList(0, response.body().size());
                    for (AreaDTO area : areaDtoList) {
                        areaListAdapter.add(area.getId() + " " + area.getAreaName());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<AreaDTO>> call, Throwable t) {
            }
        });
        areaListDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        int checkedItem = 0;
        areaListDialog.setSingleChoiceItems(areaListAdapter, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface areaDialog, int i) {
                String selectedArea = areaListAdapter.getItem(i);
                Long areaId = Long.parseLong(selectedArea.substring(0, selectedArea.indexOf(' ')));
                new APIClient(getActivity()).getApi().getTasksforArea("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), areaId).enqueue(new Callback<List<TaskDTO>>() {
                    @Override
                    public void onResponse(Call<List<TaskDTO>> call, Response<List<TaskDTO>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            taskListDto = response.body().subList(0, response.body().size());
                            AlertDialog.Builder taskListDialog = new AlertDialog.Builder(getContext());
                            taskListDialog.setTitle("Select Task");
                            ArrayList<String> taskList = new ArrayList<>();
                            ArrayList<Boolean> checkedTasks = new ArrayList<Boolean>();
                            final ArrayAdapter<String> taskListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_multichoice);
                            for (TaskDTO taskDto : taskListDto) {
                                taskList.add(taskDto.getId() + " " + taskDto.getTaskName());
                                taskListAdapter.add(taskDto.getId() + " " + taskDto.getTaskName());
                                //checkedTasks.add(false);
                                List<TaskDTO> assignedTasks = expandableListDetail.get(taskDto.getArea().getAreaName());
                                if (null != assignedTasks && !assignedTasks.isEmpty()) {
                                    for (TaskDTO assignedTask : assignedTasks) {
                                        if (assignedTask.getId() == taskDto.getId()) {
                                            checkedTasks.add(true);
                                            selectedTasks.put(assignedTask.getId(), taskDto);
                                        } else {
                                            checkedTasks.add(false);
                                        }
                                    }
                                } else {
                                    checkedTasks.add(false);
                                }
                            }
                            CharSequence[] taskListArray = new CharSequence[taskList.size()];
                            taskList.toArray(taskListArray);
                            Log.i("TaskList", "onClick: taskListArray:" + taskListArray.length);
                            boolean[] checkedTaskArray = new boolean[taskList.size()];
                            for (int j = 0; j < checkedTasks.size(); j++) {
                                checkedTaskArray[j] = checkedTasks.get(j);
                            }
                            Log.i("TaskList", "onClick: checkedTaskArray:" + checkedTaskArray.length);
                            Log.i("AreaADialog", "onClick: On Area Selection:" + selectedArea);
                            taskListDialog.setMultiChoiceItems(taskListArray, checkedTaskArray, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface taskDialog, int i, boolean b) {
                                    String task = new String(taskListArray[i].toString());
                                    Long taskId = new Long(task.substring(0, task.indexOf(' ')));
                                    if (b) {
                                        TaskDTO addedTask = new TaskDTO();
                                        addedTask.setId(taskId);
                                        selectedTasks.put(taskId, addedTask);
                                    } else {
                                        TaskDTO removedTask = new TaskDTO();
                                        removedTask.setId(taskId);
                                        removedTasks.put(taskId, removedTask);
                                    }
                                }
                            });
                            taskListDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            taskListDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("SelectedTask", "Selected Tasks List" + selectedTasks.toString());
                                    Log.i("RemovedTask", "Removed Tasks List" + selectedTasks.toString());
                                    Set<Long> selected = selectedTasks.keySet();
                                    Set<Long> removed = removedTasks.keySet();
                                    for (Long taskId : removed) {
                                        selected.remove(taskId);
                                    }
                                    for (Long taskId : selected) {
                                        removed.remove(taskId);
                                    }
                                    ChildTaskDTO addedChildTask = new ChildTaskDTO();
                                    addedChildTask.setId(child.getId());
                                    List<TaskDTO> addedTasks = new ArrayList<>();
                                    for (Long id : selected) {
                                        TaskDTO addedTask = new TaskDTO();
                                        addedTask.setId(id);
                                        addedTasks.add(addedTask);
                                    }
                                    addedChildTask.setTasks(addedTasks);

                                    ChildTaskDTO removedChildTask = new ChildTaskDTO();
                                    removedChildTask.setId(child.getId());
                                    List<TaskDTO> removedTasks = new ArrayList<>();
                                    for (Long id : selected) {
                                        TaskDTO addedTask = new TaskDTO();
                                        addedTask.setId(id);
                                        removedTasks.add(addedTask);
                                    }
                                    removedChildTask.setTasks(removedTasks);
                                    new APIClient(getContext()).getApi().assignTaskToChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), addedChildTask).enqueue(new Callback<ChildTaskDTO>() {
                                        @Override
                                        public void onResponse(Call<ChildTaskDTO> call, Response<ChildTaskDTO> response) {
                                            Toast.makeText(getContext(), "Tasks has been added Successfully!", Toast.LENGTH_LONG);
                                        }

                                        @Override
                                        public void onFailure(Call<ChildTaskDTO> call, Throwable t) {
                                        }
                                    });

                                    new APIClient(getContext()).getApi().deAssignTaskToChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), removedChildTask).enqueue(new Callback<ChildTaskDTO>() {
                                        @Override
                                        public void onResponse(Call<ChildTaskDTO> call, Response<ChildTaskDTO> response) {
                                        }

                                        @Override
                                        public void onFailure(Call<ChildTaskDTO> call, Throwable t) {
                                        }
                                    });
                                    dialogInterface.dismiss();
                                    areaDialog.dismiss();
                                }
                            });
                            taskListDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TaskDTO>> call, Throwable t) {

                    }
                });

            }

        });
        areaListDialog.show();
    }

    private boolean checkTaskAlreadyAssigned(Long taskId) {
        boolean taskPresent = false;
        List<TaskDTO> taskList = new ArrayList<>();
        for (List<TaskDTO> tasks : expandableListDetail.values()) {
            for (TaskDTO task : tasks) {
                if (task.getId().equals(taskId)) {
                    return true;
                }
            }
        }
        return taskPresent;
    }

    public void getAssignedTasks(Long childId) {
        new APIClient(getActivity()).getApi().getChildTasksByArea("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), (childId)).enqueue(new Callback<List<AreaTaskDTO>>() {
            @Override
            public void onResponse(Call<List<AreaTaskDTO>> call, Response<List<AreaTaskDTO>> response) {
                if (response.code() == 200) {
                    List<AreaTaskDTO> areaDTOS = response.body().subList(0, response.body().size());
                    expandableListDetail = new HashMap<>();
                    for (int i = 0; i < areaDTOS.size(); i++) {
                        expandableListDetail.put(areaDTOS.get(i).getName(), areaDTOS.get(i).getTasks());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AreaTaskDTO>> call, Throwable t) {
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

    private void loadStaffList() {

        showDialog("Loading Staff List...");

        new APIClient(getActivity()).getApi().getStaff("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN)).enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {

                hideDialog();

                if (response.code() == 200) {
                    ArrayList<String> staffList = new ArrayList<>();
                    staffDTOList = new ArrayList<>();
                    assert response.body() != null;
                    //StaffDTO[] enums = gson.fromJson(yourJson, ChannelSearchEnum[].class);
                    staffDTOList = response.body().subList(0, response.body().size());
                    for (UserDTO staff : staffDTOList) {
                        staffList.add(staff.getId() + " " + staff.getFirstName() + " " + staff.getLastName());
                    }

                    showStafSelectionDialog(staffList);
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                hideDialog();
                //Log.e("error", t.getMessage());
            }
        });
    }

    private void showStafSelectionDialog(ArrayList<String> staffList) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Staff");
        builder.setCancelable(true);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(staffList.toArray(new CharSequence[staffList.size()]), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FileUtils.showMessage(getActivity(), "Selected Positon :" + which);

                String strName = staffList.get(which);
                Log.i("AssignStaff", "onClick: Selected Staff Id:" + strName + " staffId:" + strName.substring(0, strName.indexOf(' ')));
                String staffId = strName.substring(0, strName.indexOf(' '));
                UserDTO user = new UserDTO();
                StaffDTO staff = new StaffDTO();
                staff.setId(Long.parseLong(staffId));
                user.setStaff(staff);
                user.setId(child.getId());

                submitAssignedStaffToChild(user, strName);

                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void submitAssignedStaffToChild(UserDTO user, String strName) {
        showDialog("Assigned Staff...");
        new APIClient(getActivity()).getApi().assignStafftoChild("Bearer " + PrefUtils.getStringPreference(getActivity(), PrefUtils.TOKEN), user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                hideDialog();
                if (response.code() == 200) {
                    if (response.isSuccessful() || response.code() == 200) {
                        staffAssigned = true;
                        alertDialogManager.showAlertDialog(getActivity(), "Success", "Staff Assigned successfully", true);
                        edtStaffNameAssigned.setText(strName.substring(strName.indexOf(' ')));
                        btnDeAssignStaff.setText("De-assign Staff");
                    } else {
                        alertDialogManager.showAlertDialog(getActivity(), "Failure", "Staff Assigned failed", false);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                hideDialog();
                // Log.e("error", t.getMessage());
            }
        });

    }

    AlertDialog spotsDialog;

    private void showDialog(String message) {

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
