package au.edu.swin.sdmd.smarthome.data.light

import kotlinx.coroutines.flow.Flow

interface LightRepository {
    suspend fun insertLight(light: Light)

    fun getAllLights() : Flow<List<Light>>

    fun getAllFavoriteLights(): Flow<List<Light>>

    fun getActiveLightCount(): Flow<Int>

    fun getLightCount(): Flow<Int>

    fun getLightById(id: Int): Flow<Light>

    suspend fun updateLight(light: Light)

    suspend fun deleteLight(light: Light)

    fun getActiveCountForRoom(room: String): Flow<Int>
    fun getCountForRoom(room: String): Flow<Int>

    fun getAllForRoom(room: String): Flow<List<Light>>
}