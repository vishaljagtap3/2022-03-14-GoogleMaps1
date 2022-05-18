package com.bitcode.goooglemaps1

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.bitcode.goooglemaps1.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var puneMarker: Marker
    private lateinit var mumbaiMarker: Marker

    private var count = 0
    private var markers = ArrayList<Marker>()
    private lateinit var circle : Circle
    private lateinit var polygon : Polygon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        initMapSettings()
        addMarkers()

        addOnMapClickListener()
        addOnMarkerClickListener()
        addOnInfoWindowClickListener()
        addOnMarkerDragListener()

        addShapes()
        setInfoWindowAdapter()
        misc()
    }

    private fun misc() {
        map.setOnPoiClickListener {
            mt("Poi: ${it.name}")
        }

        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this,
                R.raw.my_style
            )
        )
    }

    private fun setInfoWindowAdapter() {
        map.setInfoWindowAdapter(MyInfoWindowAdapter())
    }

    private inner class MyInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        override fun getInfoWindow(marker: Marker): View? {
            return null
        }

        override fun getInfoContents(marker: Marker): View? {

            var view = layoutInflater.inflate(R.layout.my_infowindow, null)

            var binding = com.bitcode.goooglemaps1.databinding.MyInfowindowBinding.bind(view)
            binding.txtTitle.setText(marker.title)
            binding.imgInfo.setImageResource(R.mipmap.ic_launcher)

            /*var txtTitle = view.findViewById<TextView>(R.id.txtTitle)
            var imgInfo = view.findViewById<ImageView>(R.id.imgInfo);
            txtTitle.setText(marker.title)
            imgInfo.setImageResource(R.mipmap.ic_launcher)*/

            return view
        }
    }

    private fun addShapes() {
        circle = map.addCircle(
            CircleOptions()
                .center(puneMarker.position)
                .radius(500.0)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(90, 255, 0, 0))
        )

        polygon = map.addPolygon(
            PolygonOptions()
                .add( LatLng(21.1458, 79.0882) )
                .add( LatLng(22.7196, 75.8577) )
                .add( LatLng(28.7041, 77.1025) )
                .add( LatLng(22.5726, 88.3639) )
                .fillColor(Color.argb(90, 0, 0, 255))

        )
    }

    private fun addOnMarkerDragListener() {
        map.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                    mt("Drag Start ${marker.title}")
                }

                override fun onMarkerDrag(marker: Marker) {
                    Log.e("tag", "Drag ${marker.title} -- ${marker.position.latitude} , ${marker.position.longitude}")
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    mt("Drag End ${marker.title}")
                }
            }
        )
    }


    private fun addOnInfoWindowClickListener() {
        map.setOnInfoWindowClickListener {
            mt("InfoWindow Click: ${it.title}")
        }
    }

    private fun addOnMarkerClickListener() {
        map.setOnMarkerClickListener(
            object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    mt("Marker Clicked: ${marker.title}")

                    return false
                }
            }
        )
    }

    private fun mt(text : String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun addOnMapClickListener() {
        map.setOnMapClickListener {

            markers.add(
                map.addMarker(
                    MarkerOptions()
                        .position(it)
                        .title("Marker $count")
                )!!
            )
            count++
            //var cameraUpdate = CameraUpdateFactory.newLatLng(puneMarker.position)
            //var cameraUpdate = CameraUpdateFactory.newLatLngZoom(puneMarker.position, 16F)
            //map.moveCamera(cameraUpdate)
            var cameraPosition = CameraPosition.Builder()
                .tilt(90F)
                .zoom(18F)
                .bearing(60F)
                .target(puneMarker.position)
                .build()

            var cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

            map.animateCamera(cameraUpdate, 5000, MyAnimationCancellableCallback())
        }


    }

    inner class MyAnimationCancellableCallback : GoogleMap.CancelableCallback {
        override fun onCancel() {
            mt("Animation Cancelled")
        }

        override fun onFinish() {
            mt("Animation finished")
        }
    }


    private fun addMarkers() {

        puneMarker = map.addMarker(
            MarkerOptions()
                .title("Pune")
                .snippet("This is Pune!")
                .position(LatLng(18.5204, 73.8567))
                .visible(true)
                .rotation(45F)
                .draggable(true)
                .anchor(0.5F, 0.5f)
                .zIndex(30F)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        )!!


        var scaledBitMap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.city), 100, 100, false
        )
        var cityIcon = BitmapDescriptorFactory.fromBitmap(scaledBitMap)

        mumbaiMarker = map.addMarker(
            MarkerOptions()
                .title("Mumbai")
                .snippet("this is Mumba")
                .icon(cityIcon)
                .position(LatLng(19.0760, 72.8777))
        )!!

    }

    @SuppressLint("MissingPermission")
    private fun initMapSettings() {

        map.isIndoorEnabled = true
        map.isMyLocationEnabled = true
        map.isBuildingsEnabled = true
        map.isTrafficEnabled = true

        //map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true
        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        //map.uiSettings.setAllGesturesEnabled(true)
    }
}