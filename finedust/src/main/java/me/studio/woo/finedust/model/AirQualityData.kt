package me.studio.woo.finedust.model


data class AirPolutionInfoResponse(
    val list: List<AirQualityData>
)

data class AirQualityData(
    val coGrade: String,
    val coValue: String,
    val dataTime: String,
    val khaiGrade: String,
    val khaiValue: String,
    val mangName: String,
    val no2Grade: String,
    val no2Value: String,
    val o3Grade: String,
    val o3Value: String,
    val pm10Grade: String,
    val pm10Grade1h: String,
    val pm10Value: String,
    val pm10Value24: String,
    val pm25Grade: String,
    val pm25Grade1h: String,
    val pm25Value: String,
    val pm25Value24: String,
    val so2Grade: String,
    val so2Value: String
) {
    companion object {
        fun getPM10Level(value: Int) = when (value) {
            in 0..30 -> 1
            in 31..80 -> 2
            in 81..150 -> 3
            else -> 4
        }

        fun getPM25Level(value: Int) = when (value) {
            in 0..15 -> 1
            in 16..35 -> 2
            in 36..75 -> 3
            else -> 4
        }
    }
}