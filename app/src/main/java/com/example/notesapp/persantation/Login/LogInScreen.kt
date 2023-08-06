package com.example.notesapp.persantation.Login

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.R
import com.example.notesapp.ui.theme.Background
import com.example.notesapp.ui.theme.MyGreen
import kotlinx.coroutines.launch
import androidx.compose.material3.Text as Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    viewModel: LogInViewModel,

) {
    val context = LocalContext.current
    var loginUiState = viewModel.logInUiState
    val isError = loginUiState.logInError != null


    Column(
        Modifier
            .background(Background)
            .fillMaxSize(),

        ) {

        GreetingSection()
        if (isError) {
            Text(
                text = loginUiState.logInError ?: "unknown error",
                color = Color.Red
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(26.dp, 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 8.dp)

            ) {

                TextField(
                    singleLine= true ,
                    placeholder  = ({ Text(text = "Email") }),
                    value = loginUiState.userName,
                    onValueChange = {
                        viewModel.onUserNameChange(it)
                    },
                    modifier = Modifier
                        .border(
                            BorderStroke(2.dp, MyGreen),
                            shape = RoundedCornerShape(20)
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20),


                    )
            }

            // password field

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 8.dp)

            ) {
                var visible by remember {
                    mutableStateOf(false)
                }
                TextField(
                    placeholder = ({ Text(text = "Password") }),
                    value = loginUiState.password,
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                    },
                   singleLine= true ,
                    modifier = Modifier
                        .border(
                            BorderStroke(2.dp, MyGreen),
                            shape = RoundedCornerShape(20)
                        )
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(20),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(if (visible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                visible = !visible
                            },
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (visible) VisualTransformation.None
                    else PasswordVisualTransformation(),

                    )
            }

            Text(
                text = "Forget Password?",
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                color = Color.White,

                )

            Button(
                onClick = {
                    viewModel.logIn(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp)
                    .shadow(
                        ambientColor = Color.Blue,
                        spotColor = Color.Cyan,
                        elevation = 15.dp,
                        clip = true,
                        shape = RoundedCornerShape(20)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyGreen,
                    contentColor = Color.Black
                ),

                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White
                )
            }
        }
        Divider(color = Color.White, modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Are you new ?",
                color = Color.White
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Register here",
                color = Color(0xFF72F269),
                modifier = Modifier.clickable {
                    navigateToSignUp.invoke()
                }
            )
        }
        if (loginUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LaunchedEffect(key1 = viewModel.hasUser, block = {
            if (viewModel.hasUser) {
                navigateToHome.invoke()
            }
        })
    }
}


@Composable
fun GreetingSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Hello Again!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(16.dp),
            color = Color.White
        )
        Text(
            text = "Welcome back you've",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Text(
            text = "been missed!",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

    }
}

