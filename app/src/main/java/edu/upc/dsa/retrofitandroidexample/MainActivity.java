package edu.upc.dsa.retrofitandroidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);
        final TextView txt = findViewById(R.id.textView);
        final TextView txtbtn = findViewById(R.id.textviewbtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast toast= Toast.makeText(MainActivity.this,"Button Clicked",Toast.LENGTH_SHORT);
                toast.show();
                txtbtn.setText("Button Clicked");


                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);
                Call<List<Repo>> repos = service.listRepos("Maite-Fernandez");
                repos.enqueue(new Callback<List<Repo>>() {

                    @Override
                    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {

                        Toast toast= Toast.makeText(MainActivity.this,"CORRECT",Toast.LENGTH_SHORT);
                        toast.show();
                        List<Repo> result = response.body();
                        assert result != null;
                        String str = "";
                        for (Repo r:result) {
                            str = r.toString() + "\n" + str;
                        }
                        txt.setText(str);
                    }

                    @Override
                    public void onFailure(Call<List<Repo>> call, Throwable t) {
                        Toast toast= Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });

    }
}