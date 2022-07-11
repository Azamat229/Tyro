package com.student.tyro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.student.tyro.Adapter.SvedCardAdapter;
import com.student.tyro.Model.SavedCardModel;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StripePaymentFullCourse extends AppCompatActivity {


    Button button_pay_now;
    CardInputWidget cardInputWidget;
    PaymentMethodCreateParams params;
    String cardNumber = "", cardName = "", nameOnCard = "", cardExpiry = "", cvv = "", zipcode = "";
    Stripe stripe;
    ProgressDialog progressDialog;
    String client_secret, public_key, transaction_id;
    String Grand_Total = "717.55";
    String Card_selection_string;
    TextView card_name;
    KProgressHUD hud, hud1;
    String User_id;
    String Card_id = "";
    String savecardid;
    TextView grand_total_txt;
    String Type;
    ImageView back_icon;
    CheckBox check_box;
    String sp_agree = "";
    String id, name, lang, rating, loc, date, start_time, end_time, hr, img, longt, lat, studentid, type;
    ArrayList<SavedCardModel> savedCardModels;
    SvedCardAdapter cards_pager;
    NetworkConnection networkConnection;
    RadioGroup card_rg;
    String radio_value;
    LinearLayout card_layout;
    private String pickup_location, student_lat, student_lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment_full_course);


        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, new IntentFilter("custom-User_Card"));
        networkConnection = new NetworkConnection(StripePaymentFullCourse.this);





//        Intent intent = getIntent();
//        Bundle b = intent.getExtras();
//        if (b != null) {
//            studentid = (String) b.get("student_id");
//            id = (String) b.get("instruct_id");
//            name = (String) b.get("username");
//            lang = (String) b.get("language");
//            rating = (String) b.get("rating");
//            loc = (String) b.get("location");
//            date = (String) b.get("bookingdate");
//            start_time = (String) b.get("strttme");
//            end_time = (String) b.get("endtme");
//            hr = (String) b.get("total_hrs");
//            img = (String) b.get("image");
//            longt = (String) b.get("longitude");
//            lat = (String) b.get("lat");
//            type = (String) b.get("Type");
//            pickup_location = (String) b.get("pickup_location");
//            student_lat = (String) b.get("student_lat");
//            student_lng = (String) b.get("student_lng");
//
//            //Toast.makeText(getApplicationContext(),lat+"longtitude"+longt,Toast.LENGTH_LONG).show();
//        }

//        System.out.println("location " + pickup_location + "\t" + student_lat + "\t" + student_lng);
//        System.out.println("intent result" + b.toString()); /// Azamat ****

        cardInputWidget = findViewById(R.id.cardInputWidget_pay);
        cardInputWidget.setPostalCodeEnabled(true);


        card_name = findViewById(R.id.card_name_pay);
        //card_details=findViewById(R.id.card_details_pay);
        grand_total_txt = findViewById(R.id.grand_total_txt_pay);
        grand_total_txt.setText("$" + Grand_Total);
        back_icon = findViewById(R.id.back_icon_pay);
//        check_box = findViewById(R.id.check_box_pay);
        card_rg = findViewById(R.id.card_rg_pay);
        card_layout = findViewById(R.id.card_layout_pay);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (networkConnection.isConnectingToInternet()) {
            Checkout();
        } else {
            Toast.makeText(StripePaymentFullCourse.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }


        button_pay_now = findViewById(R.id.btnPayNow_pay);
        button_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                params = cardInputWidget.getPaymentMethodCreateParams();

                if (Card_id.equals("")) {
                    if (card_name.getText().toString().isEmpty()) {
                        Toast.makeText(StripePaymentFullCourse.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    }
//                    else if (radio_value == null) {
//                        Toast.makeText(StripePayment.this, "Please select Card Type", Toast.LENGTH_SHORT).show();
//                        card_rg.setFocusable(true);
//                        card_rg.requestFocus();
//                    }
                    else if (params != null) {
                        hud1 = KProgressHUD.create(StripePaymentFullCourse.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setBackgroundColor(R.color.colorPrimary)
                                .show();

                        button_pay_now.setEnabled(false);
                        cardNumber = cardInputWidget.getCard().getNumber();

                        getCardType(cardNumber);

                        String exp = cardInputWidget.getCard().getExpMonth().toString();

                        if (exp.length() == 1) {
                            exp = "0".concat(exp);
                        }

                        nameOnCard = card_name.getText().toString();

                        cardExpiry = exp.concat("/")
                                .concat(String.valueOf(cardInputWidget.getCard().getExpYear())
                                        .substring(2));


                        cvv = cardInputWidget.getCard().getCvc();


                        zipcode = cardInputWidget.getCard().getAddressZip();


                        ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, client_secret);
                        Log.i("client_secret is", "" + client_secret);
                        final Context context = getApplicationContext();

                        stripe = new Stripe(context, public_key);
                        stripe.confirmPayment(StripePaymentFullCourse.this, confirmParams);

                    }
                } else {
                    hud = KProgressHUD.create(StripePaymentFullCourse.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setBackgroundColor(R.color.colorPrimary)
                            .show();
                    cardInputWidget.setVisibility(View.GONE);

                    // Send_Status(Card_id);


                }
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Card_id = "" + intent.getExtras().getString("User_Card_id");

            System.out.println("radiobuttonvalue" + Card_id);
            Log.e("Card_id", Card_id);
        }
    };



    public void Checkout() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.checkout(Grand_Total);

        final KProgressHUD hud = KProgressHUD.create(StripePaymentFullCourse.this)
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
                        Log.i("status is", "" + status);
                        if (status == 1) {
                            public_key = jsonObject.getString("public_key");
                            client_secret = jsonObject.getString("client_secret");
                            Log.i("client_secret is", "" + client_secret);
                        } else {
                            Toast.makeText(StripePaymentFullCourse.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        hud.dismiss();
                        e.printStackTrace();
                        Log.e("exception ", e.toString());
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hud.dismiss();
                Log.e("on_failure ", t.toString());

            }
        });

    }



    private void getCardType(String card_number) {

        String[] cardtype = {"Visa", "MasterCard", "MaestroCard", "American Express", "Discover", "Diners Club", "JCB"};

        ArrayList<String> listOfPattern = new ArrayList<String>();
        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);

        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);

        String pMaestroCard = "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{8,15}$";
        listOfPattern.add(pMaestroCard);

        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);

        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);

        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);

        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);


        if (!card_number.equalsIgnoreCase("")) {
            for (int i = 0; i < listOfPattern.size(); i++) {
                if (card_number.matches(listOfPattern.get(i))) {
                    cardName = cardtype[i];
                    Log.d("DEBUG", "if : " + cardName);
                }
            }
        }
    }



    public void showSaveCardDetails(String card_number, String card_expiry, String card_cvv, String cardname, String id) {
        try {
            cardInputWidget.setCardNumber(card_number);

            cardInputWidget.setCvcCode(card_cvv);
            card_name.setText(cardname);
            card_expiry = card_expiry.replace(" ", "");
            cardInputWidget.setExpiryDate(Integer.parseInt(card_expiry.trim().substring(0, card_expiry.indexOf("/"))),
                    Integer.parseInt(card_expiry.trim().substring(3)));

            savecardid = id;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        hud1.dismiss();
        stripe.onPaymentResult(requestCode, data, new StripePaymentFullCourse.PaymentResultCallback(StripePaymentFullCourse.this));
    }


    private class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {

        private final WeakReference<StripePaymentFullCourse> activityRef;

        public PaymentResultCallback(StripePaymentFullCourse activity) {

            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onError(@NotNull Exception e) {
//            bookOrderAPI();
            Toast.makeText(StripePaymentFullCourse.this, "Payment error", Toast.LENGTH_SHORT).show(); // Azamat



            final StripePaymentFullCourse activity = activityRef.get();
            /*    progressDialog.dismiss();*/


            button_pay_now.setEnabled(true);
            if (activity == null) {
                return;
            }

            //            displayAlert( Azamat откоментировать обезательно
            //                    "Payment Error info",
            //                    e.toString(),
            //                    false
            //            );


        }

        @Override
        public void onSuccess(PaymentIntentResult paymentIntentResult) {
            button_pay_now.setEnabled(true);
            final StripePaymentFullCourse activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = paymentIntentResult.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();

            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                /* progressDialog.dismiss();*/
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Log.d("Stripe", "onSuccess: " + gson.toJson(paymentIntent));
                Log.d("stripeModel", "onSuccess: " + paymentIntent.getClientSecret());

                button_pay_now.setEnabled(true);
                bookOrderAPI();


            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                //                Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                hud1.dismiss();
                button_pay_now.setEnabled(true);
                Toast.makeText(StripePaymentFullCourse.this, "Payment failed", Toast.LENGTH_SHORT).show();
                displayAlert(
                        "Payment info",
                        "Payment failed",
                        false
                );
            }
        }
    }



    private void bookOrderAPI() {
//save card details
        // Intent to home fragment ***
        // impl method to confirming payment way succussed
        // succecce payment amimation maybe
        Toast.makeText(StripePaymentFullCourse.this, "Payment was successfully", Toast.LENGTH_SHORT).show();

    }



    private void displayAlert(@NonNull String title,
                              @Nullable String message,
                              boolean restartDemo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StripePaymentFullCourse.this)
                .setTitle(title)
                .setMessage(message);
        if (restartDemo) {
            builder.setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            builder.setPositiveButton("Ok", null);

        }
        builder.create().show();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}