package com.josecernu.rickandmortyapp.navigation

sealed class Destination(val route: String) {
    object Main : Destination("main_screen")

    object Detail : Destination("detail_screen")
}
