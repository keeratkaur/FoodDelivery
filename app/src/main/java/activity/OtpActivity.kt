package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.keerat.fummy.R
import org.json.JSONObject
import util.ConnectionManager

class OtpActivity : AppCompatActivity() {

    lateinit var etOTP: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConPass: EditText
    lateinit var btnSubmit: Button

    lateinit var rlOTP: RelativeLayout
    var mobile:String="404"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOTP = findViewById(R.id.etOTP)
        etNewPassword.text.toString()
        etNewPassword=findViewById(R.id.etNewPassword)
        etConPass.text.toString()
        etConPass = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmitOTP)
        rlOTP = findViewById(R.id.rlOTP)

        rlOTP.visibility = View.VISIBLE

        if(intent!=null){
            mobile=intent.getStringExtra("mobileNumber") as String
        }



        try {
            btnSubmit.setOnClickListener {

                rlOTP.visibility = View.GONE


                val queue = Volley.newRequestQueue(this@OtpActivity)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"


                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",mobile)
                jsonParams.put("password", etNewPassword)
                jsonParams.put("otp", etOTP)


                if (ConnectionManager().checkConnectivity(this@OtpActivity)) {


                    val jsonRequest = object : JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonParams,
                        Response.Listener {


                            val jsonData = it.getJSONObject("data")
                            val success = jsonData.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    this@OtpActivity,
                                    "Password has successfully changed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@OtpActivity,
                                    Sign_In::class.java
                                )
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@OtpActivity,
                                    "Error-${it}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@OtpActivity,
                                "Volley Error-${it}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-Type"] = "application/json"
                            headers["token"] = "ff1b87d4d13450"


                            return headers
                        }


                    }
                    queue.add(jsonRequest)


                } else {
                    val dialog = android.app.AlertDialog.Builder(this@OtpActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection not found ")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()



                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this@OtpActivity)


                    }
                    dialog.create()
                    dialog.show()
                }



            }



        }catch (e:Exception){
            Toast.makeText(this@OtpActivity,"Error-${e}",Toast.LENGTH_SHORT).show()
        }
    }
}

