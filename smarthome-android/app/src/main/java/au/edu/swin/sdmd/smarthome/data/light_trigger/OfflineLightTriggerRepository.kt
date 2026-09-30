package au.edu.swin.sdmd.smarthome.data.light_trigger

import kotlinx.coroutines.flow.Flow

class OfflineLightTriggerRepository(
    private val lightTriggerDao: LightTriggerDao
) : LightTriggerRepository {
    override suspend fun delete(lightTrigger: LightTrigger) {
        lightTriggerDao.delete(lightTrigger)
    }

    override suspend fun insert(lightTrigger: LightTrigger) {
        lightTriggerDao.insert(lightTrigger)
    }

    override suspend fun update(lightTrigger: LightTrigger) {
        lightTriggerDao.update(lightTrigger)
    }

    override fun getAllForLight(lightId: Int): Flow<List<LightTrigger>> {
        return lightTriggerDao.getAllForLight(lightId)
    }

    override fun getById(triggerId: Long): Flow<LightTrigger> {
        return lightTriggerDao.getById(triggerId)
    }
}