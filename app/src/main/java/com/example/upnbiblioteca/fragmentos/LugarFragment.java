package com.example.upnbiblioteca.fragmentos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.upnbiblioteca.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class LugarFragment extends Fragment {

    private final static String urlMostrarSedes ="https://bibliotecaupn2024.000webhostapp.com/Servicios/mostrarSedes.php";

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            AsyncHttpClient ahcMostrarSedes = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("id",-1);
            ahcMostrarSedes.get(urlMostrarSedes, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if(statusCode==200){
                        try {
                            JSONArray jsonArray = new JSONArray(rawJsonResponse);
                            for(int i=0;i<jsonArray.length();i++){
                                LatLng sede = new LatLng(jsonArray.getJSONObject(i).getDouble("latitud"),jsonArray.getJSONObject(i).getDouble("longitud"));
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(sede).title(jsonArray.getJSONObject(i).getString("nombre")));
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.upn32));
                                if (i==0)
                                 googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sede,17));

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });

            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);
            }
            else
                googleMap.setMyLocationEnabled(true);


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lugar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}