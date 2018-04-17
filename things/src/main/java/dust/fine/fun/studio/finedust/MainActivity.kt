package dust.fine.`fun`.studio.finedust

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Config.LOGD
import android.util.Log
import android.view.View
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
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
    private val handler = Handler()
    private lateinit var button: View

    private lateinit var sensorGpio: Gpio
    private lateinit var motorGpio: Gpio
    private lateinit var pwm: Pwm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSensor()
        initMotor()

//        button = findViewById(R.id.button)
//        button.setOnClickListener { _ ->
//            getAirPollution()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeGpio()
    }

    private fun initSensor() {
        sensorGpio = PeripheralManager.getInstance().openGpio("BCM5")
        sensorGpio.setDirection(Gpio.DIRECTION_IN)
        sensorGpio.setActiveType(Gpio.ACTIVE_HIGH)
        sensorGpio.setEdgeTriggerType(Gpio.EDGE_RISING)
        sensorGpio.registerGpioCallback { gpio ->
            Log.d("TEST", "BCM5 : " + gpio.value)
            getAirPollution()
            true
        }
    }

    private fun initMotor() {
        motorGpio = PeripheralManager.getInstance().openGpio("BCM6")
        motorGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        pwm = PeripheralManager.getInstance().openPwm("PWM1")
        pwm.setPwmFrequencyHz(50.0) // 50Hz
        pwm.setPwmDutyCycle(25.0) // 25%
        pwm.setEnabled(false)
    }


    private fun turnOnMotor(sec: Int) {
        motorGpio.value = true
        pwm.setEnabled(true)
        handler.postDelayed(
            {
                motorGpio.value = false
                pwm.setEnabled(false)
            },
            (sec * 1000).toLong()
        )
    }

    private fun closeGpio() {
        sensorGpio.close()
        motorGpio.close()
        pwm.close()
    }

    private fun getAirPollution() {
        AirPollutionInfoService.create().getAirPollutionByMsrStationRx("수지")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                result.list.let {
                    setUI(it.get(0))
                }
            },
                { ex ->
                    Log.d("TEST", "error -> " + ex.toString())
                }
            )
    }

    private fun checkAirValue(pm10Level: Int, pm25Level: Int) {
        if (pm10Level > 2 || pm25Level > 2) {
            turnOnMotor(5)
        }
    }

    private fun setUI(airQualityData: AirQualityData) {
        val pm_10Item = AirLeveIItemView(findViewById(R.id.pm_10))
        val pm_25Item = AirLeveIItemView(findViewById(R.id.pm_25))


        val pm10Level = airValueToInt(airQualityData.pm10Value)
        AIR_LEVEL.get(AirQualityData.getPM10Level(pm10Level))?.let {
            val airValue = "PM10 : " + airQualityData.pm10Value + getString(R.string.air_level_unit)
            val airLevel = it.levelString
            pm_10Item.setData(it.iconRes, airValue, airLevel, it.levelColor)
        }

        val pm25Level = airValueToInt(airQualityData.pm25Value)
        AIR_LEVEL.get(AirQualityData.getPM25Level(pm25Level))?.let {
            val airValue = "PM25 : " + airQualityData.pm25Value + getString(R.string.air_level_unit)
            val airLevel = it.levelString
            pm_25Item.setData(it.iconRes, airValue, airLevel, it.levelColor)
        }

        checkAirValue(pm10Level, pm25Level)
    }

    private fun airValueToInt(value: String): Int {
        if (value.isNotEmpty() && !value.contains(Regex("\\D"))) {
            return value.toInt()
        }
        return 0
    }
}