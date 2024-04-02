package au.edu.swin.sdmd.smarthome.data.light

import kotlinx.coroutines.flow.Flow

interface LightRepository {
    suspend fun insert(light: Light)

    suspend fun update(light: Light)

    suspend fun delete(light: Light)

    fun getAll() : Flow<List<Light>>

    fun getById(id: Int): Flow<Light>

    fun getByRoom(room: String): Flow<List<Light>>

    fun getFavorites(): Flow<List<Light>>

    fun getCount(): Flow<Int>

    fun getActiveCount(): Flow<Int>

    fun getCountByRoom(room: String): Flow<Int>

    fun getActiveCountByRoom(room: String): Flow<Int>
}