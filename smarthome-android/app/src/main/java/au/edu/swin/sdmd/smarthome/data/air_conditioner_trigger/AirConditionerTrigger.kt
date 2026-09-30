package au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import kotlinx.parcelize.Parcelize
import java.time.LocalTime

@Parcelize
@Entity(
    tableName = "air_conditioner_trigger",
    foreignKeys = [
        ForeignKey(
            entity = AirConditioner::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("airConditionerId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class AirConditionerTrigger(
    @PrimaryKey(autoGenerate = true)
    val triggerId: Long = 0,
    val airConditionerId: Int,
    val action: AirConditionerAction,
    val time: LocalTime
) : Parcelable

enum class AirConditionerAction(val value: String) {
    TURN_ON(value = "turn on"),
    TURN_OFF(value = "turn off")
}