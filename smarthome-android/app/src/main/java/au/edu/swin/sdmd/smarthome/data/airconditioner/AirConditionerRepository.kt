package au.edu.swin.sdmd.smarthome.data.airconditioner

import kotlinx.coroutines.flow.Flow

// Interface for repositories that have to interact with the Air Conditioner table. There can be multiple repositories of this type but this project
// has one only.
interface AirConditionerRepository {
    suspend fun insert(airConditioner: AirConditioner)

    suspend fun update(airConditioner: AirConditioner)

    suspend fun delete(airConditioner: AirConditioner)

    fun getAll(): Flow<List<AirConditioner>>

    fun getById(id: Int): Flow<AirConditioner>

    fun getByRoom(room: String): Flow<List<AirConditioner>>

    fun getFavorites(): Flow<List<AirConditioner>>

    fun getCount(): Flow<Int>

    fun getActiveCount(): Flow<Int>

    fun getCountByRoom(room: String): Flow<Int>

    fun getActiveCountByRoom(room: String): Flow<Int>
}