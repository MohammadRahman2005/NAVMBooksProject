package com.example.navmbooks

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class AppTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Verifies that the top bar elements (title, logo, back button) are rendered correctly.
    @Test
    fun testTopBarRenders() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("NAVM Books").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("App Logo").assertIsDisplayed()
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
        composeTestRule.onNodeWithTag("LibraryText").assertIsDisplayed()
        composeTestRule.waitForIdle()
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
        composeTestRule.onNodeWithText("WINNIE-THE-POOH").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("SearchField").performTextInput("The")
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