package com.example.weatherforecastapp.Ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentSecondBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Fragment created for handling Google Maps functionality
 * mainly for map movement and marker creation
 */

class MapSelectionFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private var lastLatLngMarker: MarkerOptions? = null
    private var shopLt: Double? = null
    private var shopLn: Double? = null

    private val callback = OnMapReadyCallback { googleMap ->
        //camera center above Europe (just felt like it)
        val center = LatLng(50.0, 10.0)
        val zoom = 1F

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom))
        googleMap.setOnMapClickListener {
            shopLt = it.latitude
            shopLn = it.longitude
            googleMap.clear()
            lastLatLngMarker = MarkerOptions().position(it)
            googleMap.addMarker(lastLatLngMarker!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.selectFromMapButton.setOnClickListener {
            //returning coordinates to the WeatherForecastFragment
            val locationResults = bundleOf()
            if (shopLn != null && shopLt != null) {
                locationResults.putDouble(getString(R.string.longitude), shopLn!!)
                locationResults.putDouble(getString(R.string.latitude), shopLt!!)
            }
            setFragmentResult(getString(R.string.data_key), locationResults)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

