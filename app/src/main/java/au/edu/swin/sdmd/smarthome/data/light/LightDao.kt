package au.edu.swin.sdmd.smarthome.data.light

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import kotlinx.coroutines.flow.Flow

@Dao
interface LightDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(light: Light)

    @Update
    suspend fun updateLight(light: Light)

    @Delete
    suspend fun deleteLight(light: Light)

    @Query("SELECT * FROM light")
    fun getAllLights(): Flow<List<Light>>

    @Query("SELECT * FROM light WHERE isFavorite == 1")
    fun getAllFavoriteLights(): Flow<List<Light>>

    @Query("SELECT COUNT(*) FROM light WHERE isOn == 1")
    fun getActiveLightCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM light")
    fun getLightCount(): Flow<Int>

    @Query("SELECT * FROM light WHERE id = :id")
    fun getLightById(id: Int): Flow<Light>

    @Query("SELECT COUNT(*) FROM light WHERE isOn == 1 AND location == :room")
    fun getActiveCountForRoom(room: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM light WHERE location == :room")
    fun getCountForRoom(room: String): Flow<Int>

    @Query("SELECT * FROM light WHERE location == :room")
    fun getAllForRoom(room: String): Flow<List<Light>>
}