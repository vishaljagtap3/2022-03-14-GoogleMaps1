package com.bitcode.goooglemaps1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class SVPActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {

    SupportStreetViewPanoramaFragment svpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svpactivity);

        svpFragment = (SupportStreetViewPanoramaFragment)
                getSupportFragmentManager().findFragmentById(R.id.svpFragment);

        svpFragment.getStreetViewPanoramaAsync(this);

    }


    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(37.754130, -122.447129));

    }
}