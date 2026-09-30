package au.edu.swin.sdmd.smarthome.data.light

import kotlinx.coroutines.flow.Flow

// Light repository class used by ViewModels. Contains methods that interact with the Light table.
class OfflineLightRepository(private val lightDao: LightDao) : LightRepository {
    override suspend fun insert(light: Light) {
        lightDao.insert(light)
    }

    override suspend fun update(light: Light) {
        lightDao.update(light)
    }

    override suspend fun delete(light: Light) {
        lightDao.delete(light)
    }

    override fun getAll(): Flow<List<Light>> {
        return lightDao.getAll()
    }

    override fun getById(id: Int): Flow<Light> {
        return lightDao.getById(id)
    }

    override fun getByRoom(room: String): Flow<List<Light>> {
        return lightDao.getByRoom(room)
    }

    override fun getFavorites(): Flow<List<Light>> {
        return lightDao.getFavorites()
    }

    override fun getCount(): Flow<Int> {
        return lightDao.getCount()
    }

    override fun getActiveCount(): Flow<Int> {
        return lightDao.getActiveCount()
    }

    override fun getCountByRoom(room: String): Flow<Int> {
        return lightDao.getCountByRoom(room)
    }

    override fun getActiveCountByRoom(room: String): Flow<Int> {
        return lightDao.getActiveCountByRoom(room)
    }
}