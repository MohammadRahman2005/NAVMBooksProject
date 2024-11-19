package com.example.navmbooks

import android.content.res.Configuration
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
import com.example.navmbooks.ui.theme.NAVMBooksTheme
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
                NAVMBooksTheme {
                    BookReadingApp(
                        locale = Locale.US,
                        windowSizeClass = WindowWidthSizeClass.Expanded,
                        factory = BookViewModelFactory(activity.applicationContext)
                    )
                }
            }
        }
    }

    @Test
    fun testTopBarRenders() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("NAVM Books").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("App Logo").assertIsDisplayed()
        composeTestRule.onNodeWithTag("backButton").assertIsDisplayed()
    }

    @Test
    fun testRendersHomeScreen() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
    }

    @Test
    fun testRendersLibrary() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("LibraryText").assertIsDisplayed()
    }

    @Test
    fun testRendersSearch() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("SearchText").assertIsDisplayed()
    }


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

    @Test
    fun testBottomNavRender() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Library").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun testBackNavigation() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("backButton").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Welcome to NAVMBooks").assertIsDisplayed()
    }

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