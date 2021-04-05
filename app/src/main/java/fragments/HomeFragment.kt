package fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import adapter.HomeRecyclerAdapter
import com.keerat.fummy.R
import kotlinx.android.synthetic.main.sort_radio_button.view.*
import model.Restaurant
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class HomeFragment(val contextParam:Context) : Fragment() {


    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager

    lateinit var recyclerAdapter: HomeRecyclerAdapter
    var restaurantInfoList= arrayListOf<Restaurant>()
    lateinit var radioButtonView:View

    var ratingComparator = Comparator<Restaurant> { rest1, rest2 ->

        if (rest1.rating.compareTo(rest2.rating, true) == 0) {
            rest1.name.compareTo(rest2.name, true)
        } else {
            rest1.rating.compareTo(rest2.rating, true)
        }

    }

    var costComparator = Comparator<Restaurant> { rest1, rest2 ->

        rest1.costForOne.compareTo(rest2.costForOne, true)

    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)


        val view=inflater.inflate(R.layout.fragment_home, container, false)



        recyclerHome=view.findViewById(R.id.recyclerHome) as RecyclerView
        layoutManager= LinearLayoutManager(activity)

        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://" +"13.235.250.119"+ "/v2/restaurants/fetch_result"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest=object : JsonObjectRequest(Request.Method.GET,url,null, Response.Listener{
                try {
                    val response=it.getJSONObject("data")

                    val success=response.getBoolean("success")
                    if(success){
                        val data=response.getJSONArray("data")


                        for (i in 0 until data.length()){
                            val restaurantJsonObject=data.getJSONObject(i)

                            val restaurantObject=Restaurant (
                                restaurantJsonObject.getString("id"),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("rating"),
                                restaurantJsonObject.getString("cost_for_one"),
                                restaurantJsonObject.getString("image_url")

                            )
                            restaurantInfoList.add(restaurantObject)

                            recyclerAdapter= HomeRecyclerAdapter(
                                activity as Context,
                                restaurantInfoList
                            )
                            recyclerHome.adapter=recyclerAdapter
                            recyclerHome.layoutManager=layoutManager

                            recyclerHome.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerHome.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )



                        }

                    }else{
                        Toast.makeText(activity as Context,"Some error Occurred",Toast.LENGTH_SHORT).show()
                    }
                }catch (e: JSONException){
                    Toast.makeText(activity as Context,"Json Exception Occurred!!!", Toast.LENGTH_SHORT).show()
                }


            },Response.ErrorListener {
                Toast.makeText(activity as Context,"Volley error occured",Toast.LENGTH_SHORT ).show()

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-Type"]="application/json"
                    headers["token"]="ff1b87d4d13450"

                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        }else{

            val dailog = AlertDialog.Builder(activity as Context)
            dailog.setTitle("ERROR")
            dailog.setMessage("Internet connection is not found")
            dailog.setPositiveButton("Open setting") { text, listener ->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()

            }

            dailog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dailog.create()
            dailog.show()


        }


        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when (id) {

            R.id.sort -> {
                radioButtonView = View.inflate(
                    contextParam,
                    R.layout.sort_radio_button,
                    null
                )//radiobutton view for sorting display
                androidx.appcompat.app.AlertDialog.Builder(activity as Context)
                    .setTitle("Sort By?")
                    .setView(radioButtonView)
                    .setPositiveButton("OK") { text, listener ->
                        if (radioButtonView.radio_high_to_low.isChecked) {
                            Collections.sort(restaurantInfoList, costComparator)
                            restaurantInfoList.reverse()
                            recyclerAdapter.notifyDataSetChanged()//updates the adapter
                        }
                        if (radioButtonView.radio_low_to_high.isChecked) {
                            Collections.sort(restaurantInfoList, costComparator)
                            recyclerAdapter.notifyDataSetChanged()//updates the adapter
                        }
                        if (radioButtonView.radio_rating.isChecked) {
                            Collections.sort(restaurantInfoList, ratingComparator)
                            restaurantInfoList.reverse()
                            recyclerAdapter.notifyDataSetChanged()//updates the adapter
                        }
                    }
                    .setNegativeButton("CANCEL") { text, listener ->

                    }
                    .create()
                    .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        if (!ConnectionManager().checkConnectivity(activity as Context)) {


            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)//closes all the instances of the app and the app closes completely
            }
            alterDialog.setCancelable(false)

            alterDialog.create()
            alterDialog.show()

        }

        super.onResume()
    }

}