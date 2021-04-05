package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.keerat.fummy.R


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // This is the loading time of the splash screen

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            val startAct =Intent(this@SplashScreen,
                MainActivity::class.java)
            startActivity(startAct)

            // close this activity
            finish()
        },1000)


    }

}
