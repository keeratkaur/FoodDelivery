package adapter

import activity.Cart
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keerat.fummy.R

class RestaurantRecyclerMenuAdapter(
    val context: Context, val restaurantId: String,
    val restaurantName: String,
    val proceedToCartPassed: RelativeLayout,
    val buttonProceedToCart: Button,
    val Menu: ArrayList<model.Menu>
): RecyclerView.Adapter<RestaurantRecyclerMenuAdapter.ViewHolderMenu>() {

    var itemSelectedCount:Int=0
    lateinit var proceedToCart: RelativeLayout

    var itemSelectedId= arrayListOf<String>()

    class ViewHolderMenu(view: View): RecyclerView.ViewHolder(view){

        val textViewSerialNumber: TextView = view.findViewById(R.id.txtSerialNumber)
        val textViewItemName: TextView = view.findViewById(R.id.txtItemName)
        val textViewItemPrice: TextView = view.findViewById(R.id.txtItemPrice)
        val buttonAddToCart: Button = view.findViewById(R.id.btnAddToCart)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMenu {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_restaurent_menu_single_row, parent, false)

        return ViewHolderMenu(view)
    }

    override fun getItemCount(): Int {
        Menu.size
        return Menu.size
    }

    override fun onBindViewHolder(holder: ViewHolderMenu, position: Int) {

        val restaurantMenuItem = Menu[position]

        proceedToCart = proceedToCartPassed//button view passed from the RestaurantMenuActivity

        //click listener to the button view Passed from activity which has the button proceed to cart
        buttonProceedToCart.setOnClickListener(View.OnClickListener {

            val intent = Intent(context, Cart::class.java)

            intent.putExtra(
                "restaurantId",
                restaurantId.toString()
            )  // pass the restaurant id to the next acticity

            intent.putExtra("restaurantName", restaurantName)

            intent.putExtra(
                "selectedItemsId",
                itemSelectedId
            )//pass all the items selected by the user

            context.startActivity(intent)

        })

        holder.buttonAddToCart.setOnClickListener(View.OnClickListener {

            if (holder.buttonAddToCart.text.toString().equals("Remove")) {
                itemSelectedCount--//unselected

                itemSelectedId.remove(holder.buttonAddToCart.getTag().toString())

                holder.buttonAddToCart.text = "Add"

                holder.buttonAddToCart.setBackgroundColor(
                    Color.rgb(
                        244,
                        67,
                        54
                    )
                )//primary colour to rgb

            } else {
                itemSelectedCount++//selected

                itemSelectedId.add(holder.buttonAddToCart.getTag().toString())


                holder.buttonAddToCart.text = "Remove"

                holder.buttonAddToCart.setBackgroundColor(
                    Color.rgb(
                        255,
                        196,
                        0
                    )
                )//yellow colour to rgb

            }

            if (itemSelectedCount > 0) {
                proceedToCart.visibility = View.VISIBLE
            } else {
                proceedToCart.visibility = View.INVISIBLE
            }

        })

        holder.buttonAddToCart.setTag(restaurantMenuItem.id + "")//save the item id in textViewName Tag ,will be used to add to cart
        holder.textViewSerialNumber.text = (position + 1).toString()//position starts from 0
        holder.textViewItemName.text = restaurantMenuItem.name
        holder.textViewItemPrice.text = "Rs." + restaurantMenuItem.cost_for_one

    }

    fun getSelectedItemCount(): Int {
        return itemSelectedCount
    }


}