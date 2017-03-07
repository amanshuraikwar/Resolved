package com.sonu.resolved.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sonu.resolved.R;
import com.sonu.resolved.di.component.DaggerActivityComponent;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.models.Problem;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
 * Zoom Levels :
 * 1: World
 * 5: Landmass/continent
 * 10: City
 * 15: Streets
 * 20: Buildings
 */

public class MainActivity
        extends
        AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MainMvpView{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    MainMvpPresenter mPresenter;

    @Inject
    Context mContext;

    @Inject
    GoogleApiClient mGoogleApiClient;

    private Bitmap mDotMarkerBitmap;
    private GoogleMap problemsMap;
    private Observable<Location> locationObservable;
    private Observer<Location> myLocationObserver;
    private BottomSheetBehavior addProblemBottomSheetBehavior;

    @BindView(R.id.addProblemFab)
    FloatingActionButton addProblemFab;

    @BindView(R.id.relocateFab)
    FloatingActionButton relocateFab;

    @BindView(R.id.add_problem_bottom_sheet)
    RelativeLayout addProblemRl;

    @BindView(R.id.saveBtn)
    Button saveProblemBtn;

    @BindView(R.id.cancelBtn)
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);

        ButterKnife.bind(this);

        Log.i(TAG, "mPresenter="+mPresenter);
        mPresenter.onAttach(this);

        addProblemBottomSheetBehavior = BottomSheetBehavior.from(addProblemRl);

        addProblemBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d(TAG, "onSlide():offset="+slideOffset);
                if(slideOffset < 0.5) {
                    showFabs();
                } else {
                    hideFabs();
                }
            }
        });

        SupportMapFragment problemsMapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.problemsMap);

        problemsMapFragment.getMapAsync(this);

        initialiseMyLocationBitmap();
        initialiseMyLocationRxStuff();

        addProblemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addProblemFabOnClick();
            }
        });

        relocateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.relocateFabOnClick();
            }
        });

        saveProblemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveProblemBtnOnClick();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.cancelBtnOnClick();
            }
        });
    }

    private void initialiseMyLocationBitmap() {
        int px = getResources().getDimensionPixelSize(R.dimen.my_location_marker_size);
        mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        Drawable shape;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shape = getResources().getDrawable(R.drawable.dr_my_location_marker, null);
        } else {
            shape = getResources().getDrawable(R.drawable.dr_my_location_marker);
        }

        shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
        shape.draw(canvas);
    }

    private void initialiseMyLocationRxStuff() {

        locationObservable = Observable.fromCallable(
                new Callable<Location>() {
                    @Override
                    public Location call() throws Exception {
                        return getCurrentLocation() ;
                    }
                });

        myLocationObserver = new Observer<Location>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Location value) {
                Log.d(TAG, "onNext():called");
                CameraUpdate cameraUpdate =
                        CameraUpdateFactory
                                .newLatLngZoom(
                                        new LatLng(
                                                value.getLatitude(),
                                                value.getLongitude()
                                        ),
                                        10
                                );

                problemsMap.animateCamera(cameraUpdate);

                problemsMap.addMarker(
                        new MarkerOptions()
                                .position(
                                        new LatLng(
                                                value.getLatitude(),
                                                value.getLongitude()
                                        )
                                )
                                .icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)));

                mPresenter.getProblems(value.getLatitude(), value.getLongitude());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Location getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        return LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady():called");
        problemsMap = googleMap;
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected():called");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mPresenter.onGoogleApiConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void relocateUser() {
        Log.d(TAG, "relocateUser():called");
        locationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myLocationObserver);
    }

    @Override
    public void displayProblems(ArrayList<Problem> problems) {

    }

    @Override
    public void openAddProblemSheet() {
        addProblemBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void closeAddProblemSheet() {
        addProblemBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void hideFabs() {
        addProblemFab.setVisibility(View.GONE);
        relocateFab.setVisibility(View.GONE);
    }

    @Override
    public void showFabs() {
        addProblemFab.setVisibility(View.VISIBLE);
        relocateFab.setVisibility(View.VISIBLE);
    }
}
