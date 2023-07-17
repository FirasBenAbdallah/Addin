package addinn.dev.team

import addinn.dev.domain.entity.response.Response
import addinn.dev.team.presentation.destinations.HomeViewDestination
import addinn.dev.team.presentation.destinations.LoginViewDestination
import addinn.dev.team.presentation.main.MainActivity
import addinn.dev.team.utils.connectivity.ConnectivityObserver
import addinn.dev.team.viewModel.ConnectivityViewModel
import addinn.dev.team.viewModel.MainViewModel
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val connectivityViewModel: ConnectivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            if(connectivityViewModel.status == ConnectivityObserver.Status.Available){
                val startRoute = viewModel.startRoute.collectAsState()

                val options = ActivityOptions.makeCustomAnimation(
                    this@SplashScreen,
                    R.anim.fade_in,
                    R.anim.fade_out
                )

                LaunchedEffect(startRoute.value) {
                    delay(3000)
                    when (startRoute.value) {
                        is Response.Error -> {
                            Toast.makeText(this@SplashScreen, "ERROR", Toast.LENGTH_SHORT).show()
                        }

                        Response.Loading -> {
                        }

                        is Response.Success -> {
                            val route = (startRoute.value as Response.Success<Route>).data
                            if (route == HomeViewDestination) {
                                val intent = Intent(this@SplashScreen, MainActivity::class.java)

                                intent.putExtra("route", true)
                                startActivity(intent, options.toBundle())

                                finish()
                            }

                            if (route == LoginViewDestination) {
                                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                                intent.putExtra("route", false)
                                startActivity(intent, options.toBundle())

                                finish()
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.team_lottie
                        )
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = 1
                    )
                }
            }
            else{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.lottie_internet
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieAnimation(
                            composition = composition,
                            iterations = Int.MAX_VALUE,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        Text(
                            text = "Oops! Seems like you're offline",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Please check your internet connection",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}