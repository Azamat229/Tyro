package com.student.tyro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.student.tyro.R;
public class ReferralFragment extends Fragment {
    AppCompatTextView btnSubmit;
    String  referral;
    TextView referal_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_referral, container, false);

        btnSubmit = root.findViewById(R.id.btnSubmit);
        referal_txt = root.findViewById(R.id.referal_txt);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        referral = sharedPreferences.getString("reference_code", "");

        referal_txt.setText(referral);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://demomaplebrains.com/tyro/login"));
                startActivity(sendIntent);
            }
        });


        return root;
    }
}