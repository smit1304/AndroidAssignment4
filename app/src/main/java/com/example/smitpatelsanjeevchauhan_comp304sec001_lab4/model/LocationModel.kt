package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model

import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.R
import com.google.android.gms.maps.model.LatLng

// Place category
data class LocationCategory(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int
)

// A place inside a category
data class LocationPlace(
    val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val address: String,
    val location: LatLng,
    val imageResId: Int
)

// Location Data
object LocationData {

    val categories = listOf(
        LocationCategory(
            id = "restaurant",
            title = "RESTAURANT",
            description = "Explore Toronto's Restaurants.",
            imageResId = R.drawable.restaurant
        ),
        LocationCategory(
            id = "park",
            title = "PARK",
            description = "Explore Toronto's Parks.",
            imageResId = R.drawable.park
        ),
        LocationCategory(
            id = "museum",
            title = "MUSEUM",
            description = "Explore Toronto's Museum.",
            imageResId = R.drawable.museum
        )
    )

    val places = listOf(
        // Restaurant category
        LocationPlace(
            id = "buca",
            categoryId = "restaurant",
            name = "Buca",
            description = "Italian restaurant offering handmade pasta & wood-fired pizza in a trendy setting.",
            address = "604 King St W, Toronto, ON",
            location = LatLng(43.6458, -79.3983),
            imageResId = R.drawable.res
        ),
        LocationPlace(
            id = "barchef",
            categoryId = "restaurant",
            name = "BarChef",
            description = "Creative cocktails & contemporary small plates served in a stylish, dimly lit space.",
            address = "472 Queen St W, Toronto, ON",
            location = LatLng(43.6491, -79.3973),
            imageResId = R.drawable.res
        ),
        LocationPlace(
            id = "canoe",
            categoryId = "restaurant",
            name = "Canoe",
            description = "Upscale, fine dining with a panoramic view of Toronto's skyline and Lake Ontario.",
            address = "66 Wellington St W 54th Floor, Toronto, ON",
            location = LatLng(43.6460, -79.3805),
            imageResId = R.drawable.res
        ),

        // Park category
        LocationPlace(
            id = "highpark",
            categoryId = "park",
            name = "High Park",
            description = "A large urban park with trails, sports facilities, a zoo, and beautiful gardens.",
            address = "1873 Bloor St W, Toronto, ON",
            location = LatLng(43.6465, -79.4637),
            imageResId = R.drawable.hp
        ),
        LocationPlace(
            id = "evergreenbrickworks",
            categoryId = "park",
            name = "Evergreen Brick Works",
            description = "A community environmental center with hiking trails, gardens, and outdoor activities.",
            address = "550 Bayview Ave, Toronto, ON",
            location = LatLng(43.6887, -79.3581),
            imageResId = R.drawable.eb
        ),
        LocationPlace(
            id = "trinitybellwoods",
            categoryId = "park",
            name = "Trinity Bellwoods Park",
            description = "A popular city park offering green space, picnic areas, and a lively atmosphere.",
            address = "790 Queen St W, Toronto, ON",
            location = LatLng(43.6452, -79.4127),
            imageResId = R.drawable.tw
        ),

        // Museum category
        LocationPlace(
            id = "rom",
            categoryId = "museum",
            name = "Royal Ontario Museum",
            description = "World culture & natural history.",
            address = "100 Queens Park, Toronto, ON",
            location = LatLng(43.6677, -79.3948),
            imageResId = R.drawable.rom
        ),
        LocationPlace(
            id = "ago",
            categoryId = "museum",
            name = "Art Gallery of Ontario",
            description = "One of the largest art museums in North America, with extensive collections of art.",
            address = "317 Dundas St W, Toronto, ON",
            location = LatLng(43.6536, -79.3925),
            imageResId = R.drawable.ago
        ),
        LocationPlace(
            id = "osc",
            categoryId = "museum",
            name = "Ontario Science Centre",
            description = "Interactive exhibits focused on science and technology.",
            address = "770 Don Mills Rd, Toronto, ON",
            location = LatLng(43.7162, -79.3389),
            imageResId = R.drawable.osc
        )
    )

    fun getPlacesForCategory(categoryId: String) =
        places.filter { it.categoryId == categoryId }

    fun getPlaceById(placeId: String) =
        places.find { it.id == placeId }
}
