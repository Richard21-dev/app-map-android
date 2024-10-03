package com.example.androidmap;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;

import java.util.ArrayList;

public class FragmentArequipa extends Fragment {

    private MapView map;
    private TextView cordenadasText;
    private TextView distanciasText;
    private RoadManager roadManager;
    private AdminSQLiteData dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arequipa, container, false);

        Configuration.getInstance().setUserAgentValue(requireActivity().getPackageName());

        cordenadasText = view.findViewById(R.id.cordenadas);
        distanciasText = view.findViewById(R.id.distancia);
        map = view.findViewById(R.id.map);

        map.setMultiTouchControls(true);

        roadManager = new OSRMRoadManager(requireContext(), Configuration.getInstance().getUserAgentValue());
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_CAR);

        dbHelper = new AdminSQLiteData(requireContext());

        ArrayList<GeoPoint> points = getPointsFromDatabase();
        if (points != null) {

            for (int i = 0; i < points.size(); i++) {
                Marker marker = new Marker(map);
                marker.setPosition(points.get(i));
                marker.setIcon(getResources().getDrawable(getMarkerDrawable(i), null));
                marker.setTitle(getMarkerTitle(i));
                map.getOverlays().add(marker);
            }

            calculateAndDrawRoutes(points);

            if (!points.isEmpty()) {
                GeoPoint center = points.get(0);
                map.getController().setCenter(center);
                map.getController().setZoom(15.0);
            }
        }

        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                GeoPoint center = (GeoPoint) map.getMapCenter();
                updateCoordinates(center);
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                GeoPoint center = (GeoPoint) map.getMapCenter();
                updateCoordinates(center);
                return true;
            }
        });

        return view;
    }

    @SuppressLint("DefaultLocale")
    private void calculateAndDrawRoutes(ArrayList<GeoPoint> points) {
        new Thread(() -> {
            StringBuilder distanceString = new StringBuilder();
            double totalDistance = 0;

            for (int i = 0; i < points.size() - 1; i++) {
                ArrayList<GeoPoint> waypoints = new ArrayList<>();
                waypoints.add(points.get(i));
                waypoints.add(points.get(i + 1));

                Road road = roadManager.getRoad(waypoints);
                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                roadOverlay.setColor(Color.parseColor("#2200ff"));
                roadOverlay.setWidth(10);

                double distance = road.mLength;
                totalDistance += distance;

                final int segmentIndex = i;
                requireActivity().runOnUiThread(() -> {
                    map.getOverlays().add(roadOverlay);
                    distanceString.append(String.format("Distancia del punto %d al punto %d: %.2f km\n",
                            segmentIndex + 1, segmentIndex + 2, distance));
                });
            }

            ArrayList<GeoPoint> lastToFirstWaypoints = new ArrayList<>();
            lastToFirstWaypoints.add(points.get(points.size() - 1));
            lastToFirstWaypoints.add(points.get(0));
            Road lastToFirstRoad = roadManager.getRoad(lastToFirstWaypoints);
            double lastToFirstDistance = lastToFirstRoad.mLength;
            totalDistance += lastToFirstDistance;

            final double finalTotalDistance = totalDistance;
            requireActivity().runOnUiThread(() -> {
                Polyline lastToFirstRoadOverlay = RoadManager.buildRoadOverlay(lastToFirstRoad);
                lastToFirstRoadOverlay.setColor(Color.parseColor("#2200ff"));
                lastToFirstRoadOverlay.setWidth(10);
                map.getOverlays().add(lastToFirstRoadOverlay);
                distanceString.append(String.format("Distancia del último al primer punto: %.2f km\n", lastToFirstDistance));
                distanceString.append(String.format("Distancia total: %.2f km", finalTotalDistance));
                distanciasText.setText(distanceString.toString());
                map.invalidate();
            });
        }).start();
    }

    private ArrayList<GeoPoint> getPointsFromDatabase() {
        ArrayList<GeoPoint> points = new ArrayList<>();
        Cursor cursor = dbHelper.getAllPoints(AdminSQLiteData.TABLE_AREQUIPA); // Obtener puntos de Arequipa
        if (cursor.moveToFirst()) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(AdminSQLiteData.COLUMN_LATITUDE));
                double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(AdminSQLiteData.COLUMN_LONGITUDE));
                points.add(new GeoPoint(lat, lon));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return points;
    }

    private void updateCoordinates(GeoPoint center) {
        double lat = center.getLatitude();
        double lon = center.getLongitude();
        cordenadasText.setText(String.format("Lat: %.6f, Lon: %.6f", lat, lon));
    }

    private int getMarkerDrawable(int index) {
        switch (index) {
            case 0:
                return R.drawable.marcador1; // Asegúrate de que marker1 está en res/drawable
            case 1:
                return R.drawable.marcador2; // Asegúrate de que marker2 está en res/drawable
            case 2:
                return R.drawable.marcador3; // Asegúrate de que marker3 está en res/drawable
            case 3:
                return R.drawable.marcador4; // Asegúrate de que marker4 está en res/drawable
            case 4:
                return R.drawable.marcador5; // Asegúrate de que marker5 está en res/drawable
            default:
                return R.drawable.marcador1; // Usa marker_default como recurso por defecto
        }
    }

    private String getMarkerTitle(int index) {
        // Puedes definir títulos diferentes para cada marcador
        switch (index) {
            case 0:
                return "Plaza de Armas de Arequipa";
            case 1:
                return "Monasterio de Santa Catalina";
            case 2:
                return "Mirador de Yanahuara";
            case 3:
                return "Museo Santuarios Andinos";
            case 4:
                return "Molino de Sabandía";
            default:
                return "Punto de interés";
        }
    }
}