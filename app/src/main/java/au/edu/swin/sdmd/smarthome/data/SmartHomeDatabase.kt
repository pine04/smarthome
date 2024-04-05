package au.edu.swin.sdmd.smarthome.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerDao
import au.edu.swin.sdmd.smarthome.data.airconditioner.FanSpeed
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightDao

// Responsible for creating and storing an instance of the database.
@Database(entities = [Light::class, AirConditioner::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SmartHomeDatabase : RoomDatabase() {
    abstract fun lightDao(): LightDao
    abstract fun airConditionerDao() : AirConditionerDao

    companion object {
        @Volatile
        private var Instance: SmartHomeDatabase? = null

        fun getDatabase(context: Context): SmartHomeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SmartHomeDatabase::class.java, "smart_home_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

// Specifies converters to convert between FanSpeed and String.
class Converters {
    @TypeConverter
    fun FanSpeedToString(fanSpeed: FanSpeed?): String? {
        return fanSpeed?.value
    }

    @TypeConverter
    fun StringToFanSpeed(string: String?): FanSpeed? {
        return string?.let { FanSpeed.entries.find { it.value == string } }
    }
}