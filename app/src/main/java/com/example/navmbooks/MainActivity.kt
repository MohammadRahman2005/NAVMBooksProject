@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navmbooks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.navmbooks.ui.theme.BookReadingApp
import com.example.navmbooks.ui.theme.BookViewModelFactory
import com.example.navmbooks.ui.theme.NAVMBooksTheme
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
                    windowSizeClass = windowSize.widthSizeClass,
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
                bookViewModel = bookViewModel,
                adaptiveNavType = adaptiveNavigationType
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
                    adaptiveNavType = adaptiveNavigationType
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
    bookViewModel: BookViewModel,
    adaptiveNavType: AdaptiveNavigationType
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
                padding = PaddingValues(dimensionResource(R.dimen.zero_padding)),
                adaptiveNavType = adaptiveNavType
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
    adaptiveNavType: AdaptiveNavigationType,
) {
    val books = bookViewModel.bookList
    val booksToDownload = bookViewModel.downloadedBookList
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HomeScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = NavRoutes.HomeScreen.route) {
            HomeScreen(navController = navController, modifier, padding)
        }
        composable(route = NavRoutes.LibraryScreen.route) {
            LibraryScreen(navController = navController, modifier, padding, books = books, booksToDownload = booksToDownload)
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
                    it,
                    adaptiveNavType = adaptiveNavType
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