package com.josecernu.rickandmortyapp.navigation

import DetailScreen
import MainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.Main.route,
    ) {
        composable(Destination.Main.route) {
            MainScreen(navController = navController)
        }
        composable(
            "${Destination.Detail.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) {
            DetailScreen(navController = navController, id = it.arguments?.getInt("id") ?: 0)
        }
    }
}
