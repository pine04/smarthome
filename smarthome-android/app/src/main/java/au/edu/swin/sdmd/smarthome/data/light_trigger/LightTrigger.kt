package au.edu.swin.sdmd.smarthome.data.light_trigger

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import au.edu.swin.sdmd.smarthome.data.light.Light
import kotlinx.parcelize.Parcelize
import java.time.LocalTime

@Parcelize
@Entity(
    tableName = "light_trigger",
    foreignKeys = [
        ForeignKey(
            entity = Light::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("lightId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class LightTrigger(
    @PrimaryKey(autoGenerate = true)
    val triggerId: Long = 0,
    val lightId: Int,
    val action: LightAction,
    val time: LocalTime
) : Parcelable

enum class LightAction(val value: String) {
    TURN_ON(value = "turn on"),
    TURN_OFF(value = "turn off")
}