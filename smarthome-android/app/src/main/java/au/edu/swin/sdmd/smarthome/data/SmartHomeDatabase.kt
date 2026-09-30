package au.edu.swin.sdmd.smarthome.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerAction
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTriggerDao
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerDao
import au.edu.swin.sdmd.smarthome.data.airconditioner.FanSpeed
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightDao
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightAction
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTriggerDao
import java.time.LocalTime

// Responsible for creating and storing an instance of the database.
@Database(
    entities = [Light::class, AirConditioner::class, LightTrigger::class, AirConditionerTrigger::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SmartHomeDatabase : RoomDatabase() {
    abstract fun lightDao(): LightDao
    abstract fun airConditionerDao(): AirConditionerDao
    abstract fun lightTriggerDao(): LightTriggerDao
    abstract fun airConditionerTriggerDao() : AirConditionerTriggerDao

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
    fun fanSpeedToString(fanSpeed: FanSpeed?): String? {
        return fanSpeed?.value
    }

    @TypeConverter
    fun stringToFanSpeed(string: String?): FanSpeed? {
        return string?.let { FanSpeed.entries.find { it.value == string } }
    }

    @TypeConverter
    fun lightActionToString(lightAction: LightAction?): String? {
        return lightAction?.value
    }

    @TypeConverter
    fun stringToLightAction(string: String?): LightAction? {
        return string?.let { LightAction.entries.find { it.value == string } }
    }

    @TypeConverter
    fun airConditionerActionToString(airConditionerAction: AirConditionerAction?): String? {
        return airConditionerAction?.value
    }

    @TypeConverter
    fun stringToAirConditionerAction(string: String?): AirConditionerAction? {
        return string?.let { AirConditionerAction.entries.find { it.value == string } }
    }

    @TypeConverter
    fun localTimeToString(localTime: LocalTime?) :String? {
        return localTime?.toString()
    }

    @TypeConverter
    fun stringToLocalTime(string: String?) : LocalTime? {
        return string?.let { LocalTime.parse(it) }
    }
}