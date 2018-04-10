package me.studio.woo.finedust.model

data class MeasurmentSatationResponse(
    val list: List<MeasurmentStationInfo>
)

data class MeasurmentStationInfo(
    val addr: String,
    val stationName: String,
    val tmX: String,
    val tmY: String
)