package com.example.roadrunnerdriverrr.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.roadrunnerdriverrr.MyDataManager;
import com.example.roadrunnerdriverrr.R;

public class SettingsFragment extends Fragment {
    MyDataManager myDataManager = new MyDataManager();
    ConstraintLayout settingsLay;


    private SettingsViewModel settingsViewModel;
    CheckBox chbStickyEdges, chbAutoGetPostcodeFromLoc, chbBlackMode;
    Button bSelDelCont, bSave;
    TextView tvDeliveryContNumber, tv1, tvNumberName, tvNumber;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        tv1 = root.findViewById(R.id.tv1);
        tvNumberName = root.findViewById(R.id.tvNumberName);
        tvNumber = root.findViewById(R.id.tvNumber);
        chbStickyEdges = root.findViewById(R.id.chbStickyEdges);
        Log.v("zz","st:" + myDataManager.getStickyEdges());
        chbStickyEdges.setChecked(myDataManager.getStickyEdges());

        chbStickyEdges.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myDataManager.setStickyEdges(isChecked);
            }
        });

        bSelDelCont = root.findViewById(R.id.bSelDelCont);
        bSelDelCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selDelContact();
            }
        });
       tvDeliveryContNumber = root.findViewById(R.id.tvNumber);
        chbAutoGetPostcodeFromLoc = root.findViewById(R.id.chbAutoGetPostcodeFromLoc);
        chbAutoGetPostcodeFromLoc.setChecked(myDataManager.getAutoGetPostcodeFromLoc());
        chbAutoGetPostcodeFromLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myDataManager.setAutoGetPostcodeFromLoc(isChecked);
            }
        });
        chbBlackMode = root.findViewById(R.id.chbBlackMode);
        chbBlackMode.setChecked(myDataManager.getBlackTheme());

        chbBlackMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myDataManager.setBlackTheme(isChecked);
            }
        });
        bSave = root.findViewById(R.id.bSave);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        settingsLay = (ConstraintLayout) root.findViewById(R.id.settingsLay);
        if(myDataManager.getBlackTheme()){
            settingsLay.setBackground(getContext().getDrawable(R.drawable.pizzabgsettingsblack));
            chbStickyEdges.setTextColor(Color.WHITE);
            tv1.setTextColor(Color.WHITE);
            tvNumberName.setTextColor(Color.WHITE);
            tvNumber.setTextColor(Color.WHITE);
            chbAutoGetPostcodeFromLoc.setTextColor(Color.WHITE);
            chbBlackMode.setTextColor(Color.WHITE);
        }else {
            settingsLay.setBackground(getContext().getDrawable(R.drawable.pizzabgsettingswhite));
            chbStickyEdges.setTextColor(Color.BLACK);
            tv1.setTextColor(Color.BLACK);
            tvNumberName.setTextColor(Color.BLACK);
            tvNumber.setTextColor(Color.BLACK);
            chbAutoGetPostcodeFromLoc.setTextColor(Color.BLACK);
            chbBlackMode.setTextColor(Color.BLACK);
        }
        return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.v("aa","vvvv");
    }

    private void saveData(){
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("stickyEdges", myDataManager.getStickyEdges());
        editor.putBoolean("autoGetPostcodeFromLoc", myDataManager.getAutoGetPostcodeFromLoc());
        editor.putBoolean("blackTheme", myDataManager.getBlackTheme());
        editor.apply();
    }

    private void selDelContact(){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent,1014);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1014 :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();

                    Cursor cur =  getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {// thats mean some resutl has been found
                        if(cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("Names", name);

                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {

                                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                                while (phones.moveToNext()) {
                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    myDataManager.setDeliveryConfirmationNumber(phoneNumber);
                                    tvDeliveryContNumber.setText(phoneNumber);
                                }
                                phones.close();
                            }

                        }
                    }
                    cur.close();
                }
                break;
        }

    }
}