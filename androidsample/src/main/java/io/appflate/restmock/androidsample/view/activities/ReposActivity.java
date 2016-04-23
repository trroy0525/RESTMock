package io.appflate.restmock.androidsample.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ViewAnimator;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.appflate.restmock.androidsample.R;
import io.appflate.restmock.androidsample.SampleApplication;
import io.appflate.restmock.androidsample.domain.GithubApi;
import io.appflate.restmock.androidsample.model.Repository;
import io.appflate.restmock.androidsample.view.adapters.ReposRecyclerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposActivity extends AppCompatActivity implements Callback<List<Repository>> {

    private static final String PARAM_USERNAME = "username";
    private String username;

    @Inject GithubApi githubApi;

    @Bind(R.id.reposRecyclerView) RecyclerView reposRecyclerView;
    @Bind(R.id.reposAnimator) ViewAnimator reposAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ButterKnife.bind(this);
        SampleApplication.getComponent().inject(this);
        if (getIntent() != null) {
            username = getIntent().getStringExtra(PARAM_USERNAME);
        }
        setTitle(username);
        githubApi.getUserRepos(username).enqueue(this);
    }

    public static Intent intent(Activity activity,
                                String username) {
        Intent intent = new Intent(activity, ReposActivity.class);
        intent.putExtra(PARAM_USERNAME, username);
        return intent;
    }

    @Override
    public void onResponse(Call<List<Repository>> call,
                           Response<List<Repository>> response) {
        if (response.isSuccessful()) {
            reposRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false));
            reposRecyclerView.setAdapter(new ReposRecyclerAdapter(response.body()));
            reposAnimator.setDisplayedChild(1);
        } else {
            onResponseFailure();
        }
    }

    @Override
    public void onFailure(Call<List<Repository>> call,
                          Throwable t) {
        onResponseFailure();

    }

    private void onResponseFailure() {
        reposAnimator.setNextFocusDownId(1);

    }
}
