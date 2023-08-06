package com.example.notesapp.persantation.Login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.ui.theme.Background


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: LogInViewModel
) {
    val loginUitState = viewModel.logInUiState
    val isError = loginUitState.signUpError != null
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(Background)
            .fillMaxSize(),

        ) {

        Greeting()
        if (isError) {
            Text(
                text = loginUitState.signUpError ?: "unknown error",
                color = Color.Red
                )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp, 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // userName
            TextField(
                value = loginUitState.userNameSignup,
                onValueChange = {
                    viewModel.onSignUpUserNameChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp)
                    .border(
                        BorderStroke(2.dp, Color(0xFF72F269)),
                        shape = RoundedCornerShape(20)
                    ),
                shape = RoundedCornerShape(20),
                placeholder = {
                    Text("Email")
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            //password field
            TextField(
                value = loginUitState.passwordSignup,
                onValueChange = {
                    viewModel.onSignUpPasswordChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp)
                    .border(
                        BorderStroke(2.dp, Color(0xFF72F269)),
                        shape = RoundedCornerShape(20)
                    ),
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
                placeholder = {
                    Text("Password")
                }

            )  //confirm password field
            TextField(
                value = loginUitState.confirmPasswordSignup,
                onValueChange = {
                    viewModel.onSignUpConfirmPasswordChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp)
                    .border(
                        BorderStroke(2.dp, Color(0xFF72F269)),
                        shape = RoundedCornerShape(20)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(20),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (visible) VisualTransformation.None
                else PasswordVisualTransformation(),
                placeholder = {
                    Text("Confirm Password")
                }

            )
        }
        Button(
            onClick = {
                viewModel.createUser(context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 16.dp)
                .shadow(
                    ambientColor = Color.Blue,
                    spotColor = Color.Cyan,
                    elevation = 15.dp,
                    clip = true,
                    shape = RoundedCornerShape(20)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF72F269),
                contentColor = Color.Black
            ),

            shape = RoundedCornerShape(20)
        ) {
            Text(
                text = "Sign Up",
                color = Color.White
            )
        }
        Divider(Modifier.padding(16.dp),color = Color.White)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already a member? ",
                color = Color.White
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Sign in",
                color = Color(0xFF72F269),
                modifier = Modifier.clickable {
                    navigateToLogin.invoke()
                }
            )
        }


    }
    if (loginUitState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(key1 = viewModel.hasUser){
        if(viewModel.hasUser){
            navigateToHome.invoke()
        }
    }
}

@Composable
fun Greeting() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Are you new ?",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Text(
            text = "Create an Account",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
    }
}






