package com.example.navmbooks

import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import org.junit.Rule
import org.junit.Test
import java.util.Locale

class AppTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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
        composeTestRule.onNodeWithTag("homeText").assertIsDisplayed()
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

    @Test
    fun testRendersContent() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Book 1 table of contents").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ContentText").assertIsDisplayed()
    }

    @Test
    fun testRendersReading() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Read").performClick()
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
        composeTestRule.onNodeWithTag("homeText").assertIsDisplayed()
    }

    @Test
    fun testReadingModeButton() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Book 1 table of contents").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Book 1 Chapter 1 Reading").performClick()
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