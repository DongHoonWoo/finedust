package dust.fine.`fun`.studio.finedust

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dust.fine.`fun`.studio.finedust.BuildConfig.APPLICATION_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.studio.woo.finedust.api.AirPollutionInfoService
import me.studio.woo.finedust.model.AirQualityData
import me.studio.woo.finedust.model.MeasurmentSatationResponse
import retrofit2.Call

class
MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    private lateinit var airlevel: TextView
    private lateinit var icon: ImageView
    private lateinit var button: View
    private lateinit var latitudeText: TextView
    private lateinit var longitudeText: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        airlevel = findViewById(R.id.air_level)
        icon = findViewById(R.id.icon)
//        latitudeText = findViewById(R.id.latitude_text)
//        longitudeText = findViewById(R.id.longitude_text)

        button = findViewById(R.id.button) as View
        button.setOnClickListener { _ ->
            //            NetworkCall1(resultTextView).execute(
//                MeasurementStationInfoService.create().getNearByStationByAddr(
//                    "풍덕천동")
//            )

//            MeasurementStationInfoService.create().getNearByStationByAddrRx("풍덕천동")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe { result ->
//                    result.list.let {
//                        MeasurementStationInfoService.create()
//                            .getNearByStationByTmXYRx(it.get(0).tmX, it.get(0).tmY)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(Schedulers.io())
//                            .subscribe { result -> resultTextView.text = result.toString() }
//                    }
//                }

//            MeasurementStationInfoService.create().getNearByStationByAddrRx("풍덕천동")
//                .subscribeOn(Schedulers.io())
//                .doOnNext { result ->
//                    result.list.let {
//                        MeasurementStationInfoService.create()
//                            .getNearByStationByTmXYRx(it.get(0).tmX, it.get(0).tmY)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(Schedulers.io())
//                            .subscribe { result -> resultTextView.text = result.toString() }
//                    }
//                }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()


//            MeasurementStationInfoService.create().getNearByStationByAddrRx("풍덕천동")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .blockingForEach {
//                    res ->  res.list.forEach { statonInfo ->
//                    MeasurementStationInfoService.create().getNearByStationByTmXYRx(statonInfo.tmX, statonInfo.tmY)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe { result -> resultTextView.text = result.toString()}
//                }}

            AirPollutionInfoService.create().getAirPollutionByMsrStationRx("수지")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    result.list.let {
                        //                        resultTextView.text = it.get(0).toString()
                        setUI(it.get(0))
                    }
                }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            Log.d(TAG, "locationo : " + location)fddfdfd
//        }
    }

    private fun setUI(airQualityData: AirQualityData) {
        val pm_10Item = AirLeveIItemView(findViewById(R.id.pm_10))
        val pm_25Item = AirLeveIItemView(findViewById(R.id.pm_25))


        var value = airValueToInt(airQualityData.pm10Value)
        AIR_LEVEL.get(AirQualityData.getPM10Level(value))?.let {
            pm_10Item.setData(it, airQualityData.pm10Value)
        }

        value = airValueToInt(airQualityData.pm25Value)
        AIR_LEVEL.get(AirQualityData.getPM25Level(value))?.let {
            pm_25Item.setData(it, airQualityData.pm25Value)
        }
    }


    private fun airValueToInt(value: String): Int {
        if (value.isNotEmpty() && !value.contains(Regex("\\D"))) {
            return value.toInt()
        }
        return 0
    }


//        var airlevelInfo =
//            AIR_LEVEL.get(AirQualityData.getPM10Level(airQualityData.pm10Value.toInt()))
//        airlevelInfo?.let {
//            icon.setImageResource(it.iconRes)
//            icon.setColorFilter(it.levelColor)
//            airlevel.text = it.levelString
//            airlevel.setTextColor(it.levelColor)
//        }
//    }

//    override fun onStart() {
//        super.onStart()

//        if (!checkPermissions()) {
//            requestPermissions()
//        } else {
//            getLastLocation()
//        }
//    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    latitudeText.text = resources
                        .getString(R.string.latitude_label, task.result.latitude)
                    longitudeText.text = resources
                        .getString(R.string.longitude_label, task.result.longitude)


//                    NetworkCall1(resultTextView).execute(
//                        MeasurementStationInfoService.create().getNearByStationByTmXY(
//                            task.result.longitude.toString(),
//                            task.result.latitude.toString()
//                        )
//                    )ø

                } else {
                }
            }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            // Request permission
            startLocationPermissionRequest()
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
            // If user interaction was interrupted, the permission request is cancelled and you
            // receive empty arrays.
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")

            // Permission granted.
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> getLastLocation()

            // Permission denied.

            // Notify the user via a SnackBar that they have rejected a core permission for the
            // app, which makes the Activity useless. In a real app, core permissions would
            // typically be best requested during a welcome-screen flow.

            // Additionally, it is important to remember that a permission might have been
            // rejected without asking the user for permission (device policy or "Never ask
            // again" prompts). Therefore, a user interface affordance is typically implemented
            // when permissions are denied. Otherwise, your app could appear unresponsive to
            // touches or interactions which have required permissions.
                else -> {
                    // Build intent that displays the App settings screen.
                    val intent = Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                }
            }
        }
    }


    class NetworkCall1(val resultTextView: TextView) :
        AsyncTask<Call<MeasurmentSatationResponse>, Void, String>() {
        override fun doInBackground(vararg params: Call<MeasurmentSatationResponse>): String {
            return params.get(0).execute().body().toString()
        }

        override fun onPostExecute(result: String?) {
            resultTextView.text = result
        }
    }


}
