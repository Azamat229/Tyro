package com.student.tyro.Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.student.tyro.Model.StudentReview;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiCallInterface {

    //registration
    @Multipart
    @POST("registration")
    Call<JsonElement> User_signup(@Part("firstname") RequestBody first_name, @Part("lastname") RequestBody last_name, @Part("mobile") RequestBody mobile_no, @Part("email") RequestBody email, @Part("password") RequestBody password, @Part("device_name") RequestBody device_name, @Part("device_token") RequestBody device_token, @Part("type") RequestBody type);


    //Otp
    @GET("otpverification")
    Call<JsonElement> otpverification(@Query("user_id") String user_id, @Query("otp") String otp,
                                      @Query("device_name") String device_type, @Query("device_token") String device_token);

    //Resend Otp
    @GET("resendotp")
    Call<JsonElement> resend_otp(@Query("user_id") String user_id);

    //login
    @FormUrlEncoded
    @POST("login")
    Call<JsonElement> api_login(@Field("email") String email, @Field("password") String password,
                                @Field("device_name") String device_type, @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("inconvince")
    Call<JsonElement> inconvince(@Field("user_id") String user_id, @Field("email") String email);

    //forgot password
    @FormUrlEncoded
    @POST("forgotpassword")
    Call<JsonElement> Forgot_password(@Field("email") String email);

    //document upload
    @Multipart
    @POST("updatelicence")
    Call<JsonElement> upload_license(@Part("user_id") RequestBody user_id,
                                     @Part MultipartBody.Part licence);

    //get profile
    @GET("student_profile")
    Call<JsonElement> get_profile(@Query("student_id") String student_id);

//update profile

    @Multipart
    @POST("profileUpdate")
    Call<JsonElement> updateuser_profile(@Part("user_id") RequestBody user_id, @Part("firstname") RequestBody firstname, @Part("lastname") RequestBody lastname, @Part("phone") RequestBody phone, @Part("password") RequestBody password,
                                         @Part("city") RequestBody city, @Part("email") RequestBody email,
                                         @Part MultipartBody.Part profile_pic,
                                         @Part("about_us") RequestBody about_us,
                                         @Part("latitude") RequestBody latitude,
                                         @Part("longitude") RequestBody longitude);


//send location to server

    @FormUrlEncoded
    @POST("Updatelocation")
    Call<JsonElement> addAddress(@Field("user_id") String user_id, @Field("location") String address, @Field("longitude") String longitude, @Field("latitude") String latitude);

//terms and conditions

    @GET("terms_condtions")
    Call<JsonElement> get_termsconditions();

    //FAQ CATEGORIES
    @GET("faq_category")
    Call<JsonElement> get_categories();

//upcoming classes

    /* @GET("upcomingclasses")
     Call<JsonElement> Upcoming_classes();*/
    @Multipart
    @POST("upcomingclasses")
    Call<JsonElement> Upcoming_classes(@Part("user_id") RequestBody user_id);

    //completed classes

    @Multipart
    @POST("completed_student_classes")
    Call<JsonElement> Completed_classes(@Part("user_id") RequestBody user_id);

    //signup/login content

    @GET("signup_and_login")
    Call<JsonElement> get_signup_and_login_content();

    //Instructor list

    @GET("near_instructors_list")
    Call<JsonElement> get_instructorList(@Query("latitude") String latitude, @Query("longitude") String longtude);

    @GET("conversation_instructors_list")
    Call<JsonElement> conversation_instructors_list(@Query("student_id") String student_id);

    //Instructor details
    /*@Multipart
    @POST("user_instructor_profile")
    Call<JsonElement> get_instructordetails(@Part("user_id") RequestBody user_id);*/

    @GET("reviewsRatings")
    Call<JsonElement> instructor_profile_view(@Query("instructor_id") String instructor_id);

    //dates
   /* @Multipart
    @POST("slot_checkavalability")
    Call<JsonElement> get_slot(@Part("instructor_id") RequestBody instructor_id);
*/

    @GET("slot_checkavalability")
    Call<JsonElement> get_slot(@Query("instructor_id") String instructor_id);

    //getslots
    @FormUrlEncoded
    @POST("get_dateby_slots")
    Call<JsonElement> get_timeslot(@Field("instructor_id") String ins_id, @Field("booking_date") String date);

    //Covid rules
    @GET("covid_category")
    Call<JsonElement> get_covid_category();

    //Covid list
    @GET("covid_list")
    Call<JsonElement> get_covid_list(@Query("cat_id") String cat_id);

    //Conversation list
    @GET("instructor_conversations")
    Call<JsonElement> get_conversationlist();

    //reportcard
    @GET("rating_reportcard_student")
    Call<JsonElement> get_rating_reportcard_student(@Query("student_id") String student_id, @Query("instructor_id") String instructor_id, @Query("booking_id") String booking_id);

    //Signout
    @GET("signout")
    Call<JsonElement> signout(@Query("user_id") String user_id);

    //Rating
    @GET("student_review")
    Call<JsonElement> Givereviewtoinstructor(@Query("user_id") String user_id, @Query("instructor_id") String instructor_id, @Query("booking_id") String booking_id, @Query("rating") String rating, @Query("review") String review);

    @FormUrlEncoded
    @POST("review")
    Call<JsonElement> set_review_to_instructor(@Field("user_id") String userid,
                                               @Field("instructor_id") String id,
                                               @Field("booking_id") String cancel,
                                               @Field("rating") String rating,
                                               @Field("review") String reivew,
                                               @Field("type") String type);
    //cancel upcoming classes

    @FormUrlEncoded
    @POST("cancel_upcomingclass")
    Call<JsonElement> cancel_upcomingclass(@Field("user_id") String userid,
                                           @Field("id") String id,
                                           @Field("cancel_reason") String cancel,
                                           @Field("type") String type);

    //sliders
    @GET("terms_instructions")
    Call<JsonElement> terms_instructions();

    //confirm booking ***
    @FormUrlEncoded
    @POST("addbooking_class")
    Call<JsonElement> addbooking_class(@Field("student_id") String student_id, @Field("instructor_id") String instructor_id,
                                       @Field("start_time") String start_time, @Field("end_time") String end_time,
                                       @Field("booking_date") String booking_date, @Field("booking_hours") String booking_hours,
                                       @Field("latitude") String latitude, @Field("longitude") String longitude,
                                       @Field("payment_type") String payment_type, @Field("amount") String amount,
                                       @Field("pickup_location") String pickup_location,
                                       @Field("pi_latitude") String pi_latitude, @Field("pi_longitude") String pi_longitude, @Field("lesson_bde_status") String bde_status);//1 private 0 bde

    //Checkout ***
    @FormUrlEncoded
    @POST("checkout")
    Call<JsonElement> checkout(@Field("amount") String amount);

    //save card details
    @FormUrlEncoded
    @POST("save_cards")
    Call<JsonElement> save_card_details(@Field("user_id") String student_id,
                                        @Field("card_name") String card_name,
                                        @Field("card_number") String card_number,
                                        @Field("card_expiry") String card_expiry,
                                        @Field("card_type") String card_type,
                                        @Field("card_cvv") String card_cvv,
                                        @Field("zip_code") String zip_code,
                                        @Field("id") String id);

    //chat history
    @FormUrlEncoded
    @POST("chat_messages")
    Call<JsonElement> chat_history(@Field("user_id") String sender_id, @Field("receiver_id") String receiver_id);

    //send chat message

    @FormUrlEncoded
    @POST("messages")
    Call<JsonElement> Send_ChatText(@Field("sender_id") String sender_id, @Field("receiver_id") String receiver_id, @Field("message") String text);

    //chat message image
    @Multipart
    @POST("messages")
    Call<JsonElement> Send_ChatImage(@Part("sender_id") RequestBody sender_id, @Part("receiver_id") RequestBody receiver_id, @Part MultipartBody.Part profile_pic);


    //Notifications
    @GET("getNotificatins")
    Call<JsonElement> getnotifications(@Query("user_id") String userid);

    //Reschedule slot
    @FormUrlEncoded
    @POST("re_schedule_student")
    Call<JsonElement> reschedule_slot(@Field("id") String id, @Field("student_id") String student_id, @Field("instructor_id") String instructor_id, @Field("booking_hours") String booking_hours, @Field("booking_date") String booking_date, @Field("start_time") String start_time, @Field("end_time") String end_time, @Field("latitude") String latitude, @Field("longitude") String longitude);

    //Home scrren kms and hours
    @GET("student_home")
    Call<JsonElement> getdriven_details(@Query("student_id") String userid);

    //get cards ***
    @GET("user_savedcardList")
    Call<JsonElement> get_card(@Query("user_id") String user_id);

    //get filters
    @FormUrlEncoded
    @POST("rating_filters")
    Call<JsonElement> get_filters(@Field("gender") String gender, @Field("gear_type") ArrayList<String> gear_type,
                                  @Field("price") String price, @Field("rating") ArrayList<String> rating,
                                  @Field("latitude") String latitude, @Field("longitude") String longitude);

    //dete cards
    @FormUrlEncoded
    @POST("delete_card")
    Call<JsonElement> delecard(@Field("id") String id);

    //user check
    @GET("user_check")
    Call<JsonElement> checkuser(@Query("user_id") String user_id);

    //Instrucor available or not checking
    @FormUrlEncoded
    @POST("Updatelocation_student")
    Call<JsonElement> Updatelocation_student(@Field("user_id") String user_id, @Field("location") String address, @Field("longitude") String longitude, @Field("latitude") String latitude);


    @FormUrlEncoded
    @POST("read_chat_messages")
    Call<JsonElement> read_chat_messages(@Field("receiver_id") String receiver_id);

    @GET("bde_status")
    Call<JsonElement> bde_status(@Query("user_id") String user_id, @Query("bde_status") String bde_status);

//    @GET("student_review")
//    Call<StudentReview> student_review(@Query("instructor_id") String instructor_id);

    @GET("student_review")
    Call<List<StudentReview>> student_review(@Query("instructor_id") String instructor_id);

    @Multipart
    @POST("user_credits")
    Call<JsonElement> get_credits(@Part("user_id") RequestBody user_id);

    @FormUrlEncoded
    @POST("addbooking_class_by_credits")
    Call<JsonElement> add_booking_class_by_credits(@Field("student_id") String student_id,
                                                   @Field("instructor_id") String instructor_id,
                                                   @Field("start_time") String start_time,
                                                   @Field("end_time") String end_time,
                                                   @Field("booking_date") String booking_date,
                                                   @Field("booking_hours") String booking_hours,
                                                   @Field("latitude") String latitude,
                                                   @Field("longitude") String longitude,
                                                   @Field("pickup_location") String pickup_location,
                                                   @Field("pi_latitude") String pi_latitude,
                                                   @Field("pi_longitude") String pi_longitude,
                                                   @Field("lesson_bde_status") String bde_status);//1 private 0 bde


}





