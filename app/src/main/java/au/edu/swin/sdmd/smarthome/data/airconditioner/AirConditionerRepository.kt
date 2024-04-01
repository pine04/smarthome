package au.edu.swin.sdmd.smarthome.data.airconditioner

import au.edu.swin.sdmd.smarthome.data.light.Light
import kotlinx.coroutines.flow.Flow

interface AirConditionerRepository {
    suspend fun insertAirConditioner(airConditioner: AirConditioner)

    fun getAllAirConditioners(): Flow<List<AirConditioner>>

    fun getAllFavoriteAirConditioners(): Flow<List<AirConditioner>>

    fun getActiveAirConditionerCount(): Flow<Int>

    fun getAirConditionerCount(): Flow<Int>

    fun getAirConditionerById(id: Int): Flow<AirConditioner>

    suspend fun update(airConditioner: AirConditioner)

    suspend fun delete(airConditioner: AirConditioner)

    fun getCountForRoom(room: String): Flow<Int>
    fun getActiveCountForRoom(room: String): Flow<Int>

    fun getAllForRoom(room: String): Flow<List<AirConditioner>>
}