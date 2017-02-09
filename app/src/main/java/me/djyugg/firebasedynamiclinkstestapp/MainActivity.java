package me.djyugg.firebasedynamiclinkstestapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeCoogleApiClient();

        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                    @Override
                    public void onResult(@NonNull AppInviteInvitationResult result) {
                        Status status = result.getStatus();
                        if (status.isSuccess()) {
                            Intent intent = result.getInvitationIntent();
                            String deepLink = AppInviteReferral.getDeepLink(intent);

                            Toast.makeText(MainActivity.this, "Firebase Dynamic Linksから起動 URL: "
                                    + deepLink, Toast.LENGTH_SHORT).show();
                        } else {
                            Uri data = getIntent().getData();

                            if (data != null) {
                                Toast.makeText(MainActivity.this, "通常のディープリンクから起動 URL: "
                                        + data, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "通常起動", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        Toast.makeText(MainActivity.this, "Activity起動", Toast.LENGTH_SHORT).show();
    }

    private void initializeCoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
