/*
package com.student.tyro.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.R;
import com.student.tyro.activity.Adapter.InstructorsAdapter;
import com.student.tyro.activity.Model.Instructor;
import com.student.tyro.activity.Util.ApiCallInterface;
import com.student.tyro.activity.Util.NetworkConnection;
import com.student.tyro.activity.Util.Retrofit_Class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportCardsActivity extends AppCompatActivity {
    NetworkConnection networkConnection;
    RatingBar ratingbar_turning,ratingbar_reverse,ratingbar_left,ratingbar_right,ratingbar_stop,ratingbar_forwrdin,ratingbar_revrsein,ratingbar_pulling;
    RatingBar ratingbar_meetng,ratingbar_pedest,ratingbar_clearnce,ratingbar_crosing,ratingbar_distance,ratingbar_cyclist,ratingbar_lane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repoty_cards);
        networkConnection = new NetworkConnection(ReportCardsActivity.this);
        ratingbar_turning=findViewById(R.id.turningCar);
        ratingbar_reverse=findViewById(R.id.Reversing);
        ratingbar_left=findViewById(R.id.reverseleft);
        ratingbar_right=findViewById(R.id.reverseright);
        ratingbar_stop=findViewById(R.id.emergncy);
        ratingbar_forwrdin=findViewById(R.id.frwrd_in);
        ratingbar_revrsein=findViewById(R.id.reverse_in);
        ratingbar_pulling=findViewById(R.id.pulling);
        ratingbar_meetng=findViewById(R.id.meeting);
        ratingbar_pedest=findViewById(R.id.pedest);
        ratingbar_clearnce=findViewById(R.id.clearnce);
        ratingbar_crosing=findViewById(R.id.crosing);
        ratingbar_distance=findViewById(R.id.distance);
        ratingbar_cyclist=findViewById(R.id.cyclist);
        ratingbar_lane=findViewById(R.id.lane);
        if (networkConnection.isConnectingToInternet())
        {
            reportcard();
        }
        else
        {
            Toast.makeText(ReportCardsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        }

    private void reportcard() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_rating_reportcard_student("5","17");
        final KProgressHUD hud = KProgressHUD.create(ReportCardsActivity.this)
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
                            JSONObject dataobject = jsonObject.getJSONObject("reportcard");

                                String id = dataobject.optString("id");
                                String instructor_id = dataobject.optString("instructor_id");
                                String student_id = dataobject.optString("student_id");
                                String turning_car = dataobject.optString("turning_car");
                                String rev_staright_line = dataobject.optString("rev_staright_line");
                                String rev_left = dataobject.optString("rev_left");
                                String rev_right = dataobject.optString("rev_right");
                                String eme_stop = dataobject.optString("eme_stop");
                                String park_forward = dataobject.optString("park_forward");
                                String park_reversein = dataobject.optString("park_reversein");
                                String pulling_up = dataobject.optString("pulling_up");
                                String meeting_trafic = dataobject.optString("meeting_trafic");
                                String pedestrians = dataobject.optString("pedestrians");
                                String pedestrains_crossing = dataobject.optString("pedestrains_crossing");
                                String clear_obstruction = dataobject.optString("clear_obstruction");
                                String follow_distance = dataobject.optString("follow_distance");
                                String cyclists =dataobject.optString("cyclists");
                                String lane_discipline = dataobject.optString("lane_discipline");
                                String note = dataobject.optString("note");
                                String created_on = dataobject.optString("created_on");
                                    ratingbar_turning.setRating(Float.parseFloat(turning_car));
                                    ratingbar_reverse.setRating(Float.parseFloat(rev_staright_line));
                                    ratingbar_left.setRating(Float.parseFloat(rev_left));
                                    ratingbar_right.setRating(Float.parseFloat(rev_right));
                                    ratingbar_stop.setRating(Float.parseFloat(eme_stop));
                                    ratingbar_forwrdin.setRating(Float.parseFloat(park_forward));
                                    ratingbar_revrsein.setRating(Float.parseFloat(park_reversein));
                                    ratingbar_pulling.setRating(Float.parseFloat(pulling_up));
                                    ratingbar_meetng.setRating(Float.parseFloat(meeting_trafic));
                                    ratingbar_pedest.setRating(Float.parseFloat(pedestrians));
                                    ratingbar_clearnce.setRating(Float.parseFloat(clear_obstruction));
                                    ratingbar_crosing.setRating(Float.parseFloat(pedestrains_crossing));
                                    ratingbar_distance.setRating(Float.parseFloat(follow_distance));
                                    ratingbar_cyclist.setRating(Float.parseFloat(cyclists));
                                    ratingbar_lane.setRating(Float.parseFloat(lane_discipline));
                            }

                            hud.dismiss();

                    } catch (Exception e) {
                        hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });

    }
}
*/
