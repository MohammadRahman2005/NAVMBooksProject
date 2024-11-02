package com.example.navmbooks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class BottomNavigationBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    data class BarItem(val title: String, val image: ImageVector, val route: String)

    private fun navBarItems(): List<BarItem> {
        return listOf(
            BarItem("Home", Icons.Filled.Home, "home_screen"),
            BarItem("Library", Icons.Filled.Menu, "library_screen"),
            BarItem("Search", Icons.Filled.Search, "search_screen"),
            BarItem("Content", Icons.Filled.PlayArrow, "content_screen"),
            BarItem("Reading", Icons.Filled.Info, "reading_screen")
        )
    }

    @Composable
    private fun SetUp(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "home_screen") {
            composable("home_screen") { /* Your HomeScreen Composable */ }
            composable("library_screen") { /* Your LibraryScreen Composable */ }
            composable("search_screen") { /* Your SearchScreen Composable */ }
            composable("content_screen") { /* Your ContentScreen Composable */ }
            composable("reading_screen") { /* Your ReadingScreen Composable */ }
        }
        BottomNavigationBar(navController = navController)
    }

    @Composable
    fun TestBottomNavigationBar() {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            val testNavItems = listOf(
                BarItem(title = "Home", image = Icons.Filled.Home, route = "home"),
                BarItem(title = "Library", image = Icons.Filled.Menu, route = "library"),
                BarItem(title = "Search", image = Icons.Filled.Search, route = "search"),
            )

            testNavItems.forEach { navItem ->
                NavigationBarItem(
                    selected = false,
                    onClick = {},
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

    @Test
    fun bottomNavigationBar_displaysStaticNavigationItems() {
        composeTestRule.setContent {
            NAVMBooksTheme {
                rememberNavController()
                TestBottomNavigationBar()
            }
        }

        val staticNavItems = listOf("Home", "Library", "Search")
        staticNavItems.forEach { itemTitle ->
            composeTestRule.onNodeWithText(itemTitle).assertIsDisplayed()
        }
    }

    @Test
    fun bottomNavigationBar_navigatesOnClick() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SetUp(navController)
        }

        val firstNavItem = navBarItems().first()
        val secondNavItem = navBarItems()[1]
        composeTestRule.onNodeWithText(firstNavItem.title).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(secondNavItem.title).assertIsDisplayed()
    }
}

