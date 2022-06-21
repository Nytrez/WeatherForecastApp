package com.example.weatherforecastapp

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapp.databinding.FragmentFirstBinding
import com.example.weatherforecastapp.model.BranchFinderService
import com.example.weatherforecastapp.model.ExpandableAdapter
import com.example.weatherforecastapp.model.NextDays
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherForecastFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        setFragmentResultListener("dataLoc") { key, bundle ->
            // Any type can be passed via to the bundle
            val resultLn = bundle.getDouble("Longitude",-100.0)
            val resultLt = bundle.getDouble("Latitude",-100.0)
            Log.d("TEST", "into $resultLt $resultLn")
            if(resultLn != -100.0 && resultLt != -100.0){
                Log.d("TEST", "into $resultLt $resultLn")
                val geocoder = Geocoder(context)
                val list = geocoder.getFromLocation(resultLt, resultLn, 1)
                binding.locationSearchText.setText(list[0].locality)
                sendRequest(resultLn.toString(),resultLt.toString())
            }
            // Do something with the result...
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
                    sendRequest(res[0].longitude.toString(),res[0].latitude.toString())
                } else {
                    Snackbar
                        .make(view, "Invalid location", Snackbar.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                Log.d("MAIN", "location error")
            }

        }
        binding.locationSearchButton.setOnLongClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showUp(list: List<NextDays>) {
        val recyclerView = binding.recyclerView
        val adapter = ExpandableAdapter(list)

       recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
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
                            .setTitle("Something went wrong while downloading weather forecast")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Ok"
                            ) { _, _ ->

                            }
                            .create()
                            .show()
                    })
        )

    }
}




