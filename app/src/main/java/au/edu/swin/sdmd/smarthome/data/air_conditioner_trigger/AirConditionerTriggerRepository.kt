package au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger

import kotlinx.coroutines.flow.Flow

interface AirConditionerTriggerRepository {
    suspend fun insert(airConditionerTrigger: AirConditionerTrigger)

    suspend fun update(airConditionerTrigger: AirConditionerTrigger)

    suspend fun delete(airConditionerTrigger: AirConditionerTrigger)

    fun getAllForAirConditioner(airConditionerId: Int) : Flow<List<AirConditionerTrigger>>

    fun getById(triggerId: Long) : Flow<AirConditionerTrigger>
}