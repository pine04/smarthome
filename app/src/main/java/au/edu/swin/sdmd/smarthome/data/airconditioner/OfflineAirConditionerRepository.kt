package au.edu.swin.sdmd.smarthome.data.airconditioner

import kotlinx.coroutines.flow.Flow

class OfflineAirConditionerRepository(private val airConditionerDao: AirConditionerDao) :
    AirConditionerRepository {
    override fun getAllAirConditioners(): Flow<List<AirConditioner>> {
        return airConditionerDao.getAllAirConditioners()
    }

    override suspend fun insertAirConditioner(airConditioner: AirConditioner) {
        airConditionerDao.insert(airConditioner)
    }

    override fun getAllFavoriteAirConditioners(): Flow<List<AirConditioner>> {
        return airConditionerDao.getAllFavoriteAirConditioners()
    }

    override fun getActiveAirConditionerCount(): Flow<Int> {
        return airConditionerDao.getActiveAirConditionerCount()
    }

    override fun getAirConditionerCount(): Flow<Int> {
        return airConditionerDao.getAirConditionerCount()
    }

    override suspend fun update(airConditioner: AirConditioner) {
        airConditionerDao.update(airConditioner)
    }

    override suspend fun delete(airConditioner: AirConditioner) {
        airConditionerDao.delete(airConditioner)
    }

    override fun getAirConditionerById(id: Int): Flow<AirConditioner> {
        return airConditionerDao.getAirConditionerById(id)
    }

    override fun getCountForRoom(room: String): Flow<Int> {
        return airConditionerDao.getCountForRoom(room)
    }

    override fun getActiveCountForRoom(room: String): Flow<Int> {
        return airConditionerDao.getActiveCountForRoom(room)
    }

    override fun getAllForRoom(room: String): Flow<List<AirConditioner>> {
        return airConditionerDao.getAllForRoom(room)
    }
}