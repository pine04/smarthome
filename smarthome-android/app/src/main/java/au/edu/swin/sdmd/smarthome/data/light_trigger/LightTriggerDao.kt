package au.edu.swin.sdmd.smarthome.data.light_trigger

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LightTriggerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(lightTrigger: LightTrigger)

    @Update
    suspend fun update(lightTrigger: LightTrigger)

    @Delete
    suspend fun delete(lightTrigger: LightTrigger)

    @Query("SELECT * FROM light_trigger WHERE lightId = :lightId")
    fun getAllForLight(lightId: Int) : Flow<List<LightTrigger>>

    @Query("SELECT * FROM light_trigger WHERE triggerId = :triggerId")
    fun getById(triggerId: Long) : Flow<LightTrigger>
}