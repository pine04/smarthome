package au.edu.swin.sdmd.smarthome.data.airconditioner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Contains relevant queries for the Air Conditioner table.
@Dao
interface AirConditionerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(airConditioner: AirConditioner)

    @Update
    suspend fun update(airConditioner: AirConditioner)

    @Delete
    suspend fun delete(airConditioner: AirConditioner)

    @Query("SELECT * FROM air_conditioner")
    fun getAll(): Flow<List<AirConditioner>>

    @Query("SELECT * FROM air_conditioner WHERE id = :id")
    fun getById(id: Int): Flow<AirConditioner>

    @Query("SELECT * FROM air_conditioner WHERE location == :room")
    fun getByRoom(room: String): Flow<List<AirConditioner>>

    @Query("SELECT * FROM air_conditioner WHERE isFavorite == 1")
    fun getFavorites(): Flow<List<AirConditioner>>

    @Query("SELECT COUNT(*) FROM air_conditioner")
    fun getCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM air_conditioner WHERE isOn == 1")
    fun getActiveCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM air_conditioner WHERE location == :room")
    fun getCountByRoom(room: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM air_conditioner WHERE isOn == 1 AND location == :room")
    fun getActiveCountByRoom(room: String): Flow<Int>
}