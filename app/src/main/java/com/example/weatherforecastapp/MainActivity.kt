package com.example.weatherforecastapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.databinding.ActivityMainBinding
import com.example.weatherforecastapp.model.BranchFinderService
import com.example.weatherforecastapp.model.NextDays
import com.example.weatherforecastapp.utils.epochToDate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import androidx.recyclerview.widget.ConcatAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

       sendRequest("17","51")

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showUp(list: List<NextDays>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val arrayList = arrayListOf<ExpandableAdapter>()

        for (item in list) {
            arrayList.add(ExpandableAdapter(item, this))
        }

        val concatAdapterConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()

        val concatAdapter = ConcatAdapter(concatAdapterConfig, arrayList)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = concatAdapter
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
                        //it.print()
                    },
                    {
                        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
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


