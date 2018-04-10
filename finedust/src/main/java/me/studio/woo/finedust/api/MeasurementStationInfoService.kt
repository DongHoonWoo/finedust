package me.studio.woo.finedust.api

import io.reactivex.Observable
import me.studio.woo.finedust.model.MeasurmentSatationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLDecoder

interface MeasurementStationInfoService {

    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList")
    fun getNearByStationByTmXY(
        @Query("tmX") tmX: String,
        @Query("tmY") tmY: String,
        @Query("serviceKey") serviceKey: String = URLDecoder.decode(
            "9qGNRtBZob%2BaCmf%2BFVTpiuHODyXY0SRzHnVSib8PpjVMSDXb%2F4G%2FoEPXPHGjKvxaSRDbctN5CEuzUBr5h8Acaw%3D%3D",
            "utf-8"
        ),
        @Query("_returnType") returnType: String = "json"
    ): Call<MeasurmentSatationResponse>

    @GET("MsrstnInfoInqireSvc/getTMStdrCrdnt")
    fun getNearByStationByAddr(
        @Query("umdName") umdName: String,
        @Query("serviceKey") serviceKey: String = URLDecoder.decode(
            "9qGNRtBZob%2BaCmf%2BFVTpiuHODyXY0SRzHnVSib8PpjVMSDXb%2F4G%2FoEPXPHGjKvxaSRDbctN5CEuzUBr5h8Acaw%3D%3D",
            "utf-8"
        ),
        @Query("_returnType") returnType: String = "json"
    ): Call<MeasurmentSatationResponse>

    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList")
    fun getNearByStationByTmXYRx(
        @Query("tmX") tmX: String,
        @Query("tmY") tmY: String,
        @Query("serviceKey") serviceKey: String = URLDecoder.decode(
            "9qGNRtBZob%2BaCmf%2BFVTpiuHODyXY0SRzHnVSib8PpjVMSDXb%2F4G%2FoEPXPHGjKvxaSRDbctN5CEuzUBr5h8Acaw%3D%3D",
            "utf-8"
        ),
        @Query("_returnType") returnType: String = "json"
    ): Observable<MeasurmentSatationResponse>

    @GET("MsrstnInfoInqireSvc/getTMStdrCrdnt")
    fun getNearByStationByAddrRx(
        @Query("umdName") umdName: String,
        @Query("serviceKey") serviceKey: String = URLDecoder.decode(
            "9qGNRtBZob%2BaCmf%2BFVTpiuHODyXY0SRzHnVSib8PpjVMSDXb%2F4G%2FoEPXPHGjKvxaSRDbctN5CEuzUBr5h8Acaw%3D%3D",
            "utf-8"
        ),
        @Query("_returnType") returnType: String = "json"
    ): Observable<MeasurmentSatationResponse>

    companion object {
        val baseUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/"

        fun create(): MeasurementStationInfoService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()
            return retrofit.create(MeasurementStationInfoService::class.java)
        }

        fun createOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            return builder.build()
        }
    }
}