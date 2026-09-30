package au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineAirConditionerTriggerRepository(
    private val airConditionerTriggerDao: AirConditionerTriggerDao
) : AirConditionerTriggerRepository {
    override suspend fun delete(airConditionerTrigger: AirConditionerTrigger) {
        airConditionerTriggerDao.delete(airConditionerTrigger)
    }

    override suspend fun insert(airConditionerTrigger: AirConditionerTrigger) {
        airConditionerTriggerDao.insert(airConditionerTrigger)
    }

    override suspend fun update(airConditionerTrigger: AirConditionerTrigger) {
        Log.d("DAO", airConditionerTrigger.toString())
        airConditionerTriggerDao.update(airConditionerTrigger)
    }

    override fun getAllForAirConditioner(airConditionerId: Int): Flow<List<AirConditionerTrigger>> {
        return airConditionerTriggerDao.getAllForAirConditioner(airConditionerId)
    }

    override fun getById(triggerId: Long): Flow<AirConditionerTrigger> {
        return airConditionerTriggerDao.getById(triggerId)
    }
}