package com.example.roadrunnerdriverrr.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.roadrunnerdriverrr.MyDataManager;
import com.example.roadrunnerdriverrr.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    DashboardFragment me = this;
    ConstraintLayout dashLay;
    MyDataManager myDataManager = new MyDataManager();
    private DashboardViewModel dashboardViewModel;
    private ListView listView;
    private ArrayList<String> listItem2 = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    LinearLayout listBgLayout;
    private TextView tvTotal, tvLabel1, tvLabel2, tvLabel3;
    private EditText edPostcode;
    private Button bAdd, bDelete, bClear;

    private int totalNo;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        listBgLayout = root.findViewById(R.id.listBgLay);
        listView = (ListView) root.findViewById(R.id.lvList);
        adapter = new ArrayAdapter<String>(me.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);
                Toast.makeText(me.getContext().getApplicationContext(),value,Toast.LENGTH_SHORT).show();

            }
        });

        tvLabel1 = (TextView) root.findViewById(R.id.tvLabel1);
        tvLabel2 = (TextView) root.findViewById(R.id.tvLabel2);
        tvLabel3 = (TextView) root.findViewById(R.id.tvLabel3);

        tvTotal = root.findViewById(R.id.tvTotal);
        tvTotal.setText("0");
        bAdd = root.findViewById(R.id.bAdd);
        bDelete = root.findViewById(R.id.bDelete);

        bDelete.setEnabled(myDataManager.getDeleteLastMarker());

        bClear = root.findViewById(R.id.bClear);

        edPostcode = root.findViewById(R.id.edPostcode);
        regEvents();
        Log.v("aa",myDataManager.getTotal() + "  ss");
        totalNo = 0;
        tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
        updateList();
        //tvList.setText(myDataManager.getListItem());
        dashLay = (ConstraintLayout) root.findViewById(R.id.layDash);
        if(myDataManager.getBlackTheme()){
            dashLay.setBackground(getContext().getDrawable(R.drawable.pizzabgdashblack));
            tvLabel1.setTextColor(Color.WHITE);
            tvLabel2.setTextColor(Color.WHITE);
            tvLabel3.setTextColor(Color.WHITE);
            edPostcode.setTextColor(Color.WHITE);
            tvTotal.setTextColor(Color.WHITE);
            listBgLayout.setBackgroundColor(Color.WHITE);
        }else {
            dashLay.setBackground(getContext().getDrawable(R.drawable.pizzabgdashwhite));
            tvLabel1.setTextColor(Color.BLACK);
            tvLabel2.setTextColor(Color.BLACK);
            tvLabel3.setTextColor(Color.BLACK);
            edPostcode.setTextColor(Color.BLACK);
            tvTotal.setTextColor(Color.BLACK);
            listBgLayout.setBackgroundColor(Color.TRANSPARENT);

        }
        return root;
    }

    public void updateList(){
        Log.v("aa","up");
        //listItem2 = new ArrayList<String>();
        adapter.notifyDataSetChanged();
        adapter.clear();
        int i = 0;
        while (i < myDataManager.getPostcodeArr().size()){
            String formattedItem = (i + 1) + ". " + myDataManager.getPostcodeArr().get(i);
            addItems(formattedItem);
            Log.v("aa","add["+(i-1) + "] = " + myDataManager.getPostcodeArr().get(i));
            i++;
        }
        adapter.notifyDataSetChanged();
        Log.v("aa","up2:" + myDataManager.getPostcodeArr().size());
    }

    public void addItems(String newItem) {
        Log.v("aa","adding:" + newItem);
        listItem2.add("" + newItem);
        adapter.notifyDataSetChanged();
    }

    private void addPostcode(String newPostcode){
        totalNo++;
        String listItem = "\n" + totalNo + ". " + "GL" + newPostcode;

        float tmpTotal = myDataManager.getTotal();
        int pc1 = Integer.parseInt(newPostcode.substring(0,1));
        int pc2 = Integer.parseInt(newPostcode.substring(1,2));
        if(pc1 == 1){
            //all just for pound
            //total++;
            myDataManager.addToTotal(1.0f);
        }else if(pc1 == 2){
            if(pc2 == 0){
                myDataManager.addToTotal(1.0f); // 1f
            }else if(pc2 == 2){
                myDataManager.addToTotal(2.0f);
            }else if(pc2 == 4){
                myDataManager.addToTotal(2.0f);
            }else if(pc2 == 5){
                myDataManager.addToTotal(1.0f);
            }else if(pc2 == 8){
                myDataManager.addToTotal(2.0f);
            }
        }else if(pc1 == 3){
            if(pc2 == 1){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 3){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 4){
                myDataManager.addToTotal(2.0f);
            }
        }else if(pc1 == 4){
            if(pc2 == 0){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 3){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 4){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 5){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 6){
                myDataManager.addToTotal(1.5f);
            }else if(pc2 == 8){
                myDataManager.addToTotal(2.0f);
            }

        }
        if(myDataManager.getTotal() != tmpTotal){
            //listItem = listItem + "    " + (myDataManager.getTotal() - tmpTotal);
            //tvList.append(listItem);
            //addIms(listItem);
            myDataManager.addPostcode(newPostcode);
        }

        tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
        updateList();
    }

    private void regEvents(){
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataManager.addPostcodeString(edPostcode.getText().toString());
                //tvList.setText(myDataManager.getListItem());
                updateList();
                tvTotal.setText("" + myDataManager.getTotal() + " " +  myDataManager.getPoundSymbol());
                myDataManager.setDeleteLastMarker(true);
                bDelete.setEnabled(true);
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataManager.deleteLast();
                updateList();
                tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
                myDataManager.setDeleteLastMarker(false);
                bDelete.setEnabled(false);

            }
        });

        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataManager.clearList();
                updateList();
                tvTotal.setText("" + myDataManager.getTotal() + " " + myDataManager.getPoundSymbol());
                myDataManager.setDeleteLastMarker(false);
                bDelete.setEnabled(false);
            }
        });


    }
}