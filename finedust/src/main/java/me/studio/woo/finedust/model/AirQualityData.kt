package me.studio.woo.finedust.model


data class AirPolutionInfoResponse(
    val list: List<AirQualityData>
)

data class AirQualityData(
    val mangName: String,
    val dateTime: String,
    val so2Value: String,
    val coValue: String,
    val o3Value: String,
    val no2Value: String,
    val pm10Value: String,
    val pm10Value24: String,
    val pm25Value: String,
    val pm25Value24: String,
    val khaiValue: String,
    val khaiGrade: String,
    val so2Grade: String,
    val coGrade: String,
    val o3Grade: String,
    val no2Grade: String,
    val pm10Grade: String,
    val pm25Grade: String,
    val pm10Grade1h: String,
    val pm25Grade1h: String
)