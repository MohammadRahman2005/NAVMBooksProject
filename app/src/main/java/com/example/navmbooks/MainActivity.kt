@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navmbooks

import ContentScreen
import HomeScreen
import LibraryScreen
import ReadingScreen
import SearchScreen
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
<<<<<<< HEAD
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
=======
>>>>>>> da275ad2ab36ac9a0bc7b1a3523f04fdc77984e9
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import com.example.navmbooks.utils.AdaptiveNavigationType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import com.example.navmbooks.utils.AdaptiveNavigationType



class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isReadingMode by rememberSaveable { mutableStateOf(false) }
            NAVMBooksTheme {
                val windowSize = calculateWindowSizeClass(this)
                BookReadingApp(
                    locale = Locale.US,
                    isReadingMode = isReadingMode,
                    onReadingModeChanged = { isReadingMode = it },
                    windowSizeClass = windowSize.widthSizeClass
                )
            }
        }
    }
}

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
    isReadingMode: Boolean,
    onReadingModeChanged: (Boolean) -> Unit,
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
            if (!isReadingMode && adaptiveNavigationType == AdaptiveNavigationType.BOTTOM_NAVIGATION) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { padding ->
        AdaptiveNavigationBars(
            padding = padding,
            navController = navController,
            isReadingMode = isReadingMode,
            onReadingModeChanged = onReadingModeChanged,
            adaptiveNavigationType = adaptiveNavigationType
        )
        MainScreen()
    }
}

@Composable
fun AdaptiveNavigationBars(
    padding: PaddingValues,
    navController: NavHostController,
    isReadingMode: Boolean,
    onReadingModeChanged: (Boolean) -> Unit,
    adaptiveNavigationType: AdaptiveNavigationType
) {
    Column(Modifier.padding(padding)) {
        NavigationHost(
            navController = navController,
            isReadingMode = isReadingMode,
            onReadingModeChanged = onReadingModeChanged
        )
    }
    Row(modifier = Modifier.padding(padding)) {
        if (!isReadingMode && adaptiveNavigationType == AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER) {
            PermanentNavigationDrawerComponent(navController = navController, isReadingMode = isReadingMode, onReadingModeChanged = onReadingModeChanged)
        }
        if (!isReadingMode && adaptiveNavigationType == AdaptiveNavigationType.NAVIGATION_RAIL) {
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
    isReadingMode: Boolean,
    onReadingModeChanged: (Boolean) -> Unit
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
                NavigationHost(navController = navController, isReadingMode = isReadingMode, onReadingModeChanged = onReadingModeChanged)
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
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 8.dp, start = 50.dp)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        actions = {
            Logo(modifier = Modifier.size(48.dp))
        }
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
    isReadingMode: Boolean,
    onReadingModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HomeScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = NavRoutes.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = NavRoutes.LibraryScreen.route) {
            LibraryScreen(navController = navController)
        }
        composable(route = NavRoutes.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(route = NavRoutes.ContentScreen.route) {
            ContentScreen(navController = navController)
        }
        composable(route = NavRoutes.ReadingScreen.route) {
            ReadingScreen(
                navController = navController,
                isReadingMode = isReadingMode,
                onReadingModeChanged = onReadingModeChanged
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
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
        ),
        BarItem(
            title = stringResource(id = R.string.content),
            image = Icons.Filled.PlayArrow,
            route = stringResource(id = R.string.content_screen)
        ),
        BarItem(
            title = stringResource(id = R.string.reading),
            image = Icons.Filled.Info,
            route = stringResource(id = R.string.reading_screen)
        )

    )

    return barItems
}



