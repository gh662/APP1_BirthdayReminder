package com.antandbuffalo.birthdayreminder.settings;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.antandbuffalo.birthdayreminder.Constants;
import com.antandbuffalo.birthdayreminder.DataHolder;
import com.antandbuffalo.birthdayreminder.MainActivity;
import com.antandbuffalo.birthdayreminder.R;
import com.antandbuffalo.birthdayreminder.Util;
import com.antandbuffalo.birthdayreminder.about.About;
import com.antandbuffalo.birthdayreminder.database.DateOfBirthDBHelper;
import com.antandbuffalo.birthdayreminder.database.OptionsDBHelper;
import com.antandbuffalo.birthdayreminder.fragments.MyFragment;
import com.antandbuffalo.birthdayreminder.modifytoday.ModifyToday;

import java.util.Date;
import java.util.List;

/**
 * Created by i677567 on 28/8/15.
 */
public class Settings extends MyFragment {
    SettingsListAdapter settingsListAdapter;
    private SettingsViewModel settingsViewModel;
    LayoutInflater layoutInflater;
    SettingsModel selectedOption;
    public static Settings newInstance() {
        Settings fragment = new Settings();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        View rootView = inflater.inflate(R.layout.settings, container, false);

        final ListView settingsList = (ListView)rootView.findViewById(R.id.settingsList);

        settingsListAdapter = new SettingsListAdapter();
        //http://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
        settingsList.setAdapter(settingsListAdapter);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = settingsListAdapter.listData.get(position);
                if (selectedOption.getKey().equalsIgnoreCase(Constants.SETTINGS_WRITE_FILE)) {
                    if(getStoragePermission(Constants.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)) {
                        createBackup(true);
                    }
                } else if (selectedOption.getKey().equalsIgnoreCase(Constants.SETTINGS_READ_FILE)) {
                    if(getStoragePermission(Constants.MY_PERMISSIONS_READ_EXTERNAL_STORAGE)) {
                        restoreBackup(true);
                    }
                } else if (selectedOption.getKey().equalsIgnoreCase(Constants.SETTINGS_DELETE_ALL)) {
                    //put confirmation here
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Confirmation")
                            .setMessage("Are you sure you want to delete all?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(inflater.getContext(), DateOfBirthDBHelper.deleteAll(), Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < DataHolder.getInstance().refreshTracker.size(); i++) {
                                        DataHolder.getInstance().refreshTracker.set(i, true);
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else if (selectedOption.getKey().equalsIgnoreCase(Constants.SETTINGS_MODIFY_TODAY)) {
                    Intent intent = new Intent(view.getContext(), ModifyToday.class);
                    startActivity(intent);
                } else if (selectedOption.getKey().equalsIgnoreCase(Constants.SETTINGS_ABOUT)) {
                    Intent intent = new Intent(view.getContext(), About.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    public void createBackup(Boolean isGranted) {
        if(isGranted) {
            Util.writeToFile();
            Toast.makeText(layoutInflater.getContext(), "Backup file is created and stored in the location " + Constants.FOLDER_NAME + "/" + Constants.FILE_NAME + Constants.FILE_NAME_SUFFIX, Toast.LENGTH_LONG).show();
            Util.updateBackupTime(selectedOption);
            settingsListAdapter.refreshData();
        }
        else {
            Toast.makeText(layoutInflater.getContext(), "Please provide storage access to save the backup file", Toast.LENGTH_LONG).show();
        }
    }

    public void restoreBackup(Boolean isGranted) {
        if(isGranted) {
            String returnValue = Util.readFromFile(Constants.FILE_NAME);
            Toast.makeText(layoutInflater.getContext(), returnValue, Toast.LENGTH_SHORT).show();
            Util.updateRestoreTime(selectedOption);
            settingsListAdapter.refreshData();
            for (int i = 0; i < DataHolder.getInstance().refreshTracker.size(); i++) {
                DataHolder.getInstance().refreshTracker.set(i, true);
            }
        }
        else {
            Toast.makeText(layoutInflater.getContext(), "Please provide storage access to read the backup file", Toast.LENGTH_LONG).show();
        }
    }


    public Boolean getStoragePermission(int permissionType) {
        switch (permissionType) {
            case Constants.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                    return false;
                } else {
                    // Permission has already been granted
                    return true;
                }
            }
            case Constants.MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                    return false;
                } else {
                    // Permission has already been granted
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    createBackup(true);

                } else {
                    // permission denied, boo! Disable the
                    createBackup(false);
                }
                return;
            }
            case Constants.MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    restoreBackup(true);

                } else {
                    // permission denied, boo! Disable the
                    restoreBackup(false);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
