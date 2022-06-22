package com.example.weatherforecastapp.Ui.forecastfragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentFirstBinding
import com.example.weatherforecastapp.model.BranchFinderService
import com.example.weatherforecastapp.model.NextDays
import com.example.weatherforecastapp.utils.makeSnackbar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

/**
 *
 */
class WeatherForecastFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var locationManager: LocationManager? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        setFragmentResultListener(getString(R.string.data_key)) { _, bundle ->
            // results from weather fragment parsing
            val resultLn = bundle.getDouble(getString(R.string.longitude), Double.MIN_VALUE)
            val resultLt = bundle.getDouble(getString(R.string.latitude), Double.MIN_VALUE)

            if (resultLn != Double.MIN_VALUE && resultLt != Double.MIN_VALUE) {
                binding.locationSearchText.setText(getLocationFromXY(resultLn, resultLt))
                sendRequest(resultLn.toString(), resultLt.toString())
            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationSearchButton.setOnClickListener {
            val location = binding.locationSearchText.text.toString()
            try {
                val gc = Geocoder(context)
                val res = gc.getFromLocationName(location, 1)

                if (res.size > 0) {
                    sendRequest(res[0].longitude.toString(), res[0].latitude.toString())
                } else {
                   makeSnackbar(view,getString(R.string.Invalid_string))
                }
            } catch (e: Exception) {
                makeSnackbar(view,e.message.toString())
            }

        }

        binding.selectOnMap.setOnClickListener {
            getUserLocation()
        }
        // custom functionality of choosing location from map
        binding.selectOnMap.setOnLongClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Custom location listener for handling location updates
     *  simply updates the location text field with the location
     */

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            binding.locationSearchText.setText(getLocationFromXY(location.longitude, location.latitude))
            locationManager?.removeUpdates(this)
            locationManager = null
        }
    }

    /** function returning name of the location from the given coordinates
     * @param longitude
     * @param latitude
     * @return string containing the location name
     */

    private fun getLocationFromXY(longitude: Double, latitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses[0].locality
    }

    /**
     * function which gets list of NextDay object and initializes an recyclerView with ExpandableAdapter adapter
     * @param list list of NextDays
     */

    private fun showUp(list: List<NextDays>) {
        binding.locationSearchGuideTxt.visibility = View.GONE
        val recyclerView = binding.recyclerView
        val adapter = ExpandableAdapter(list)


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

    }

    /**
     * function for getting user location
     */
    private fun getUserLocation(){
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsIsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsIsEnabled!!) {
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            builder.setMessage(
                getString(R.string.location_turned_off_message)
            )
                .setTitle(getString(R.string.need_gps))
                .setCancelable(false)
                .setPositiveButton(
                    getString(R.string.yes)
                ) { dialog, _ ->
                    val intent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    this.startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton(
                    getString(R.string.no)
                ) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()

        } else {
            try {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )

            } catch (ex: SecurityException) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
                )
            }

        }
    }

    /** function which gets coordinates and then sends a api request for the given location
     *  then invoking showUp function
     *  @param longitude
     *  @param latitude
     */


    private fun sendRequest(
        longitude: String,
        latitude: String,
    ) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            BranchFinderService.buildService().getForecast(
                latitude, longitude
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        showUp(it.fiveDays())
                    },
                    {
                        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
                        builder.setMessage(
                            it.message
                        )
                            .setTitle(getString(R.string.weather_forecast_error_downloading))
                            .setCancelable(false)
                            .setPositiveButton(
                                getString(R.string.ok)
                            ) { _, _ ->

                            }
                            .create()
                            .show()
                    })
        )

    }
}




