package au.edu.swin.sdmd.smarthome.data.airconditioner

import kotlinx.coroutines.flow.Flow

// AirConditioner repository class used by ViewModels. Contains methods that interact with the Air Conditioner table.
class OfflineAirConditionerRepository(
    private val airConditionerDao: AirConditionerDao
) : AirConditionerRepository {
    override suspend fun insert(airConditioner: AirConditioner) {
        airConditionerDao.insert(airConditioner)
    }

    override suspend fun update(airConditioner: AirConditioner) {
        airConditionerDao.update(airConditioner)
    }

    override suspend fun delete(airConditioner: AirConditioner) {
        airConditionerDao.delete(airConditioner)
    }

    override fun getAll(): Flow<List<AirConditioner>> {
        return airConditionerDao.getAll()
    }

    override fun getById(id: Int): Flow<AirConditioner> {
        return airConditionerDao.getById(id)
    }

    override fun getByRoom(room: String): Flow<List<AirConditioner>> {
        return airConditionerDao.getByRoom(room)
    }

    override fun getFavorites(): Flow<List<AirConditioner>> {
        return airConditionerDao.getFavorites()
    }

    override fun getCount(): Flow<Int> {
        return airConditionerDao.getCount()
    }

    override fun getActiveCount(): Flow<Int> {
        return airConditionerDao.getActiveCount()
    }

    override fun getCountByRoom(room: String): Flow<Int> {
        return airConditionerDao.getCountByRoom(room)
    }

    override fun getActiveCountByRoom(room: String): Flow<Int> {
        return airConditionerDao.getActiveCountByRoom(room)
    }
}