package au.edu.swin.sdmd.smarthome.data

// A list of supported rooms in the application.
enum class Room(val value: String) {
    LIVING_ROOM(value = "Living Room"),
    BEDROOM(value = "Bedroom"),
    BATHROOM(value = "Bathroom"),
    KITCHEN(value = "Kitchen"),
    HALLWAY(value = "Hallway"),
    GARAGE(value = "Garage"),
    ATTIC(value = "Attic")
}