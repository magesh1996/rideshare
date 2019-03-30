package com.magesh.rideshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Mapfordriver extends AppCompatActivity implements OnMapReadyCallback, LocationListener, RoutingListener {

    private LatLng latLng, start, end;

    List<Polyline> polylines;

    GoogleMap mMap;

    private static final int[] COLORS = new int[]{R.color.route};

    private Location currentlocation;

    LocationRequest locationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_REQUEST_CODE = 101;

    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userid);

    GeoFire geoFire = new GeoFire(dbRef);

    String cloc = "cloc";

    FloatingActionButton notifyfab;

    String oid;

    Marker carmarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfordriver);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        oid = b.getString("oid");
        double orilat = b.getDouble("orilat");
        double orilng = b.getDouble("orilng");
        double deslat = b.getDouble("deslat");
        double deslng = b.getDouble("deslng");


        start = new LatLng(orilat,orilng);
        end = new LatLng(deslat, deslng);

        polylines = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(Mapfordriver.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Mapfordriver.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Mapfordriver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

        }

        fetchLastLocation();

        notifyfab = findViewById(R.id.notifyfab);
        notifyfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("userid", userid);
                b.putString("oid",oid);
                startActivity(new Intent(getApplicationContext(), RequestedActivity.class).putExtras(b));
            }
        });

    }

    private void showRequestors() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child(oid).child("reqs");
        databaseReference.orderByChild("aord").equalTo("accepted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String requestorid = data.child("requestorid").getValue(String.class);
                    assert requestorid != null;
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(requestorid);
                    GeoFire reqcloc = new GeoFire(databaseReference1);
                    reqcloc.getLocation("cloc", new com.firebase.geofire.LocationCallback() {
                        @Override
                        public void onLocationResult(String key, GeoLocation location) {
                            double reqlat = location.latitude;
                            double reqlng = location.longitude;
                            LatLng reqloc = new LatLng(reqlat, reqlng);
                            mMap.addMarker(new MarkerOptions().position(reqloc).icon(BitmapDescriptorFactory.fromResource(R.drawable.personlocation)));
                            /*MarkerOptions reqmark = new MarkerOptions();
                            reqmark.position(reqloc);
                            reqmark.icon(BitmapDescriptorFactory.fromResource(R.drawable.personlocation));
                            mMap.addMarker(reqmark);*/

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void route() {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, end)
                .key("AIzaSyBNOGGHYlXOJ44JTyGYAMXCKXTnheWtouk")
                .build();
        routing.execute();
    }

    private void fetchLastLocation() {


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

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        },getMainLooper());

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentlocation = location;
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Mapfordriver.this);
                } else {
                    Toast.makeText(Mapfordriver.this, "No location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15));

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
        mMap.setMyLocationEnabled(false);
        mMap.setPadding(0,70,0,0);

        if (carmarker != null){
            carmarker.remove();
        }
        carmarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

        route();
        showRequestors();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                else {
                    Toast.makeText(Mapfordriver.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(),location.getLongitude());

        geoFire.setLocation(cloc, new GeoLocation(location.getLatitude(),location.getLongitude()));
    }

    @Override
    protected void onStop() {
        super.onStop();

        //geoFire.removeLocation(cloc);
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            //Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
        mMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
        mMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }
}
