package activity

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.keerat.fummy.R
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import kotlinx.android.synthetic.main.activity_sign__in.*
import kotlinx.android.synthetic.main.activity_sign__in.etMobileNo
import kotlinx.android.synthetic.main.activity_sign_up.*
import model.SharedPrefManager
import model.User
import model.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import util.ConnectionManager
import java.util.HashMap


class SignUp : AppCompatActivity() {
    lateinit var editTexMNumber: EditText
     lateinit var editTextEmail: EditText
     lateinit var editTextPassword: EditText

    lateinit var etName:EditText
    lateinit var etAddress:EditText

    lateinit var buttonRegister:Button
    lateinit var txtViewLogIn:TextView
    lateinit var register_fragment_Progressdailog:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTexMNumber = findViewById(R.id.editTextMNumber)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)

        buttonRegister=findViewById(R.id.buttonRegister)
        etAddress=findViewById(R.id.etAddress)

        etName=findViewById(R.id.etName)
        txtViewLogIn=findViewById(R.id.textViewLogin)
        register_fragment_Progressdailog=findViewById(R.id.register_fragment_Progressdialog)


        buttonRegister.setOnClickListener {
            register()
        }
    }


    fun openHome() {
        val intent = Intent(this@SignUp, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    fun register() {

        val sharedPreferences:SharedPreferences=getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("user_logged_in", false).apply()

        if (ConnectionManager().checkConnectivity(this@SignUp)) {
            if (checkForErrors()){
                register_fragment_Progressdialog.visibility = View.VISIBLE
                try {
                    val registerUser=JSONObject()
                    registerUser.put("name",etName.text)
                    registerUser.put("mobile_number",editTexMNumber.text)
                    registerUser.put("password",editTextPassword.text)
                    registerUser.put("address",etAddress.text)
                    registerUser.put("email",editTextEmail.text)




                    val queue= Volley.newRequestQueue(this@SignUp)

                    val url="http://13.235.250.119/v2/register/fetch_result"
                    val jsonObjectRequest=object:
                        JsonObjectRequest(Request.Method.POST,url,registerUser, Response.Listener {

                        val responseJsonObjectData=it.getJSONObject("data")

                        val success=responseJsonObjectData.getBoolean("success")

                        if(success){
                            val data=responseJsonObjectData.getJSONObject("data")
                            sharedPreferences.edit().putBoolean("user_logged_in",true).apply()
                            sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name",data.getString("name")).apply()
                            sharedPreferences.edit().putString("email",data.getString("email")).apply()
                            sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()

                            Toast.makeText(this@SignUp,"Registered successful!!",Toast.LENGTH_SHORT).show()
                            openHome()

                        }else{
                            val responseMessageServer=responseJsonObjectData.getString("errorMessage")
                            Toast.makeText(this@SignUp,responseMessageServer.toString(),Toast.LENGTH_SHORT).show()
                        }
                        register_fragment_Progressdialog.visibility = View.INVISIBLE

                    },Response.ErrorListener {
                        println("error is $it")
                        register_fragment_Progressdialog.visibility = View.INVISIBLE
                        Toast.makeText(this@SignUp,"some error occurred",Toast.LENGTH_SHORT).show()

                    }
                    )
                    {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers=HashMap<String,String>()
                            headers["Content-Type"]="application/json"
                            headers["token"]="ff1b87d4d13450"

                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)
                }catch (e:JSONException){
                    Toast.makeText(this@SignUp,"some Exception Occurred",Toast.LENGTH_SHORT).show()
                }

            }


        }else{
            val alterDailog=androidx.appcompat.app.AlertDialog.Builder(this@SignUp)

            alterDailog.setMessage("Internet connection can't be established!")
            alterDailog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent=Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }
            alterDailog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this)
            }
            alterDailog.create()
            alterDailog.show()
        }
    }

    fun checkForErrors():Boolean{
        var errorPassCount=0
        if(etName.text.isBlank()){
            etName.setError("Field Missing")
        }else{
            errorPassCount++
        }
        if(editTexMNumber.text.isBlank()){
            editTexMNumber.setError("Field Missing")
        }else{
            errorPassCount++
        }
        if (etEmail.text.isBlank()){
            etEmail.setError("Field Missing")
        }else{
            errorPassCount++
        }
        if (etAddress.text.isBlank()){
            etAddress.setError("Field Missing")
        }else{
            errorPassCount++
        }
        if (etPassword.text.isBlank()){
            etPassword.setError("Field Missing")
        }else{
            errorPassCount++
        }

        return errorPassCount==5
    }

    override fun onResume() {
        if(!ConnectionManager().checkConnectivity(this@SignUp)){
            val alterDailog=androidx.appcompat.app.AlertDialog.Builder(this@SignUp)

            alterDailog.setMessage("Internet connection can't be established!")
            alterDailog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent=Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }
            alterDailog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this)
            }
            alterDailog.setCancelable(false)
            alterDailog.create()
            alterDailog.show()
        }
        super.onResume()
    }
}