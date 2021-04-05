package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.keerat.fummy.R
import org.json.JSONObject
import util.ConnectionManager
import util.Validations

class ForgotPass : AppCompatActivity() {
    lateinit var etMobileNo: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        etMobileNo=findViewById(R.id.etMobileNo)
        etEmail=findViewById(R.id.etEmail)
        btnNext=findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            val forgotMobileNumber = etMobileNo.text.toString()
            if (Validations.validateMobile(forgotMobileNumber)) {
                etMobileNo.error = null
                if (Validations.validateEmail(etEmail.text.toString())) {
                    if (ConnectionManager().checkConnectivity(this@ForgotPass)) {

                        sendOTP(etMobileNo.text.toString(), etEmail.text.toString())
                    } else {

                        Toast.makeText(
                            this@ForgotPass,
                            "No Internet Connection! $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    etEmail.error = "Invalid Email $it"
                }
            } else {

                etMobileNo.error = "Invalid Mobile Number $it"
            }
        }
    }
    private fun sendOTP(mobileNumber: String, email: String) {
        val queue = Volley.newRequestQueue(this)
        val url =
            "http://" +"13.235.250.119" + "/v2/forgot_password/fetch_result"
        val jsonParams= JSONObject()
        jsonParams.put("mobile_number", mobileNumber)
        jsonParams.put("email", email)
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                try {val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val firstTry = data.getBoolean("first_try")
                        if (firstTry) {
                            val builder = AlertDialog.Builder(this@ForgotPass)
                            builder.setTitle("Information")
                            builder.setMessage("Please check your registered Email for the OTP.")
                            builder.setCancelable(false)
                            builder.setPositiveButton("Ok") { _, _ ->
                                val intent = Intent(
                                    this@ForgotPass,
                                    OtpActivity::class.java
                                )
                                intent.putExtra("user_mobile", mobileNumber)
                                startActivity(intent)
                            }
                            builder.create().show()
                        } else {
                            val builder = AlertDialog.Builder(this@ForgotPass)
                            builder.setTitle("Information")
                            builder.setMessage("Please refer to the previous email for the OTP.")
                            builder.setCancelable(false)
                            builder.setPositiveButton("Ok") { _, _ ->
                                val intent = Intent(
                                    this@ForgotPass,
                                    OtpActivity::class.java
                                )
                                intent.putExtra("user_mobile", mobileNumber)
                                startActivity(intent)
                            }
                            builder.create().show()
                        }
                    } else {

                        Toast.makeText(
                            this@ForgotPass,"Mobile number not registered! $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {e.printStackTrace()

                    Toast.makeText(
                        this@ForgotPass,
                        "Incorrect response error!! $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                VolleyLog.e("Error::::", "/post request fail! Error: ${it.message}")
                Toast.makeText(this@ForgotPass, it.message,
                    Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "ff1b87d4d13450"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)
    }
    override fun onBackPressed() {
        val intent = Intent(this@ForgotPass, Sign_In::class.java)
        startActivity(intent)
    }
}
