package au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AirConditionerTriggerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(airConditionerTrigger: AirConditionerTrigger)

    @Update
    suspend fun update(airConditionerTrigger: AirConditionerTrigger)

    @Delete
    suspend fun delete(airConditionerTrigger: AirConditionerTrigger)

    @Query("SELECT * FROM air_conditioner_trigger WHERE airConditionerId = :airConditionerId")
    fun getAllForAirConditioner(airConditionerId: Int) : Flow<List<AirConditionerTrigger>>

    @Query("SELECT * FROM air_conditioner_trigger WHERE triggerId = :triggerId")
    fun getById(triggerId: Long) : Flow<AirConditionerTrigger>
}