package au.edu.swin.sdmd.smarthome.data.light

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Contains relevant queries for the Light table.
@Dao
interface LightDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(light: Light)

    @Update
    suspend fun update(light: Light)

    @Delete
    suspend fun delete(light: Light)

    @Query("SELECT * FROM light")
    fun getAll(): Flow<List<Light>>

    @Query("SELECT * FROM light WHERE id = :id")
    fun getById(id: Int): Flow<Light>

    @Query("SELECT * FROM light WHERE location == :room")
    fun getByRoom(room: String): Flow<List<Light>>

    @Query("SELECT * FROM light WHERE isFavorite == 1")
    fun getFavorites(): Flow<List<Light>>

    @Query("SELECT COUNT(*) FROM light")
    fun getCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM light WHERE isOn == 1")
    fun getActiveCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM light WHERE location == :room")
    fun getCountByRoom(room: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM light WHERE isOn == 1 AND location == :room")
    fun getActiveCountByRoom(room: String): Flow<Int>
}