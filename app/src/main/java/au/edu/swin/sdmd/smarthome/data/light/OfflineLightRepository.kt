package au.edu.swin.sdmd.smarthome.data.light

import kotlinx.coroutines.flow.Flow

class OfflineLightRepository(private val lightDao: LightDao) : LightRepository {
    override fun getAllLights(): Flow<List<Light>> {
        return lightDao.getAllLights()
    }

    override suspend fun insertLight(light: Light) {
        lightDao.insert(light)
    }

    override fun getLightCount(): Flow<Int> {
        return lightDao.getLightCount()
    }

    override fun getActiveLightCount(): Flow<Int> {
        return lightDao.getActiveLightCount()
    }

    override fun getAllFavoriteLights(): Flow<List<Light>> {
        return lightDao.getAllFavoriteLights()
    }

    override fun getLightById(id: Int): Flow<Light> {
        return lightDao.getLightById(id)
    }

    override suspend fun updateLight(light: Light) {
        lightDao.updateLight(light)
    }

    override suspend fun deleteLight(light: Light) {
        lightDao.deleteLight(light)
    }

    override fun getCountForRoom(room: String): Flow<Int> {
        return lightDao.getCountForRoom(room)
    }

    override fun getActiveCountForRoom(room: String): Flow<Int> {
        return lightDao.getActiveCountForRoom(room)
    }

    override fun getAllForRoom(room: String): Flow<List<Light>> {
        return lightDao.getAllForRoom(room)
    }
}