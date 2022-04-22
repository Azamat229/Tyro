package com.student.tyro.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Class {

    private static Retrofit retrofit = null;
    private static Retrofit skill_retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()

                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://162.240.47.148/~tyrodriving/Mobile/")
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
          /*  retrofit = new Retrofit.Builder()
                    .baseUrl(Constants_Urls.check_user_url)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();*/
        }
        return retrofit;
    }



    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
             // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            OkHttpClient okHttpClient = builder
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
