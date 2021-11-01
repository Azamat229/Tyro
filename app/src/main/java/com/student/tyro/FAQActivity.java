package com.student.tyro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Adapter.FaqAdapter;
import com.student.tyro.Model.Catgory;
import com.student.tyro.Model.Faqcatgories;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends AppCompatActivity {
    NetworkConnection networkConnection;
    ArrayList<Catgory>faqcatgories;
    ArrayList<Faqcatgories>faqcatgories1;
    RecyclerView fq_layout;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        fq_layout=findViewById(R.id.cat_details);
        networkConnection = new NetworkConnection(FAQActivity.this);
        back=findViewById(R.id.ivBack);
        if (networkConnection.isConnectingToInternet())
        {
            Catgories();
        }
        else
        {
            Toast.makeText(FAQActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i=new Intent(FAQActivity.this,MainActivity.class);
                 startActivity(i);
                 finish();
             }
         });
    }

    private void Catgories() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_categories();
        final KProgressHUD hud = KProgressHUD.create(FAQActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();
                if (response.isSuccessful()) {
                    Log.e("response is", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String data = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array list1"+jsonArray);
                            faqcatgories = new ArrayList<>();
                            GridLayoutManager gridLayoutManager=new GridLayoutManager(FAQActivity.this,1);
                            fq_layout.setLayoutManager(gridLayoutManager);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String catgry=jsonObject1.getString("category");
                                Log.i("the catagory data is", "" + catgry);

                                String faq=jsonObject1.getString("faqList");
                                Log.i("the faq data is", "" + faq);

                                JSONArray jsonArray1 = new JSONArray(faq);
                                System.out.println("faq list"+jsonArray1);
                                faqcatgories1 = new ArrayList<>();
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                    String faq_id = jsonObject2.getString("id");
                                    String faq_catid= jsonObject2.getString("cat_id");
                                    String faq_ttl = jsonObject2.getString("title");
                                    String faq_desc = jsonObject2.getString("description");
                                    String faq_sts = jsonObject2.getString("status");
                                    String faq_creatd = jsonObject2.getString("created_on");
                                    System.out.println("faq faq_ttl"+faq_ttl);
                                    faqcatgories1.add(new Faqcatgories(faq_id, faq_catid, faq_ttl, faq_desc,faq_sts,faq_creatd));
                                  //  Log.i("the  data is", "" + faqcatgories);

                                }
                                faqcatgories.add(new Catgory(catgry,faqcatgories1));
                                }
                            fq_layout.setAdapter(new FaqAdapter(FAQActivity.this,faqcatgories,
                                    faqcatgories1));
                          /*  faqcatgories = new ArrayList<>();
                            GridLayoutManager gridLayoutManager=new GridLayoutManager(FAQActivity.this,1);
                            fq_layout.setLayoutManager(gridLayoutManager);
                            //faqcatgories.add(new Faqcatgories("", "", "", ""));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String catgry=jsonObject1.getString("category");
                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("category");
                                String status1 = jsonObject1.getString("status");
                                String created = jsonObject1.getString("created_on");
                                faqcatgories.add(new Faqcatgories(catgry,id, name, status1, created));
                                Log.i("the catagory data is", "" + faqcatgories);

                            }
                            fq_layout.setAdapter(new CategoryAdapter(FAQActivity.this,faqcatgories));*/
                        }



                    } catch (Exception e) {
                        //hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }


}