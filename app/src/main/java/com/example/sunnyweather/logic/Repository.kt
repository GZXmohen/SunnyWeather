package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable

object Repository {
    // 1. liveData 构建器会启动一个协程
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        // 2. 将 try-catch 的结果直接赋值给 result
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places) // 成功分支
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}")) // 业务错误分支
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e) // 网络或解析异常分支
        }

        // 3. 将最终得到的 Result 对象发射出去
        emit(result)
    }
}