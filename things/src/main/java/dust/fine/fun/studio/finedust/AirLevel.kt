package dust.fine.`fun`.studio.finedust

import android.graphics.Color

val AIR_LEVEL = hashMapOf(
    1 to AirLevelInfo(
        R.drawable.ic_very_good,
        Color.rgb(41, 140, 255),
        "GOOD"
    ),
    2 to AirLevelInfo(
        R.drawable.ic_good,
        Color.rgb(25, 192, 46),
        "NORMAL"
    ),
    3 to AirLevelInfo(
        R.drawable.ic_bad,
        Color.rgb(250, 135, 72),
        "BAD"
    ),
    4 to AirLevelInfo(
        R.drawable.ic_very_bad,
        Color.rgb(251, 64, 71),
        "VERY BAD"
    )
)

data class AirLevelInfo(
    val iconRes: Int,
    val levelColor: Int,
    val levelString: String
)