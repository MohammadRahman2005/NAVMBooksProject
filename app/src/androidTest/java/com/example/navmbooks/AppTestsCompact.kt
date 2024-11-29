package com.example.navmbooks

import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.navmbooks.ui.theme.BookViewModelFactory
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class AppTestsCompact {

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
                NAVMBooksTheme {
                    BookReadingApp(
                        locale = Locale.US,
                        windowSizeClass = WindowWidthSizeClass.Compact,
                        factory = BookViewModelFactory(activity.applicationContext)
                    )
                }
            }
        }
    }

    // Verifies that the top bar elements (title, logo, back button) are rendered correctly.
    @Test
    fun testTopBarRenders() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("NAVM Books").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("App Logo").assertIsDisplayed()
        composeTestRule.onNodeWithTag("backButton").assertIsDisplayed()
    }

    // Ensures the home screen is rendered with the welcome message displayed.
    @Test
    fun testRendersHomeScreen() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
    }

    // Confirms the library screen is rendered and its content is displayed upon navigation.
    @Test
    fun testRendersLibrary() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("LibraryText").assertIsDisplayed()
    }

    // Validates that the search screen is rendered correctly upon navigation.
    @Test
    fun testRendersSearch() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("SearchText").assertIsDisplayed()
    }

    // Checks if the content details are displayed correctly when navigating through the library.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRendersContent() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WOOD-BLOCK PRINTING"), timeoutMillis = 10000)
        composeTestRule.onNodeWithContentDescription("WOOD-BLOCK PRINTING").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Chapter 1").assertIsDisplayed()
    }

    // Ensures the reading screen displays the appropriate content when navigating from the library.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRendersReading() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WOOD-BLOCK PRINTING"), timeoutMillis = 10000)
        composeTestRule.onNodeWithContentDescription("WOOD-BLOCK PRINTING").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Chapter 1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ReadingText").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ContentText").assertIsDisplayed()
    }

    // Verifies that the bottom navigation bar renders with all its options (Home, Library, Search).
    @Test
    fun testBottomNavRender() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Library").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    // Tests if back navigation from the library screen returns to the home screen.
    @Test
    fun testBackNavigation() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("backButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
    }

    // Ensures the reading mode toggle works as expected, switching states and affecting the UI accordingly.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testReadingModeButton() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("WOOD-BLOCK PRINTING"), timeoutMillis = 10000)
        composeTestRule.onNodeWithContentDescription("WOOD-BLOCK PRINTING").performClick()
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

}
