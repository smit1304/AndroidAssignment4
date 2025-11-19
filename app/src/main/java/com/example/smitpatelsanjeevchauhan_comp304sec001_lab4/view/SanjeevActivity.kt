package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model.LocationData
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.ui.theme.SmitPatelSanjeevChauhan_COMP304Sec001_Lab4Theme

class SanjeevActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryId = intent.getStringExtra("categoryId") ?: "museum"

        setContent {
            SmitPatelSanjeevChauhan_COMP304Sec001_Lab4Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlacesListScreen(
                        categoryId = categoryId,
                        onPlaceClick = { placeId ->
                            val intent = Intent(this, SmitActivity::class.java).apply {
                                putExtra("placeId", placeId)
                            }
                            startActivity(intent)
                        },
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesListScreen(
    categoryId: String,
    onPlaceClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val places = remember { LocationData.getPlacesForCategory(categoryId) }
    val category = LocationData.categories.find { it.id == categoryId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = category?.title ?: "Places")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(places) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPlaceClick(place.id) },
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = place.imageResId),
                            contentDescription = place.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = place.address,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = place.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
