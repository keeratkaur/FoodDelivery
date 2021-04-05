package adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodie.activity.RestaurantMenu
import com.keerat.fummy.R
import com.squareup.picasso.Picasso
import database.RestaurantDatabase
import database.RestaurantEntities
import model.Restaurant

class HomeRecyclerAdapter(val context: Context,
                          var itemList:ArrayList<Restaurant>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tatRestaurantName: TextView =view.findViewById(R.id.txtRestaurentName)
        val txtRating: TextView =view.findViewById(R.id.txtRestaurentRating)
        val txtPrice: TextView =view.findViewById(R.id.txtPrice)
        val imgRestaurantImage: ImageView =view.findViewById(R.id.imgRestaurentImage)
        val txtfavourites: TextView =view.findViewById(R.id.txtfavourite)
        val llContent: LinearLayout =view.findViewById(R.id.llcontent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val restaurant=itemList[position]
        holder.tatRestaurantName.setTag(restaurant.id + "")
        holder.tatRestaurantName.text=restaurant.name
        holder.txtRating.text=restaurant.rating
        holder.txtPrice.text= restaurant.costForOne.toString()
        Picasso.get().load(restaurant.imageUrl).into(holder.imgRestaurantImage)

        val restaurantEntity = RestaurantEntities(
            restaurant.id,
            restaurant.name
        )

        holder.txtfavourites.setOnClickListener(View.OnClickListener {
            if (!DBAsynTask(
                    context,
                    restaurantEntity,
                    1
                ).execute().get()) {

                val result = DBAsynTask(
                    context,
                    restaurantEntity,
                    2
                ).execute().get()

                if (result) {

                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()

                    holder.txtfavourites.setTag("liked")//new value
                    holder.txtfavourites.background =
                        context.resources.getDrawable(R.drawable.ic_fill_in)
                } else {

                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()

                }

            } else {

                val result = DBAsynTask(
                    context,
                    restaurantEntity,
                    3
                ).execute().get()

                if (result) {

                    Toast.makeText(context, "Removed favourites", Toast.LENGTH_SHORT).show()

                    holder.txtfavourites.setTag("unliked")
                    holder.txtfavourites.background =
                        context.resources.getDrawable(R.drawable.ic_fav_outline)
                } else {

                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()

                }

            }
        })

        holder.llContent.setOnClickListener(View.OnClickListener {

            val intent = Intent(context, RestaurantMenu::class.java)

            intent.putExtra("restaurantId", holder.tatRestaurantName.getTag().toString())

            intent.putExtra("restaurantName", holder.tatRestaurantName.text.toString())


            context.startActivity(intent)


        })

        val checkFav = DBAsynTask(
            context,
            restaurantEntity,
            1
        ).execute()
        val isFav = checkFav.get()

        if (isFav) {
            holder.txtfavourites.setTag("liked")
            holder.txtfavourites.background =
                context.resources.getDrawable(R.drawable.ic_fill_in)

        } else {
            holder.txtfavourites.setTag("unliked")
            holder.txtfavourites.background =
                context.resources.getDrawable(R.drawable.ic_fav_outline)
        }

    }

    fun filterList(filteredList: ArrayList<Restaurant>) {//to update the recycler view depending on the search
        itemList = filteredList
        notifyDataSetChanged()
    }


    class DBAsynTask(val context: Context, val restaurantEntity: RestaurantEntities, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            /*
            * Mode 1->check if restaurant is in favourites
            * Mode 2->Save the restaurant into DB as favourites
            * Mode 3-> Remove the favourite restaurant*/


            when (mode) {
                1 -> {
                    val restaurant: RestaurantEntities? = db.restaurantDao()
                        .getRestaurantById(restaurantEntity.restaurantId)
                    db. close()
                    return restaurant != null
                }
                2 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                else -> return false

            }

        }


    }




}