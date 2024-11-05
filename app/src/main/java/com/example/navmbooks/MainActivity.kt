@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navmbooks

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import com.example.navmbooks.utils.AdaptiveNavigationType
import com.example.navmbooks.viewpoints.ContentScreen
import com.example.navmbooks.viewpoints.HomeScreen
import com.example.navmbooks.viewpoints.LibraryScreen
import com.example.navmbooks.viewpoints.ReadingScreen
import com.example.navmbooks.viewpoints.SearchScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NAVMBooksTheme {
                val windowSize = calculateWindowSizeClass(this)
                val bookViewModel: BookViewModel = viewModel()
                BookReadingApp(
                    locale = Locale.US,
                    windowSizeClass = windowSize.widthSizeClass,
                    bookViewModel = bookViewModel
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    scope.launch(Dispatchers.IO) {
        try
        {
            context.assets.open("pg20195-h/pg20195-images.html").use { inputStream ->
                val book = Book.readBook(inputStream)
//                Log.d("MainScreen", "Book parsed: $book")
            }
            val book = Book.readBookURL("https://www.gutenberg.org/cache/epub/8710/pg8710-images.html")
            val book2 = Book.readBookURL("https://www.gutenberg.org/cache/epub/20195/pg20195-images.html")
            val book3 = Book.readBookURL("https://www.gutenberg.org/cache/epub/40367/pg40367-images.html")
            Log.d("MainScreen", "Book parsed: $book")
        } catch (e: IOException) {
            Log.e("MainScreen", "Error reading book", e)
        }

    }
}

@Composable
fun BookReadingApp(
    navController: NavHostController = rememberNavController(),
    locale: Locale,
    bookViewModel: BookViewModel,
    windowSizeClass: WindowWidthSizeClass
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val adaptiveNavigationType = when (windowSizeClass) {
        Compact -> AdaptiveNavigationType.BOTTOM_NAVIGATION
        Medium -> AdaptiveNavigationType.NAVIGATION_RAIL
        else -> AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER
    }

    Scaffold(
        topBar = {
            if (currentRoute != NavRoutes.ContentScreen.route && currentRoute != NavRoutes.ReadingScreen.route) {
                NAVMAppBar(navigateUp = { navController.navigateUp() })
            }
        },
        bottomBar = {
            if (!bookViewModel.isReadingMode.value && adaptiveNavigationType == AdaptiveNavigationType.BOTTOM_NAVIGATION) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { padding ->
        AdaptiveNavigationBars(
            padding = padding,
            navController = navController,
            bookViewModel = bookViewModel,
            adaptiveNavigationType = adaptiveNavigationType
        )
        MainScreen()
    }
}

@Composable
fun AdaptiveNavigationBars(
    padding: PaddingValues,
    navController: NavHostController,
    bookViewModel: BookViewModel,
    adaptiveNavigationType: AdaptiveNavigationType,
    modifier: Modifier = Modifier
) {
    Column(Modifier.padding(padding)) {
        val padding = if (adaptiveNavigationType == AdaptiveNavigationType.NAVIGATION_RAIL) {
            PaddingValues(start = 100.dp)
        } else {
            PaddingValues(0.dp)
        }

        NavigationHost(
            navController = navController,
            bookViewModel = bookViewModel,
            modifier = modifier,
            padding = padding
        )
    }
    Row(modifier = Modifier.padding(padding)) {
        if (!bookViewModel.isReadingMode.value && adaptiveNavigationType == AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER) {
            PermanentNavigationDrawerComponent(navController = navController, bookViewModel = bookViewModel)
        }
        if (!bookViewModel.isReadingMode.value && adaptiveNavigationType == AdaptiveNavigationType.NAVIGATION_RAIL) {
            NavigationRailComponent(navController = navController)
        }
    }
}

@Composable
fun NavigationRailComponent(
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutes = backStackEntry?.destination?.route
    NavigationRail {
        navBarItems().forEach { navItem ->
            NavigationRailItem(
                selected = currentRoutes == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(navItem.image, contentDescription = navItem.title)
                },
                label = { Text(text = navItem.title) }
            )
        }
    }
}

@Composable
fun PermanentNavigationDrawerComponent(
    navController: NavHostController,
    bookViewModel: BookViewModel
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutes = backStackEntry?.destination?.route
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet {
                Column {
                    Spacer(Modifier.height(12.dp))
                    navBarItems().forEach { navItem ->
                        NavigationDrawerItem(
                            selected = currentRoutes == navItem.route,
                            onClick = {
                                navController.navigate(navItem.route)
                            },
                            icon = {
                                Icon(navItem.image, contentDescription = navItem.title)
                            },
                            label = { Text(text = navItem.title) }
                        )
                    }
                }
            } },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                NavigationHost(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    modifier = Modifier,
                    padding = PaddingValues(0.dp)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NAVMAppBar(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp, start = 50.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp , modifier.testTag("backButton")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            Logo(modifier = Modifier.size(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.navm),
        contentDescription = "App Logo",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HomeScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = NavRoutes.HomeScreen.route) {
            HomeScreen(navController = navController, modifier, padding)
        }
        composable(route = NavRoutes.LibraryScreen.route) {
            LibraryScreen(navController = navController, modifier, padding)
        }
        composable(route = NavRoutes.SearchScreen.route) {
            SearchScreen(navController = navController, modifier, padding)
        }
//        composable(route = NavRoutes.ContentScreen.route) {
//            ContentScreen(navController = navController, modifier, padding)
//        }
//        composable(route = NavRoutes.ReadingScreen.route) {
//            ReadingScreen(
//                navController = navController,
//                bookViewModel = bookViewModel,
//                modifier = modifier,
//                padding = padding
//            )
//        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoutes = backStackEntry?.destination?.route

        navBarItems().forEach { navItem ->
            NavigationBarItem(
                selected = currentRoutes == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                },
            )
        }
    }
}

@Composable
fun navBarItems() : List<BarItem> {
    val  barItems = listOf(
        BarItem(
            title = stringResource(id = R.string.home),
            image = Icons.Filled.Home,
            route = stringResource(id = R.string.home_screen)
        ),
        BarItem(
            title = stringResource(id = R.string.library),
            image = Icons.Filled.Menu,
            route = stringResource(id = R.string.library_screen)
        ),
        BarItem(
            title = stringResource(id = R.string.search),
            image = Icons.Filled.Search,
            route = stringResource(id = R.string.search_screen)
        )
    )

    return barItems
}