package com.example.navmbooks

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import com.example.navmbooks.database.DatabaseViewModel
import com.example.navmbooks.ui.theme.BookReadingApp
import com.example.navmbooks.ui.theme.BookViewModelFactory
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import com.example.navmbooks.ui.theme.NavRoutes
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Locale

// HAVE DEVICE IN LANDSCAPE
// FIX FOR UI NOT DISPLAYING FULLY CAUSING TESTS TO FAIL.
class AppTestsExpanded {
    // Create the ComposeTestRule to test composables
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        // Launch the activity and set up content with the Compact window size class for all tests
        scenario = ActivityScenario.launch(MainActivity::class.java)
        setContentForTest()
    }

    private fun setContentForTest() {
        // Set content for the test, forcing a specific window size class
        scenario.onActivity { activity ->
            activity.setContent {
                val sharedPreferences = activity.getSharedPreferences("book_preferences", Context.MODE_PRIVATE)
                val dbViewModel = DatabaseViewModel(activity.application)
                NAVMBooksTheme {
                    BookReadingApp(
                        locale = Locale.US,
                        windowSizeClass = WindowWidthSizeClass.Compact,
                        factory = BookViewModelFactory(activity.applicationContext, dbViewModel),
                        startDestination = NavRoutes.HomeScreen.route,
                        onResetLastAccessed = {resetLastAccessed(sharedPreferences)}
                    )
                }
            }
        }
    }

    private fun resetLastAccessed(sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            clear() // Clear all saved data
            apply()
        }
        scenario.onActivity { activity ->
            Toast.makeText(activity, "Last accessed chapter reset successfully", Toast.LENGTH_SHORT).show()
        }
    }

    // Verifies that the top bar elements (title, logo, back button) are rendered correctly.
    @Test
    fun testTopBarRenders() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("NAVM Books").assertIsDisplayed()
        composeTestRule.onNodeWithTag("TopLogo").assertIsDisplayed()
        composeTestRule.onNodeWithTag("backButton").assertIsDisplayed()
    }

    // Verifies that the bottom navigation bar renders with all its options (Home, Library, Search).
    @Test
    fun testBottomNavRender() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Library").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    // Tests if back navigation Functions
    @Test
    fun testBackNavigation() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("backButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("backButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
    }

    // Ensures the home screen is rendered with the welcome message displayed.
    @Test
    fun testRendersHomeScreen() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
    }

    // Ensures the Get Started button navigates to Library screen upon click.
    @Test
    fun testGetStartedButton() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
        composeTestRule.onNodeWithTag("StartedButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("LibraryText").assertIsDisplayed()
    }

    // Confirms the library screen is rendered and its content is displayed upon navigation.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRendersLibrary() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("LibraryText"), timeoutMillis = 5000)
        composeTestRule.onNodeWithTag("LibraryText").assertIsDisplayed()
    }

    // Tests the download functionality of the library screen.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLibraryDownloadFunctionality() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("LibraryText"), timeoutMillis = 5000)
        composeTestRule.onNodeWithText("Download Viennese Medley").performScrollTo()
        composeTestRule.onNodeWithText("Download Viennese Medley").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("Viennese Medley"), timeoutMillis = 10000)
        composeTestRule.onAllNodesWithContentDescription("Viennese Medley")[0].assertIsDisplayed()
    }

    // Validates that the search screen is rendered correctly upon navigation.
    @Test
    fun testRendersSearch() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("SearchButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SearchField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DropdownMenu").assertIsDisplayed()
    }

    // Tests the functionality of the search screen.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testSearchFunctionality() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("SearchButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SearchField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DropdownMenu").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DropdownMenu").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.waitUntilAtLeastOneExists(hasText("WINNIE-THE-POOH"))
        composeTestRule.onNodeWithText("WINNIE-THE-POOH").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("SearchField").performTextInput("the")
        composeTestRule.onNodeWithTag("SearchButton").performClick()
        composeTestRule.waitForIdle()
        val searchResults = composeTestRule.onAllNodesWithTag("SearchResult")
        searchResults[0].assertIsDisplayed()
        searchResults[0].performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Content").assertIsDisplayed()
    }

    // Checks if the content details are displayed correctly when navigating through the library.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRendersContent() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WINNIE-THE-POOH"), timeoutMillis = 5000)
        composeTestRule.onNodeWithContentDescription("WINNIE-THE-POOH").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Chapter 1").assertIsDisplayed()
    }

    // Ensures the reading screen displays the appropriate content when navigating from the library.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRendersReading() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WINNIE-THE-POOH"), timeoutMillis = 5000)
        composeTestRule.onNodeWithContentDescription("WINNIE-THE-POOH").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Chapter 1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingSwitch").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Content").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Previous").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Next").assertIsDisplayed()
    }

    // Ensures the reading mode toggle works as expected, switching states and affecting the UI accordingly.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testReadingModeButton() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WINNIE-THE-POOH"), timeoutMillis = 5000)
        composeTestRule.onNodeWithContentDescription("WINNIE-THE-POOH").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Chapter 1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingSwitch").assertIsOff()
        composeTestRule.onNodeWithTag("ReadingSwitch").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingSwitch").assertIsOn()
        composeTestRule.onNodeWithText("Home").assertDoesNotExist()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingSwitch").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingSwitch").assertIsOff()
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
    }

    // Tests the Next and Previous buttons in the reading screen.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testChapterButtons() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WINNIE-THE-POOH"), timeoutMillis = 5000)
        composeTestRule.onNodeWithContentDescription("WINNIE-THE-POOH").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Chapter 1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingSwitch").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Content").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Previous").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Next").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Next").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Content").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Previous").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Content").assertIsDisplayed()
    }
}