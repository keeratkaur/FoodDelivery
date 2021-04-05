package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
class RestaurantEntities (
    @ColumnInfo(name = "restaurant_id") @PrimaryKey var restaurantId: String,
    @ColumnInfo(name = "restaurant_name") var restaurantName: String
)