package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fragments.FaqsFragment
import com.keerat.fummy.R
import fragments.FavRestaurant
import fragments.HomeFragment

import fragments.ProfileFragment

class MainActivity2 : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editTextMNumber:EditText
    lateinit var textViewUser: TextView
    var previousMenuItem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        navigationView = findViewById(R.id.navigationView)
        sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        val headerView = navigationView.getHeaderView(0)

        editTextMNumber = headerView.findViewById(R.id.editTextMNumber)

        textViewUser.text = sharedPreferences.getString("name", "User")
        editTextMNumber.hint =
            "+91-" + sharedPreferences.getString("mobile_number", "9999999999")

        setUpToolbar()
        openHome()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity2,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment(this))
                        .addToBackStack("Home")
                        .commit()

                    supportActionBar?.title = "Home"

                    drawerLayout.closeDrawers()
                }
                R.id.myProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment(this))
                        .addToBackStack("MyProfile")
                        .commit()

                    supportActionBar?.title = "My Profile"

                    drawerLayout.closeDrawers()
                }
                R.id.favRestaurant -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavRestaurant(this))
                        .addToBackStack("Favourite Restaurant")
                        .commit()
                    supportActionBar?.title = "Favorite Restaurants"


                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory -> {
                    val intent = Intent(this, OrderHistory::class.java)
                    drawerLayout.closeDrawers()
                    startActivity(intent)


                    supportActionBar?.title = "Order History"


                    drawerLayout.closeDrawers()
                }
                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqsFragment()
                        )
                        .addToBackStack("FAQs")
                        .commit()

                    supportActionBar?.title = "FAQs"


                    drawerLayout.closeDrawers()
                }

                R.id.logOut -> {
                    drawerLayout.closeDrawers()

                    val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)

                    alterDialog.setTitle("Confirmation")
                    alterDialog.setMessage("Are you sure you want to log out?")
                    alterDialog.setPositiveButton("Yes") { text, listener ->
                        sharedPreferences.edit().putBoolean("user_logged_in", false).apply()

                        ActivityCompat.finishAffinity(this)
                    }

                    alterDialog.setNegativeButton("No") { text, listener ->

                    }
                    alterDialog.create()
                    alterDialog.show()

                }



            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        val fragment =
            HomeFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
        drawerLayout.closeDrawers()
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (frag) {
            !is HomeFragment -> openHome()

            else -> super.onBackPressed()
        }


    }
}