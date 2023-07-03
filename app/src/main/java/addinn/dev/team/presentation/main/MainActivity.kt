package addinn.dev.team.presentation.main

import addinn.dev.team.viewModel.MainViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var backPressed = 0L

    private val finish: () -> Unit = {
        if (backPressed + 3000 > System.currentTimeMillis()) {
            finishAndRemoveTask()
        } else {
            Toast.makeText(this, "Press Back again to EXIT", Toast.LENGTH_SHORT).show()
        }
        backPressed = System.currentTimeMillis()
    }

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        setContent {
            MainRoot(finish = finish, startRoute = viewModel.startRoute.value)
        }
    }
}




