@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navmbooks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import com.example.navmbooks.utils.AdaptiveNavigationType
import com.example.navmbooks.viewpoints.ContentScreen
import com.example.navmbooks.viewpoints.HomeScreen
import com.example.navmbooks.viewpoints.LibraryScreen
import com.example.navmbooks.viewpoints.ReadingScreen
import com.example.navmbooks.viewpoints.SearchScreen
import java.util.Locale

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view and determines the navigation style
 * based on the device's window size.
 */
class MainActivity : ComponentActivity() {
    private val factory by lazy {
        BookViewModelFactory(this.applicationContext) // Use application context to prevent memory leaks
    }
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NAVMBooksTheme {
                val windowSize = calculateWindowSizeClass(this)
                BookReadingApp(
                    locale = Locale.US,
                    windowSizeClass = Expanded,
                    factory=factory
                )
            }
        }
    }
}

/**
 * BookReadingApp handles the main composable structure of the app,
 * including navigation and adapting the UI based on the window size class.
 */
@Composable
fun BookReadingApp(
    navController: NavHostController = rememberNavController(),
    locale: Locale,
    windowSizeClass: WindowWidthSizeClass,
    factory: BookViewModelFactory
) {
    val bookViewModel: BookViewModel = viewModel(factory=factory)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val adaptiveNavigationType = when (windowSizeClass) {
        Compact -> AdaptiveNavigationType.BOTTOM_NAVIGATION
        Medium -> AdaptiveNavigationType.NAVIGATION_RAIL
        else -> AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER
    }

    Scaffold(
        topBar = {
            // Display the app bar only on specific screens
            if (currentRoute != NavRoutes.ContentScreen.route && currentRoute != NavRoutes.ReadingScreen.route) {
                NAVMAppBar(navigateUp = { navController.navigateUp() })
            }
        },
        bottomBar = {
            // Show a bottom navigation bar if the current mode supports it
            if (!bookViewModel.isReadingMode.value && adaptiveNavigationType == AdaptiveNavigationType.BOTTOM_NAVIGATION) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { padding ->
        AdaptiveNavigationBars(
            padding = padding,
            navController = navController,
            bookViewModel = bookViewModel,
            adaptiveNavigationType = adaptiveNavigationType,
        )

    }
}


/**
 * AdaptiveNavigationBars handles the adaptive navigation structure,
 * supporting drawer, rail, or bottom navigation based on the device.
 */
@Composable
fun AdaptiveNavigationBars(
    padding: PaddingValues,
    navController: NavHostController,
    bookViewModel: BookViewModel,
    adaptiveNavigationType: AdaptiveNavigationType,
    modifier: Modifier = Modifier,
) {
    when (adaptiveNavigationType) {
        AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            Row(modifier = Modifier.padding(padding)) {
                PermanentNavigationDrawerComponent(
                    navController = navController,
                    bookViewModel = bookViewModel
                )
            }
        }
        else -> {
            Column(Modifier.padding(padding)) {
                val paddingVal = if (adaptiveNavigationType == AdaptiveNavigationType.NAVIGATION_RAIL) {
                    PaddingValues(start = dimensionResource(R.dimen.small_padding))
                } else {
                    PaddingValues(dimensionResource(R.dimen.zero_padding))
                }

                NavigationHost(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    modifier = modifier,
                    padding = paddingVal,
                )
            }
            if (adaptiveNavigationType == AdaptiveNavigationType.NAVIGATION_RAIL) {
                Row(modifier = Modifier.padding(padding)) {
                    NavigationRailComponent(
                        navController = navController,
                        bookViewModel = bookViewModel
                    )
                }
            }
        }
    }
}

/**
 * Defines the Navigation Rail for medium-sized screens.
 */
@Composable
fun NavigationRailComponent(
    navController: NavHostController,
    bookViewModel: BookViewModel
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutes = backStackEntry?.destination?.route
    NavigationRail {
        if (!bookViewModel.isReadingMode.value) {
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
}

/**
 * Defines the Permanent Navigation Drawer for large screens.
 */
@Composable
fun PermanentNavigationDrawerComponent(
    navController: NavHostController,
    bookViewModel: BookViewModel
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutes = backStackEntry?.destination?.route

    PermanentNavigationDrawer(
        drawerContent = {
            if (!bookViewModel.isReadingMode.value) {
                PermanentDrawerSheet {
                    Column {
                        Spacer(Modifier.height(dimensionResource(R.dimen.large_size)))
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
                }
            }
        },
        content = {
            NavigationHost(
                navController = navController,
                bookViewModel = bookViewModel,
                modifier = Modifier,
                padding = PaddingValues(dimensionResource(R.dimen.zero_padding))
            )
        }
    )
}

/**
 * Defines the Top App Bar with a back button and logo.
 */
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
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.small_padding), start = dimensionResource(R.dimen.large_padding)),
                color = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp , modifier.testTag("backButton")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            Logo(modifier = Modifier.size(dimensionResource(R.dimen.large_size)))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    )
}

/**
 * Displays the app logo in the top bar.
 */
@Composable
fun Logo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.navm),
        contentDescription = stringResource(R.string.logo_desc),
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}


/**
 * Hosts the navigation routes and connects screens.
 */
@Composable
fun NavigationHost(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues,
) {
    val books = bookViewModel.bookList
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HomeScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = NavRoutes.HomeScreen.route) {
            HomeScreen(navController = navController, modifier, padding)
        }
        composable(route = NavRoutes.LibraryScreen.route) {
            LibraryScreen(navController = navController, modifier, padding, books = books)
        }
        composable(route = NavRoutes.SearchScreen.route) {
            SearchScreen(navController = navController, modifier, padding)
        }
        composable(route = NavRoutes.ContentScreen.route, arguments = listOf(navArgument("bookIndex") { type = NavType.IntType })) {
            backStackEntry ->
            val bookIndex = backStackEntry.arguments?.getInt("bookIndex") ?: 0
            ContentScreen(navController = navController, modifier, padding, bookViewModel, books = books, bookIndex = bookIndex)
        }
        composable(route = NavRoutes.ReadingScreen.route, arguments = listOf(navArgument("chapterIndex") { type = NavType.StringType }, navArgument("bookIndex") { type = NavType.IntType })) {
            backStackEntry ->
            val bookIndex = backStackEntry.arguments?.getInt("bookIndex") ?: 0
            val chapterIndex = backStackEntry.arguments?.getString("chapterIndex")?.toIntOrNull()
            chapterIndex?.let { books[bookIndex]?.chapters?.get(it) }?.let {
                ReadingScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    modifier = modifier,
                    padding = padding,
                    it
                )
            }
        }
    }
}

/**
 * Defines the Bottom Navigation Drawer for phone screens.
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoutes = backStackEntry?.destination?.route

        navBarItems().forEach { navItem ->
            NavigationBarItem(
                selected = currentRoutes == navItem.route,
                onClick = {
                    if (currentRoutes != navItem.route) {
                        navController.navigate(navItem.route)
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