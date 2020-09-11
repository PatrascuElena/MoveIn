package com.movein;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.PostSearchAdapter;
import adapter.SearchAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.PostModel;
import model.User;
import rest.ApiClient;
import rest.services.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostSearchActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_recy)
    RecyclerView searchRecy;
    PostSearchAdapter searchAdapter;
    List<PostModel> posts = new ArrayList<>();
     String phonenr = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        phonenr = getIntent().getStringExtra("number");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostSearchActivity.this, MainActivity.class));
            }
        });
        searchAdapter = new PostSearchAdapter(PostSearchActivity.this, posts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostSearchActivity.this);
        searchRecy.setLayoutManager(layoutManager);
        searchRecy.setAdapter(searchAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setIconified(false);
        //((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(getResources().getColor(R.color.hint_color));
        //((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.hint_color));
        //((ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn)).setImageResource(R.drawable.icon_clear);
        searchView.setQueryHint("Cauta o postare ");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFromDb(query, true);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2) {
                    searchFromDb(query, false);
                } else {
                    posts.clear();
                    searchAdapter.notifyDataSetChanged();
                }


                return true;
            }
        });

        return true;
    }
    private void searchFromDb(String query, boolean b) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("keyword", query);

        Call<List<PostModel>> call = userInterface.searchPost(params);
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostModel>> call, @NonNull Response<List<PostModel>> response) {

                posts.clear();
                posts.addAll(response.body());
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<PostModel>> call, @NonNull Throwable t) {

            }
        });
    }
}
