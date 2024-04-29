package au.edu.swin.sdmd.smarthome.data.light_trigger

import kotlinx.coroutines.flow.Flow

interface LightTriggerRepository {
    suspend fun insert(lightTrigger: LightTrigger)

    suspend fun update(lightTrigger: LightTrigger)

    suspend fun delete(lightTrigger: LightTrigger)

    fun getAllForLight(lightId: Int) : Flow<List<LightTrigger>>

    fun getById(triggerId: Long) : Flow<LightTrigger>
}