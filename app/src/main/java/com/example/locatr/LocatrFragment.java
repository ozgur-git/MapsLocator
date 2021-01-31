package com.example.locatr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.io.IOException;
import java.util.List;

public class LocatrFragment extends SupportMapFragment {

    private static final String TAG="LocatrFragment";
    private static final String[] LOCATION_PERMISSONS=new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSIONS=0;
    private GoogleApiClient mClient;
//    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Bitmap mMapImage;
    private Photo mMapItem;
    private Location mCurrentLocation;
    private GoogleMap mMap;

    public static LocatrFragment newInstance(){
        return new LocatrFragment();
    }

    private boolean hasLocationPermission(){
        int result= ContextCompat.checkSelfPermission(getActivity(),LOCATION_PERMISSONS[0]);
        return result== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mClient=new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        getMapAsync((googleMap)->mMap=googleMap);
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v=inflater.inflate(R.layout.fragment_locatr,container,false);
//        mImageView=v.findViewById(R.id.image);
//        mProgressBar=v.findViewById(R.id.progress_horizontal);
//        mProgressBar.setVisibility(View.VISIBLE);
//        mImageView.setVisibility(View.GONE);
//        return v;
//    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr,menu);

        MenuItem searchItem=menu.findItem(R.id.action_locate);
//        searchItem.setEnabled(true);
        searchItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_locate:
                if (hasLocationPermission()) {
                    Log.i(TAG, "find image is called");
                    findImage();
                } else {
                    requestPermissions(LOCATION_PERMISSONS,REQUEST_LOCATION_PERMISSIONS);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()){
                    findImage();
                }
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    private void findImage(){
        LocationRequest request=LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        Log.i(TAG,"find image is called");
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, location -> {
                    Log.i(TAG,"Got a fix "+location);
                    (new SearchTask()).execute(location);
                });
    }

    private void updateUI(){
        if (mMap==null||mMapImage==null) return;

        LatLng itemPoint=new LatLng(mMapItem.getLatitude(),mMapItem.getLongitude());
        LatLng myPoint=new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

        BitmapDescriptor itemBitmap= BitmapDescriptorFactory.fromBitmap(mMapImage);
        MarkerOptions itemMarker=new MarkerOptions()
                .position(itemPoint)
                .icon(itemBitmap);
        MarkerOptions myMarker=new MarkerOptions().position(myPoint);

        mMap.clear();
        mMap.addMarker(itemMarker);
        mMap.addMarker(myMarker);

        LatLngBounds bounds=new LatLngBounds.Builder()
                .include(itemPoint)
                .include(myPoint)
                .build();

        int margin=getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update= CameraUpdateFactory.newLatLngBounds(bounds,margin);
        mMap.animateCamera(update);

    }

    private class SearchTask extends AsyncTask<Location,Void,Void>{
        FlickrFetchr mFlickrFetchr=new FlickrFetchr();
        Bitmap mBitmap;
        Location mLocation;
        Photo mPhotoItem;

        @Override
        protected Void doInBackground(Location... locations) {
            mLocation=locations[0];
                List<Photo> photos=mFlickrFetchr.searchPhotos(mLocation);
                mPhotoItem=photos.get(0);
            try {
                Log.i("url_small",photos.get(0).getUrl_s());
                byte[] urlBytes=mFlickrFetchr.getUrlBytes(mPhotoItem.getUrl_s());
                mBitmap=BitmapFactory.decodeByteArray(urlBytes,0,urlBytes.length);
//                mImageView.setImageBitmap(BitmapFactory.decodeByteArray(mFlickrFetchr.getUrlBytes(photos.get(0).getUrl_s()),0,0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
//            mProgressBar.setVisibility(View.GONE);
            mMapImage=mBitmap;
            mMapItem=mPhotoItem;
            mCurrentLocation=mLocation;
            updateUI();
//            mImageView.setVisibility(View.VISIBLE);
//            mImageView.setImageBitmap(mBitmap);
        }
    }
}
