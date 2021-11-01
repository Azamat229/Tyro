package com.student.tyro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Model.MyCardsAdapter;
import com.student.tyro.Model.SavedCardModel;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCards extends AppCompatActivity {
    ArrayList<SavedCardModel>savedCardModels;
    MyCardsAdapter cards_pager;
    RecyclerView recycler_cards;
    ImageView back;
    String User_id;
    NetworkConnection networkConnection;
    LinearLayout linearnocards;
    TextView tvnocard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        recycler_cards = findViewById(R.id.cards_viewPager);
        back=findViewById(R.id.back_icon);
        networkConnection = new NetworkConnection(MyCards.this);
        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");
        linearnocards=findViewById(R.id.linear_nocards);
        tvnocard=findViewById(R.id.tv_nocards);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (networkConnection.isConnectingToInternet()) {
            get_card();
        }
        else {
            Toast.makeText(MyCards.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

    }
    public void get_card() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_card(User_id);
        KProgressHUD hud = KProgressHUD.create(MyCards.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();
                if (response.isSuccessful()) {
                    Log.e("spprofileee", response.body().toString());
                    System.out.println("data_image"+ response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String promocodes = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(promocodes);
                            savedCardModels=new ArrayList<>();
                            GridLayoutManager gridLayoutManager=new GridLayoutManager(MyCards.this,1);
                            recycler_cards.setLayoutManager(gridLayoutManager);
                            //card_details.setLayoutManager(new GridLayoutManager(StripePayment.this,1));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id=jsonObject1.getString("id");
                                String user_id=jsonObject1.getString("user_id");
                                String card_name=jsonObject1.getString("card_name");
                                String card_number=jsonObject1.getString("card_number");
                                String card_expiry=jsonObject1.getString("card_expiry");
                                String card_type=jsonObject1.getString("card_type");
                                String card_cvv=jsonObject1.getString("card_cvv");
                                String card_pincode=jsonObject1.getString("zip_code");
                                String created=jsonObject1.getString("created_on");

                                savedCardModels.add(new SavedCardModel(id,user_id,card_name,card_number,card_expiry,card_type,card_cvv,card_pincode,created));

                            }

                            if (savedCardModels != null) {
                                recycler_cards.setAdapter(new MyCardsAdapter(MyCards.this,savedCardModels));
                            }
                            if (savedCardModels.size() <= 0) {
                                //helperClass.showErrorDialog(SaveCardDetailsActivity.this, jsonObject.getString("message"));
                                recycler_cards.setVisibility(View.GONE);
                                linearnocards.setVisibility(View.VISIBLE);
                                tvnocard.setText("No Data Found");
                            }
                            }
                        else{

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Log.e("sdfdsd ", t.toString());
            }
        });

    }
}
