package au.edu.swin.sdmd.smarthome.data.airconditioner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import au.edu.swin.sdmd.smarthome.data.light.Light
import kotlinx.coroutines.flow.Flow

@Dao
interface AirConditionerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(airConditioner: AirConditioner)

    @Update
    suspend fun update(airConditioner: AirConditioner)

    @Delete
    suspend fun delete(airConditioner: AirConditioner)

    @Query("SELECT * FROM air_conditioner")
    fun getAllAirConditioners(): Flow<List<AirConditioner>>

    @Query("SELECT * FROM air_conditioner WHERE isFavorite == 1")
    fun getAllFavoriteAirConditioners(): Flow<List<AirConditioner>>

    @Query("SELECT COUNT(*) FROM air_conditioner WHERE isOn == 1")
    fun getActiveAirConditionerCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM air_conditioner")
    fun getAirConditionerCount(): Flow<Int>

    @Query("SELECT * FROM air_conditioner WHERE id = :id")
    fun getAirConditionerById(id: Int): Flow<AirConditioner>

    @Query("SELECT COUNT(*) FROM air_conditioner WHERE isOn == 1 AND location == :room")
    fun getActiveCountForRoom(room: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM air_conditioner WHERE location == :room")
    fun getCountForRoom(room: String): Flow<Int>

    @Query("SELECT * FROM air_conditioner WHERE location == :room")
    fun getAllForRoom(room: String): Flow<List<AirConditioner>>
}