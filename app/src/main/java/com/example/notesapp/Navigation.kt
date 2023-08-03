package com.example.notesapp

import android.app.usage.NetworkStats
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.notesapp.persantation.home.HomeScreen
import com.example.notesapp.persantation.Login.LogInScreen
import com.example.notesapp.persantation.Login.LogInViewModel
import com.example.notesapp.persantation.Login.RegistrationScreen
import com.example.notesapp.persantation.detail.DetailScreen
import com.example.notesapp.persantation.detail.DetailViewModel
import com.example.notesapp.persantation.home.HomeViewModel
import kotlin.math.sin


enum class LoginRoutes {
    SignUp,
    SignIn
}

enum class HomeRoute {
    Home,
    Detail
}

enum class NestedRoutes {
    Main,
    Login
}


@Composable
fun Navigation(
    logInViewModel: LogInViewModel,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NestedRoutes.Login.name) {
        authGraph(navController, logInViewModel)
        homeGraph(navController,detailViewModel,homeViewModel)
        }
    }


fun NavGraphBuilder.authGraph(
    navController: NavController,
    logInViewModel: LogInViewModel
) {
    navigation(
        startDestination = LoginRoutes.SignIn.name,
        route = NestedRoutes.Login.name
    ) {
        composable(
            route = LoginRoutes.SignIn.name
        ) {
            LogInScreen(
                navigateToHome = {
                    navController.navigate(NestedRoutes.Main.name) {
                        launchSingleTop = true
                        popUpTo(LoginRoutes.SignIn.name) {
                            inclusive = true
                        }
                    }
                },
                navigateToSignUp = {
                    navController.navigate(LoginRoutes.SignUp.name) {
                        launchSingleTop = true
                        popUpTo(LoginRoutes.SignIn.name) {
                            inclusive = true
                        }
                    }
                },
                viewModel = logInViewModel
            )
        }
        composable(route = LoginRoutes.SignUp.name) {
            RegistrationScreen(
                navigateToHome = {
                    navController.navigate(NestedRoutes.Main.name) {
                        popUpTo(LoginRoutes.SignUp.name) {
                            inclusive = true
                        }
                    }
                },
                navigateToLogin = {
                    navController.navigate(LoginRoutes.SignIn.name)
                },
                viewModel = logInViewModel
            )
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    detailViewModel: DetailViewModel,
    homeViewModel: HomeViewModel
) {
    navigation(
        startDestination = HomeRoute.Home.name,
        route = NestedRoutes.Main.name
    ){
        composable(route = HomeRoute.Home.name){
            HomeScreen(
                onNoteClick = { noteId ->
                    navController.navigate(HomeRoute.Detail.name + "?id=$noteId") {
                        launchSingleTop = true
                    }
                },
                navigateToSignIn = {
                                   navController.navigate(route= NestedRoutes.Login.name){
                                       launchSingleTop= true
                                       popUpTo(0){
                                           inclusive = true
                                       }
                                   }
                },
                viewModel = homeViewModel,
                navigateToDetailScreen = {
                    navController.navigate(HomeRoute.Detail.name)
                }
            )
        }
        composable(
            route = HomeRoute.Detail.name+"?id={id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )

        ){
            val id  = it.arguments?.getString("id")
            DetailScreen(viewModel = detailViewModel, noteId = id!!) {
                navController.navigateUp()
            }
        }
    }
}

