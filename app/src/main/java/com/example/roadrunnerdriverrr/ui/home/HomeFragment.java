package com.example.roadrunnerdriverrr.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.roadrunnerdriverrr.ChatHeadService;
import com.example.roadrunnerdriverrr.MyDataManager;
import com.example.roadrunnerdriverrr.R;


public class HomeFragment extends Fragment {

    MyDataManager myDataManager = new MyDataManager();
    private HomeViewModel homeViewModel;
    private Button bStartWidget;
    ConstraintLayout homeLay;
    TextView tvAppName, tvAppV;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tvAppName = root.findViewById(R.id.textView);
        tvAppV = root.findViewById(R.id.textView2);
        homeLay = (ConstraintLayout) root.findViewById(R.id.layHome);
        if(myDataManager.getBlackTheme()){
            homeLay.setBackground(getContext().getDrawable(R.drawable.pizzabghomeblack));
            tvAppName.setTextColor(Color.WHITE);
            tvAppV.setTextColor(Color.WHITE);
        }else {
            homeLay.setBackground(getContext().getDrawable(R.drawable.pizzabghomewhite));
            tvAppName.setTextColor(Color.BLACK);
            tvAppV.setTextColor(Color.BLACK);
        }

        bStartWidget = (Button) root.findViewById(R.id.bStartWidget);
        bStartWidget.setEnabled(myDataManager.getWidgetPerm());
        bStartWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatHead();
            }
        });
        return root;
    }

    private void startChatHead(){


        Intent chatIntent = new Intent(getActivity(), ChatHeadService.class);
        Log.v("z","aaaa");
        //startService(new Intent(MainActivity.this, ChatHeadService.class));
        getActivity().startService(chatIntent);


    }
}