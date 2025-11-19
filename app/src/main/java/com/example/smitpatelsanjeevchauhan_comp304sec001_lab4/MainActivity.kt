package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model.LocationData
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.ui.theme.SmitPatelSanjeevChauhan_COMP304Sec001_Lab4Theme
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.view.SanjeevActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmitPatelSanjeevChauhan_COMP304Sec001_Lab4Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CategoryListScreen { categoryId ->
                        val intent = Intent(this, SanjeevActivity::class.java).apply {
                            putExtra("categoryId", categoryId)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }


    @Composable
    fun CategoryListScreen(onCategoryClick: (String) -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            // Background photo
            Image(
                painter = painterResource(id = R.drawable.tour),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay so text pops
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xAA000000),
                                Color(0x66000000),
                                Color(0xAA000000)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Toronto Tourism",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(LocationData.categories) { category ->
                        CategoryCard(
                            title = category.title,
                            imageRes = category.imageResId,
                            onClick = { onCategoryClick(category.id) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun CategoryCard(
        title: String,
        imageRes: Int,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark overlay bar with text in centre
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xCC000000),
                                    Color.Transparent,
                                    Color(0xCC000000)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                    )
                }
            }
        }
    }
}

