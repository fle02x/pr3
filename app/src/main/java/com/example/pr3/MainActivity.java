package com.example.pr3;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mTextView;

    private EditText eTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        eTextView = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    public void onClick(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);

        final Call<List<Repos>> call = gitHubService.getRepos("goodwinSEV");

        call.enqueue(new Callback<List<Repos>>() {
                         @Override
                         public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                             // response.isSuccessfull() is true if the response code is 2xx
                             if (response.isSuccessful()) {
                                 // Выводим массив имён
                                 mTextView.setText(response.body().toString() + "\n");
                                 for (int i = 0; i < response.body().size(); i++) {
                                     // Выводим имена по отдельности
                                     mTextView.append(response.body().get(i).getName() + "\n");
                                 }

                                 mProgressBar.setVisibility(View.INVISIBLE);
                             } else {
                                 int statusCode = response.code();
                                 // Обрабатываем ошибку
                                 ResponseBody errorBody = response.errorBody();
                                 try {
                                     mTextView.setText(errorBody.string());
                                     mProgressBar.setVisibility(View.INVISIBLE);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<List<Repos>> call, Throwable throwable) {
                             mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
                         }
                     }
        );
    }
}