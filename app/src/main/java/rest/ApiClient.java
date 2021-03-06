package rest;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends AppCompatActivity {

    //public static final String BASE_URL = "http://10.0.2.2/licentatry1/public/app/";
   // public static final String BASE_URL_1 = "http://10.0.2.2/licentatry1/public/";

   public static final String BASE_URL = "http://192.168.100.7/licentatry1/public/app/";
   public static final String BASE_URL_1 = "http://192.168.100.7/licentatry1/public/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}