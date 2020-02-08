package com.example.roadrunnerdriverrr.ui.address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.roadrunnerdriverrr.MyDataManager;
import com.example.roadrunnerdriverrr.PostcodeIOManager;
import com.example.roadrunnerdriverrr.R;
import com.example.roadrunnerdriverrr.SingleDelivery;

import java.util.ArrayList;


public class AddressFragment extends Fragment {
    AddressFragment me = this;
    MyDataManager myDataManager = new MyDataManager();
    private AddressViewModel addressViewModel;
    private Button bStartWidget;
    ConstraintLayout addressLay;
    EditText etAddress, etPostcode;
    Button bAdd, bClear, bSetAllDel;
    ListView lvDelList;
    ArrayAdapter<String> adapter;
    TextView tvTotal, tvAdrLabel, tvPostcodeLabel, tvTotalLabel;
    int editPos = -1;

    private ArrayList<String> listItem = new ArrayList<String>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addressViewModel =
                ViewModelProviders.of(this).get(AddressViewModel.class);
        View root = inflater.inflate(R.layout.fragment_address, container, false);
        etAddress = root.findViewById(R.id.etAddress);
        etPostcode = root.findViewById(R.id.etPostcode);
        tvAdrLabel = root.findViewById(R.id.tvAdrLabel);
        tvPostcodeLabel = root.findViewById(R.id.tvPostcodeLabel);
        tvTotalLabel = root.findViewById(R.id.tvTotalLabel);
        tvTotal = root.findViewById(R.id.tvTotal);
        tvTotal.setText(myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
        bAdd = root.findViewById(R.id.bAddDel);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPostcode.getText().toString().length() > 3) {
                    if(PostcodeIOManager.validatePostcode(etPostcode.getText().toString())) {
                        etPostcode.setText(etPostcode.getText().toString().toUpperCase());
                        myDataManager.addDelivery(etAddress.getText().toString(), etPostcode.getText().toString());
                        etPostcode.setHint(etPostcode.getText().toString());
                        etPostcode.setText("");
                        etAddress.setHint(etAddress.getText().toString());
                        etAddress.setText("");
                        //tvTotal.setText("" + myDataManager.getTotal());
                        loadAllDel();
                    }else {
                        Toast.makeText(getContext().getApplicationContext(),"Something wrong with postcode!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext().getApplicationContext(),"Put postcode first",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bSetAllDel = root.findViewById(R.id.bSetAllDel);
        bSetAllDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                while (i<myDataManager.getDelArr().size()){
                    myDataManager.getDelArr().get(i).setStatusDelivered(true);
                    myDataManager.getDelArr().get(i).setCurDateAsDelDate();
                    i++;
                }
            }
        });

        bClear = root.findViewById(R.id.bClear);
        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataManager.clearDeliveries();
                tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
                loadAllDel();
                etPostcode.setText("");
                etAddress.setText("");
            }
        });

        lvDelList =  root.findViewById(R.id.lvDelList);
        adapter = new ArrayAdapter<String>(me.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        lvDelList.setAdapter(adapter);
        registerForContextMenu(lvDelList);
        lvDelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);
                //Toast.makeText(me.getContext().getApplicationContext(),value,Toast.LENGTH_SHORT).show();
                //myDataManager.getDelArr().get(position).flipStatusDelivered();
                if(!myDataManager.getDelArr().get(position).getStatusDelivered()){
                    myDataManager.getDelArr().get(position).flipStatusDelivered();
                    myDataManager.addToTotal(myDataManager.getDelArr().get(position).getReward());
                    myDataManager.getDelArr().get(position).setCurDateAsDelDate();
                    tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
                    loadAllDel();
                }else {
                    showDeleteConfDialog(position);
                }


            }
        });
        lvDelList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return true;
            }
        });
        loadAllDel();
        addressLay = (ConstraintLayout) root.findViewById(R.id.layAddress);
        if(myDataManager.getBlackTheme()){
            addressLay.setBackground(getContext().getDrawable(R.drawable.pizzabghomeblack));
            tvAdrLabel.setTextColor(Color.WHITE);
            tvPostcodeLabel.setTextColor(Color.WHITE);
            tvTotalLabel.setTextColor(Color.WHITE);
            etPostcode.setTextColor(Color.WHITE);
            etAddress.setTextColor(Color.WHITE);
            lvDelList.setBackgroundColor(Color.WHITE);
            etAddress.setHintTextColor(Color.GRAY);
        }else {
            addressLay.setBackground(getContext().getDrawable(R.drawable.pizzabghomewhite));
            tvAdrLabel.setTextColor(Color.GRAY);
            tvPostcodeLabel.setTextColor(Color.GRAY);
            tvTotalLabel.setTextColor(Color.GRAY);
            etPostcode.setTextColor(Color.GRAY);
            etAddress.setTextColor(Color.GRAY);
            lvDelList.setBackgroundColor(Color.TRANSPARENT);
            etAddress.setHintTextColor(Color.GRAY);
        }
        return root;
    }

    private void showDeleteConfDialog(final int pos){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warning")
                .setMessage("Delete ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myDataManager.getDelArr().get(pos).flipStatusDelivered();
                        myDataManager.takeFromTotal(myDataManager.getDelArr().get(pos).getReward());
                        myDataManager.getDelArr().get(pos).setDateOfDelivery("");
                        tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
                        loadAllDel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(0, v.getId(), 0, "Info");
        menu.add(0,v.getId(),0,"Edit");
        menu.add(0, v.getId(), 0, "Delete");

    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int pos=info.position;
        if(item.getTitle()=="Delete"){
            Toast.makeText(this.getContext().getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            myDataManager.delDelivery(pos);
            loadAllDel();
            tvTotal.setText(myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
        }
        if(item.getTitle() == "Info"){
            createInfoDialog(myDataManager.getDelArr().get(pos).getPostcode());
        }
        if(item.getTitle() == "Edit"){
            editPos = pos;
            createEditDialog(myDataManager.getDelArr().get(pos));

        }
        return true;
    }

    private void createInfoDialog(String postcode) {
        ArrayList<String> arrayList = PostcodeIOManager.getPostcodeDetails(postcode);
        CharSequence[] cs = arrayList.toArray(new CharSequence[arrayList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Info")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // close dialog
                    }
                })

                .setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createEditDialog(final SingleDelivery editDelivery) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_edit_dialog);
        dialog.setTitle("Edit");
        final EditText etAddressEdit = (EditText) dialog.findViewById(R.id.etAddressEdit);
        etAddressEdit.setText(editDelivery.getAddress());
        final EditText etPostcodeEdit = (EditText) dialog.findViewById(R.id.etPostcodeEdit);
        etPostcodeEdit.setText(editDelivery.getPostcode());
        final CheckBox chbIsDelivered = (CheckBox) dialog.findViewById(R.id.chbIsDelivered);
        chbIsDelivered.setChecked(editDelivery.getStatusDelivered());
        final EditText etDeliveryDate = (EditText) dialog.findViewById(R.id.etDeliveryDate);
        etDeliveryDate.setText(editDelivery.getDateOfDelivery());
        if(chbIsDelivered.isChecked()){
            etDeliveryDate.setEnabled(true);
        }else {
            etDeliveryDate.setEnabled(false);
        }
        chbIsDelivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etDeliveryDate.setEnabled(isChecked);
            }
        });
        Button bSaveEdit = (Button) dialog.findViewById(R.id.bSaveEdit);
        bSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDelivery.setAddress(etAddressEdit.getText().toString());
                editDelivery.setPostcode(etPostcodeEdit.getText().toString());
                if(chbIsDelivered.isChecked()){
                    editDelivery.setDateOfDelivery(etDeliveryDate.getText().toString());
                }else {
                    editDelivery.flipStatusDelivered();
                }
                myDataManager.getDelArr().set(editPos,editDelivery);
                loadAllDel();
                dialog.dismiss();
            }
        });

        Button bCancelEdit = (Button) dialog.findViewById(R.id.bCancelEdit);
        bCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void loadAllDel(){
        listItem.clear();
        adapter.clear();
        int i = 0;
        while (i<myDataManager.getDelArr().size()){
            listItem.add((i + 1) + ". " + myDataManager.getDelArr().get(i).getExportString());
            i++;
        }
        adapter.notifyDataSetChanged();

    }

}