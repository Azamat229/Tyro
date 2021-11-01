package com.student.tyro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Adapter.ConversationsAdapter;
import com.student.tyro.Model.Conversation;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationsActivity extends AppCompatActivity {

    NetworkConnection networkConnection;
    RecyclerView recycle_convers;
    ArrayList<Conversation> con;
    ImageView img_back;
    SearchView search;
    String lattude, longtude;
    String Rating;
    ConversationsAdapter conversationsAdapter;
    String user_id;
    TextView errorText;

    @Override
    protected void onResume() {
        super.onResume();
        serviceConversationList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        lattude = sharedPreferences.getString("lat", "");
        longtude = sharedPreferences.getString("long", "");
        user_id = sharedPreferences.getString("User_id", "");
        recycle_convers = (RecyclerView) findViewById(R.id.rvConversations);
        networkConnection = new NetworkConnection(ConversationsActivity.this);
        search = (SearchView) findViewById(R.id.etSearch);
        img_back = (ImageView) findViewById(R.id.ivBack);
        errorText = findViewById(R.id.errorText);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConversationsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        if (networkConnection.isConnectingToInternet()) {
            serviceConversationList();

        } else {
            Toast.makeText(ConversationsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filters(newText);
                return false;
            }
        });
    }

    private void serviceConversationList() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.conversation_instructors_list(user_id);
        final KProgressHUD hud = KProgressHUD.create(ConversationsActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();

                if (response.isSuccessful()) {
                    Log.e("spprofileee", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String data = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array is" + jsonArray);
                            con = new ArrayList<>();
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(ConversationsActivity.this, 1);
                            recycle_convers.setLayoutManager(gridLayoutManager);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String langu_age = jsonObject1.getString("language");
                                String inst_id = jsonObject1.getString("user_id");
                                String inst_first_name = jsonObject1.getString("firstname");
                                String inst_img = jsonObject1.getString("profile_pic");
                                String message = jsonObject1.getString("message");
                                String read_status = jsonObject1.getString("read_status");
                                int inst_rating = jsonObject1.getInt("rating");

                                Rating = Integer.toString((int) inst_rating);
                                con.add(new Conversation(langu_age, inst_id, inst_first_name, inst_img, Rating,
                                        "", message, read_status));
                                Log.i("the catagory data is", "" + con);
                            }
                            //recycle_instruct.setAdapter(new InstructorsAdapter(InstructorsActivity.this,instructorslist));
                            conversationsAdapter = new ConversationsAdapter(ConversationsActivity.this, con);
                            recycle_convers.setAdapter(conversationsAdapter);
                            hud.dismiss();

                            errorText.setVisibility(View.GONE);
                        } else {
                            errorText.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        errorText.setVisibility(View.VISIBLE);
                        hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hud.dismiss();
                errorText.setVisibility(View.VISIBLE);
                Log.e("sdfdsd ", t.toString());
            }
        });
    }

    private void filters(String text) {
        if (con != null && con.size() > 0) {
            ArrayList<Conversation> filterlist = new ArrayList<>();
            for (Conversation mainItemModel : con) {
                if (mainItemModel.getInstruct_firstname().toLowerCase().contains(text.toLowerCase())) {
                    filterlist.add(mainItemModel);
                    Log.i("filterlist", "" + filterlist);
                }
            }
            conversationsAdapter = new ConversationsAdapter(ConversationsActivity.this, filterlist);
            recycle_convers.setAdapter(conversationsAdapter);
        }
    }
}
