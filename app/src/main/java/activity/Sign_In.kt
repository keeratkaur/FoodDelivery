package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.keerat.fummy.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import model.SharedPrefManager
import model.User
import model.VolleySingleton
import org.json.JSONException
import java.util.HashMap

import org.json.JSONObject
import util.ConnectionManager

class Sign_In : AppCompatActivity() {
    lateinit var etMobileNo: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogIn: Button
    lateinit var txtForgetPass: TextView
    lateinit var txtNewAcc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign__in)

        etMobileNo=findViewById(R.id.etMobileNo)
        etPassword=findViewById(R.id.etPassword)
        btnLogIn=findViewById(R.id.btnLogIn)
        txtForgetPass=findViewById(R.id.txtForgotPass)
        txtNewAcc=findViewById(R.id.txtNewAcc)


        txtNewAcc.setOnClickListener{
            val intent=Intent(this@Sign_In, SignUp::class.java)
            startActivity(intent)
        }

        txtForgetPass.setOnClickListener{

            val intent=Intent(this@Sign_In,ForgotPass::class.java)
            startActivity(intent)
        }

        btnLogIn.setOnClickListener {
            if(etMobileNo.text.isBlank()){
                etMobileNo.setError("Mobile number missing")
            }else{
                if (etPassword.text.isBlank()){
                    etPassword.setError("Missing Password")
                }else{
                    logInUserFun()
                }
            }
        }
    }
    fun logInUserFun(){

        val sharedPreferences: SharedPreferences =getSharedPreferences(getString(R.string.shared_preferences),
            Context.MODE_PRIVATE)

        if (ConnectionManager().checkConnectivity(this@Sign_In)){

            try {
                val logInUser=JSONObject()

                logInUser.put("mobile_number",etMobileNo.text)
                logInUser.put("password",etPassword.text)

                val queue= Volley.newRequestQueue(this@Sign_In)

                val url="http://13.235.250.119/v2/login/fetch_result"

                val jsonObjectRequest=object :
                    JsonObjectRequest(Request.Method.POST,url,logInUser,Response.Listener{

                    val responseJsonObjectData=it.getJSONObject("data")
                    val success=responseJsonObjectData.getBoolean("success")

                    if(success){

                        val data=responseJsonObjectData.getJSONObject(("data"))
                        sharedPreferences.edit().putBoolean("user_logged_in",true).apply()
                        sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
                        sharedPreferences.edit().putString("name",data.getString("name")).apply()
                        sharedPreferences.edit().putString("email",data.getString("email")).apply()
                        sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
                        sharedPreferences.edit().putString("address",data.getString("address")).apply()
                        Toast.makeText(this@Sign_In,"welcome " + data.getString("name"),Toast.LENGTH_SHORT).show()

                        userSuccessfulLoggedIn()

                    }else{
                        println(it)
                        val responseMessageServer=responseJsonObjectData.getString("errorMessage")
                        Toast.makeText(this@Sign_In,responseMessageServer.toString(),Toast.LENGTH_SHORT).show()

                    }

                },
                    Response.ErrorListener {
                        println(it)
                        Toast.makeText(this@Sign_In,"Some error occurred",Toast.LENGTH_SHORT).show()

                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers=HashMap<String,String>()

                        headers["Content_Type"]="application/json"
                        headers["token"]="ff1b87d4d13450"

                        return headers


                    }
                }
                queue.add(jsonObjectRequest)
            }catch (e:JSONException){
                Toast.makeText(this@Sign_In,"some error occurred!!!",Toast.LENGTH_SHORT).show()
            }
        }else{
            val alterDailog=androidx.appcompat.app.AlertDialog.Builder(this@Sign_In)

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
    fun userSuccessfulLoggedIn(){
        val intent=Intent(this@Sign_In, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        if(!ConnectionManager().checkConnectivity(this@Sign_In)){
            val alterDailog=androidx.appcompat.app.AlertDialog.Builder(this@Sign_In)

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
        super.onResume()
    }
}