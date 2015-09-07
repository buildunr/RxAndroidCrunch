package it.tiwiz.rxjavacrunch.part4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import it.tiwiz.rxjavacrunch.R;
import it.tiwiz.rxjavacrunch.part4.service.GitHubService;
import it.tiwiz.rxjavacrunch.part4.service.ServiceFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Part4Activity extends AppCompatActivity {

    private final static String TAG = "RxAndroidCrunch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part4);

        GitHubService gitHubService = ServiceFactory.createServiceFrom(GitHubService.class, GitHubService.ENDPOINT);

        gitHubService.getUserData("tiwiz")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Log.d(TAG, user.getName() + " - " + user.getRepos_url());
                }, throwable -> {
                    Log.d(TAG, "Error: " + throwable.getMessage());
                });

       gitHubService.getRepoData("tiwiz")
               .flatMap(Observable::from)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(repo -> {
                   Log.d(TAG, repo.getFull_name() + " - " + repo.getOwner().getLogin());
               }, throwable -> {
                   Log.d(TAG, "Error: " + throwable.getMessage());
               });

    }
}