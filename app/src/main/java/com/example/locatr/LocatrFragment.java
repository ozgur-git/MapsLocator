package com.example.locatr;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocatrFragment extends Fragment {

    private static final String TAG="LocatrFragment";

//    private GoogleApiClient mClient;
    private ImageView mImageView;

    public static LocatrFragment newInstance(){
        return new LocatrFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        mClient=new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API)
//                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_locatr,container,false);
        mImageView=v.findViewById(R.id.image);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr,menu);

        MenuItem searchItem=menu.findItem(R.id.action_locate);
//        searchItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_locate:
                Log.i(TAG,"find image is called");
//                findImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
//        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mClient.disconnect();
    }

    private void findImage(){
//        LocationRequest request=LocationRequest.create();
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        request.setNumUpdates(1);
//        request.setInterval(0);
//        Log.i(TAG,"find image is called");
//        LocationServices.FusedLocationApi
//                .requestLocationUpdates(mClient, request, new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        Log.i(TAG,"Got a fix "+location);
//                    }
//                });
    }
}
