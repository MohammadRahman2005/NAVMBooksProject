package com.example.navmbooks

sealed class NavRoutes (val route: String) {
    data object HomeScreen : NavRoutes("home_screen")
    data object LibraryScreen : NavRoutes("library_screen")
    data object SearchScreen : NavRoutes("search_screen")
    data object ContentScreen : NavRoutes("content_screen")
    data object ReadingScreen : NavRoutes("reading_screen")
}