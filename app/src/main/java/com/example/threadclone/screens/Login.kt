package com.example.threadclone.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadclone.navigation.Routes
import com.example.threadclone.viewmodel.AuthViewModel

@Composable
//@Preview(showBackground = true)
fun Login(modifier: Modifier = Modifier,navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context= LocalContext.current

    val authViewModel: AuthViewModel= viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()


    LaunchedEffect(firebaseUser) {
        if(firebaseUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

//    error?.let{
//        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//    }




    Column(modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text ="Login",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp)

        Spacer(modifier= Modifier.height(50.dp))
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

        Spacer(modifier= Modifier.height(20.dp))

        OutlinedTextField(
            value=password,
            onValueChange = {
                password=it
            },
            label = {Text("Pas")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier= Modifier.height(30.dp))

        ElevatedButton(onClick = {
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            authViewModel.login(email,password,context)
        },
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp)

        }
        TextButton(onClick = {
            navController.navigate(Routes.Register.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop=true
            }
        }) {
        Text(text ="New User? Create Account",
            fontSize = 16.sp)
        }
    }
}