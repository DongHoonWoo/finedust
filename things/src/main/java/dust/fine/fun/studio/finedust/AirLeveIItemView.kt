package dust.fine.`fun`.studio.finedust

import android.view.View
import android.widget.ImageView
import android.widget.TextView

class AirLeveIItemView(val rootView: View) {
    val iconView: ImageView
    val airLevelView: TextView
    val airValueView: TextView

    init {
        iconView = rootView.findViewById(R.id.icon)
        airLevelView = rootView.findViewById(R.id.air_level)
        airValueView = rootView.findViewById(R.id.air_value)
    }

    fun setData(iconRes: Int, airValue: String, airLevel: String, colorLevel: Int) {
        iconView.setImageResource(iconRes)
        iconView.setColorFilter(colorLevel)
        airLevelView.text = airLevel
        airLevelView.setTextColor(colorLevel)
        airValueView.text = airValue
        airValueView.setTextColor(colorLevel)

    }
}