package com.sonu.resolved.ui.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.sonu.resolved.MyApplication;
import com.sonu.resolved.R;

import com.sonu.resolved.di.ActivityContext;
import com.sonu.resolved.di.component.DaggerActivityComponent;
import com.sonu.resolved.di.module.ActivityModule;
import com.sonu.resolved.data.network.model.Problem;
import com.sonu.resolved.ui.login.LoginActivity;

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
        MainMvpView,
        ResultCallback<LocationSettingsResult>{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private static final int LOCATION_SETTINGS_CHANGE_REQUEST = 101;

    private Bitmap mDotMarkerBitmap;
    private GoogleMap problemsMap;
    private Observable<Location> locationObservable;
    private Observer<Location> myLocationObserver;
    private BottomSheetBehavior addProblemBottomSheetBehavior;
    private AlertDialog permissionExplanationAlertDialog;

    private ProgressDialog progressDialog;
    private ClusterManager<Problem> mClusterManager;

    @Inject
    MainMvpPresenter mPresenter;

    @Inject
    @ActivityContext
    Context mContext;

    @Inject
    GoogleApiClient mGoogleApiClient;

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

    @BindView(R.id.problemTitleEt)
    EditText problemTitleEt;

    @BindView(R.id.problemDescriptionEt)
    EditText problemDescriptionEt;

    @BindView(R.id.problemTitleTil)
    TextInputLayout problemTitleTil;

    @BindView(R.id.problemDescriptionTil)
    TextInputLayout problemDescriptionTil;

    @BindView(R.id.fabLayoutLl)
    LinearLayout fabLayoutLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MyApplication)getApplicationContext()).getApplicationComponent())
                .build()
                .inject(this);

        Log.i(TAG, "mPresenter="+mPresenter);

        ButterKnife.bind(this);

        addProblemBottomSheetBehavior = BottomSheetBehavior.from(addProblemRl);

        addProblemBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset == 1) {
                    fabLayoutLl.setVisibility(View.GONE);
                } else {
                    fabLayoutLl.setVisibility(View.VISIBLE);
                }

                addProblemFab
                        .animate()
                        .alpha(1-slideOffset)
                        .scaleX(1-slideOffset)
                        .scaleY(1-slideOffset)
                        .setDuration(0)
                        .start();

                relocateFab
                        .animate()
                        .alpha(1-slideOffset)
                        .scaleX(1-slideOffset)
                        .scaleY(1-slideOffset)
                        .setDuration(0)
                        .start();
            }
        });

        SupportMapFragment problemsMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.problemsMap);

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
                mPresenter.saveProblemBtnOnClick(
                        problemsMap.getCameraPosition().target.latitude,
                        problemsMap.getCameraPosition().target.longitude,
                        problemTitleEt.getText().toString().trim(),
                        problemDescriptionEt.getText().toString().trim());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.cancelBtnOnClick();
            }
        });

        initialiseDialogs();

        problemsMapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onAttach(this);
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        mPresenter.onDetach();
        super.onStop();
    }

    @Override
    public void focusCameraOn(double lat, double lng) {
        CameraUpdate cameraUpdate =
                CameraUpdateFactory
                        .newLatLngZoom(
                                new LatLng(
                                        lat,
                                        lng
                                ),
                                15
                        );

        problemsMap.animateCamera(cameraUpdate);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION:
                if (grantResults.length > 0) {

                    for (int grantResult : grantResults) {
                        if(grantResult != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }

                    relocateUser();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady():called");
        problemsMap = googleMap;
        mGoogleApiClient.connect();

        mClusterManager = new ClusterManager<Problem>(MainActivity.this, problemsMap);

        problemsMap.setOnCameraIdleListener(mClusterManager);
        problemsMap.setOnMarkerClickListener(mClusterManager);

        problemsMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = problemsMap.getCameraPosition();
                if(cameraPosition.zoom >= 15.0) {//on streets level
                    showAddProblemFab();
                } else {
                    hideAddProblemFab();
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected():called");
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
        checkLocationSettingsStatus();
    }

    @Override
    public void displayProblems(ArrayList<Problem> problems) {
        if(problems != null && problemsMap != null) {

            if(problems.size() == 0) {
                Toast.makeText(MainActivity.this, "Yay, no problems around you!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                for (Problem problem : problems) {
                    problemsMap.addMarker(
                            new MarkerOptions()
                                    .position(
                                            new LatLng(
                                                    problem.getLatitude(),
                                                    problem.getLongitude()
                                            )
                                    )
                    );
                }
            }
        }
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
    public void hideAddProblemFab() {
        addProblemFab.setVisibility(View.GONE);
    }

    @Override
    public void showAddProblemFab() {
        addProblemFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
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

    @Override
    public void addMarkerOnMap(Problem problem) {
        mClusterManager.addItem(problem);
    }

    @Override
    public void setProblemTitleError(String error) {
        if(error == null) {
            problemTitleTil.setErrorEnabled(false);
        } else {
            problemTitleTil.setError(error);
        }
    }

    @Override
    public void setProblemDescriptionError(String error) {
        if(error == null) {
            problemDescriptionTil.setErrorEnabled(false);
        } else {
            problemDescriptionTil.setError(error);
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                locationObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myLocationObserver);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(
                            MainActivity.this,
                            LOCATION_SETTINGS_CHANGE_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "onResult():error occurred while startResolutionForResult()");
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG,"onResult():LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult():called");
        switch (requestCode) {
            case LOCATION_SETTINGS_CHANGE_REQUEST:
                Log.d(TAG, "onActivityResult():LOCATION_SETTINGS_CHANGE_REQUEST");
                Log.i(TAG, "onActivityResult():LOCATION_SETTINGS_CHANGE_REQUEST:resultCode="
                        +resultCode);
                if(resultCode == 0) {
                    //do nothing
                } else if(resultCode == -1) {
                    locationObservable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(myLocationObserver);
                }
                break;
        }
    }

    private void askPermissions() {
        Log.d(TAG, "askPermissions():called");
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
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

    private void initialiseDialogs() {
        permissionExplanationAlertDialog =
                new AlertDialog
                        .Builder(MainActivity.this)
                        .setMessage("We need Location Permissions to show Problems around you. Please provide us permissions.")
                        .setPositiveButton("Provide Permissions", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askPermissions();
                                dialog.dismiss();
                            }
                        })
                        .create();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
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
                Log.d(TAG, "onSubscribe():called");
            }

            @Override
            public void onNext(Location value) {
                Log.d(TAG, "onNext():called");
                Log.i(TAG, "onNext():locationValue="+value);

                if(value != null) {

                    focusCameraOn(value.getLatitude(), value.getLongitude());

                    mClusterManager.clearItems();

                    problemsMap.addMarker(
                            new MarkerOptions()
                                    .position(
                                            new LatLng(
                                                    value.getLatitude(),
                                                    value.getLongitude()
                                            )
                                    )
                                    .icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)));

                    //getting problems accroding to user's location
                    mPresenter.getProblems(value.getLatitude(), value.getLongitude());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError():called");
                e.printStackTrace();

                if(e instanceof NullPointerException) {
                    //do nothing
                } else {
                    if(e.getMessage().equals("0")) {
                        handlePermissionsNotGranted();
                    }
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete():called");
            }
        };
    }

    private void handlePermissionsNotGranted() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

            Log.d(TAG, "getCurrentLocation():showing permission explanation");
            permissionExplanationAlertDialog.show();

        } else {
            Log.d(TAG, "getCurrentLocation():asking permissions");
            askPermissions();
        }
    }

    private void checkLocationSettingsStatus() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest());

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(MainActivity.this);
    }

    private Location getCurrentLocation() throws Exception{
        Log.d(TAG, "getCurrentLocation():called");

        Location currentLocation = getCurLatLon();

        Log.i(TAG, "getCurrentLocation():currentLocation:"+currentLocation);

        return currentLocation;
    }

    private Location getCurLatLon() throws Exception{
        if (
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

                        &&

                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "getCurrentLocation():permission not granted");
            throw new Exception("0");
        }

        return LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
    }
}
