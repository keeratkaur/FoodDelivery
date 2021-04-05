package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.keerat.fummy.R

class MainActivity : AppCompatActivity() {
    lateinit var btnSignIn:Button
    lateinit var btnSignUp:Button
    lateinit var imgLogo2:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignIn=findViewById(R.id.btnSignIn)
        btnSignUp=findViewById(R.id.btnSignUp)
        imgLogo2=findViewById(R.id.imgLogo2)

        btnSignUp.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignUp::class.java))


        }
        btnSignIn.setOnClickListener {
            startActivity(Intent(this@MainActivity, Sign_In::class.java))
        }
    }
}