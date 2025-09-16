package com.example.threadclone.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.threadclone.model.UserModel
import com.example.threadclone.navigation.Routes

@Composable
fun UserItem(
    userModel: UserModel,
    navHostController: NavHostController,
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp).clickable{
                val routes=Routes.OtherUsers.routes.replace("{data}",userModel.uid)
                navHostController.navigate(routes)
            }
    ) {

        // Top row: Profile + name + timestamp
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile image
                Image(
                    painter = rememberAsyncImagePainter(userModel.imageUrl),
                    contentDescription = "Profile image",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = userModel.userName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    // Post text
                    Text(
                        text = userModel.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }


//                Text(
//                    text = userModel.,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
//                Text(
//                    text = date,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
            }
        }

        Divider(color= Color.Gray, thickness = 1.dp)

    }
}