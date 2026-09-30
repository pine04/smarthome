package au.edu.swin.sdmd.smarthome.data.airconditioner

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents the Air Conditioner table in the database.
@Entity(tableName = "air_conditioner")
data class AirConditioner(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val location: String,
    val isOn: Boolean = false,
    val temperature: Float = 18.0f,
    val fanSpeed: FanSpeed = FanSpeed.AUTO,
    val isFavorite: Boolean = false
)

enum class FanSpeed(val value: String) {
    LOW(value = "Low"),
    MEDIUM(value = "Medium"),
    HIGH(value = "High"),
    AUTO(value = "Auto")
}