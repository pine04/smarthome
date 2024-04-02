package au.edu.swin.sdmd.smarthome

import android.app.Application
import au.edu.swin.sdmd.smarthome.data.AppContainer

class MainApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}