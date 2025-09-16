package com.example.threadclone.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.threadclone.navigation.Routes
import com.example.threadclone.R
import com.example.threadclone.viewmodel.AuthViewModel

//@Preview(showBackground = true)
@Composable
fun Register(modifier: Modifier = Modifier,navController: NavHostController) {

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel= viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context= LocalContext.current

    //for pickeing image if permission is granted
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val permissionRequest=if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    }else Manifest.permission.READ_EXTERNAL_STORAGE

//    for check weather the permission from user is granted or not
    val persmissionlauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean->
        if(isGranted){

        }
        else{

        }
    }

    LaunchedEffect(firebaseUser) {
        if(firebaseUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
//        else{
//            navController.navigate(Routes.Login.routes){
//                popUpTo(navController.graph.startDestinationId)
//                launchSingleTop = true
//            }
//        }
    }

    Column(modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text ="Register here",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp)

        Spacer(modifier= Modifier.height(25.dp))

        Image(painter = if(imageUri==null) painterResource(R.drawable.profile)
                        else rememberAsyncImagePainter(model = imageUri)
            , contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable{
                    val isGranted= ContextCompat.checkSelfPermission(
                        context,permissionRequest
                    )== PackageManager.PERMISSION_GRANTED

                    if(isGranted){
                        launcher.launch("image/*")
                    }else{
                        persmissionlauncher.launch(permissionRequest)
                    }

            }, contentScale = ContentScale.Crop)

        Spacer(modifier= Modifier.height(25.dp))

        OutlinedTextField(
            value=name,
            onValueChange = {
                name=it
            },
            label = {Text("Name")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

//        Spacer(modifier= Modifier.height(20.dp))

        OutlinedTextField(
            value=username,
            onValueChange = {
                username=it
            },
            label = {Text("Username")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

//        Spacer(modifier= Modifier.height(20.dp))

        OutlinedTextField(
            value=bio,
            onValueChange = {
                bio=it
            },
            label = {Text("Bio")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

//        Spacer(modifier= Modifier.height(20.dp))

        OutlinedTextField(
            value=email,
            onValueChange = {
                email=it
            },
            label = {Text("Email")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

//        Spacer(modifier= Modifier.height(20.dp))

        OutlinedTextField(
            value=password,
            onValueChange = {
                password=it
            },
            label = {Text("Password")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier= Modifier.height(30.dp))

        ElevatedButton(onClick = {
            if (name.isEmpty() || username.isEmpty() || bio.isEmpty() || email.isEmpty() || password.isEmpty()  || imageUri== null) {
                Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
            }else {
//                authViewModel.register(email, password, name, bio, username, imageUri!!,context)
//                navController.navigate(Routes.BottomNav.routes) {
//                    popUpTo(navController.graph.startDestinationId)
//                    launchSingleTop = true
                authViewModel.register(email, password, name, bio, username, imageUri!!,context)

//                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Register",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp)

        }
        TextButton(onClick = {
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }) {
            Text(text ="Already have an account? Login here",
                fontSize = 16.sp)
        }
    }
}