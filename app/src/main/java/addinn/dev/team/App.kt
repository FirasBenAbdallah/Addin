package addinn.dev.team

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application(){

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: App
            private set
    }
}