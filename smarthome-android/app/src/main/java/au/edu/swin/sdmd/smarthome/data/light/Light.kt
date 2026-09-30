package au.edu.swin.sdmd.smarthome.data.light

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents the Light table in the database.
@Entity(tableName = "light")
data class Light(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val location: String,
    val isOn: Boolean = false,
    val brightness: Float = 1.0f,
    val isFavorite: Boolean = false
)