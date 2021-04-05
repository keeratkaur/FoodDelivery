package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntities)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntities)

    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): List<RestaurantEntities>

    @Query("SELECT * FROM restaurants WHERE restaurant_id = :restaurantId")
    fun getRestaurantById(restaurantId: String): RestaurantEntities


}