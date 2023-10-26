package ma.projet.projetmap;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ma.projet.projetmap.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    String showUrl = "http://10.0.2.2/localisation/showPosition.php";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpMap();
    }
    private void setUpMap() {
        Log.d("Bouchra", "input" );

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showUrl, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String jsonResponse = response.toString();

                    JSONObject modifiedResponse = new JSONObject(jsonResponse);

                    if (modifiedResponse.has("positions")) {
                        JSONArray positions = modifiedResponse.getJSONArray("positions");
                        Log.d("MapActivity", "Positions retrieved: " + positions.toString());

                        int lieuCounter = 1;

                        for (int i = 0; i < positions.length(); i++) {
                            Log.d("boucle","oui");
                            JSONObject position = positions.getJSONObject(i);
                            double latitude = position.getDouble("latitude");
                            double longitude = position.getDouble("longitude");
                            String title = "Lieu " + lieuCounter;

                            Log.d("MapActivity", "Latitude: " + latitude + ", Longitude: " + longitude);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));

                            lieuCounter++;

                        }
                    } else {
                        Log.e("MapActivity", "No response.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MapActivity", "JSONException: " + e.getMessage());
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MapActivity", "VolleyError: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ensaj= new LatLng(33.25113396838997, -8.43412233288887);
        mMap.addMarker(new MarkerOptions().position(ensaj).title("ENSAJ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ensaj));
    }
}