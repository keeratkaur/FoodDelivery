package fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.keerat.fummy.R

class ProfileFragment(val contextParam:Context)  : Fragment() {
    lateinit var txtName: TextView
    lateinit var txtPhoneNumber: TextView
    lateinit var txtEmail: TextView
    lateinit var txtViewAddress: TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        txtName=view.findViewById(R.id.txtName)
        txtEmail=view.findViewById(R.id.txtEmail)
        txtPhoneNumber=view.findViewById(R.id.txtPhoneNumber)
        txtViewAddress=view.findViewById(R.id.txtViewAddress)

        sharedPreferences = contextParam.getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        txtName.text = sharedPreferences.getString("name", "")
        txtEmail.text = sharedPreferences.getString("email", "")
        txtPhoneNumber.text = "+91-" + sharedPreferences.getString("mobile_number", "")
        txtViewAddress.text = sharedPreferences.getString("address", "")

        return view
    }

}