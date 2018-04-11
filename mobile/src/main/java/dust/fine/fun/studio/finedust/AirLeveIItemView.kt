package dust.fine.`fun`.studio.finedust

import android.view.View
import android.widget.ImageView
import android.widget.TextView

class AirLeveIItemView(val rootView: View) {
    val iconView: ImageView
    val airLevel: TextView
    val airValue: TextView

    init {
        iconView = rootView.findViewById(R.id.icon)
        airLevel = rootView.findViewById(R.id.air_level)
        airValue = rootView.findViewById(R.id.air_value)
    }

    fun setData(airlevelInfo: AirLevelInfo, value: String) {
        airlevelInfo?.let {
            iconView.setImageResource(it.iconRes)
            iconView.setColorFilter(it.levelColor)
            airLevel.text = it.levelString
            airLevel.setTextColor(it.levelColor)
            airValue.text = value + rootView.context.getString(R.string.air_level_unit)
            airValue.setTextColor(it.levelColor)

        }
    }
}