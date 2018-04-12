package dust.fine.`fun`.studio.finedust

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.studio.woo.finedust.api.AirPollutionInfoService
import me.studio.woo.finedust.model.AirQualityData

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {
    private lateinit var button: View
    private lateinit var sensorGpio: Gpio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pioService = PeripheralManager.getInstance()
        sensorGpio = pioService.openGpio("BCM5")
        sensorGpio.setDirection(Gpio.DIRECTION_IN)
        sensorGpio.setActiveType(Gpio.ACTIVE_HIGH)
        sensorGpio.setEdgeTriggerType(Gpio.EDGE_RISING)
        sensorGpio.registerGpioCallback { gpio ->
            Log.d("TEST", "BCM5 : " + gpio.value)
            getAirPollution()
            true
        }

        button = findViewById(R.id.button)
        button.setOnClickListener { _ ->
            getAirPollution()
        }
    }

    private fun getAirPollution() {
        AirPollutionInfoService.create().getAirPollutionByMsrStationRx("수지")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                result.list.let {
                    setUI(it.get(0))
                }
            }
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
}