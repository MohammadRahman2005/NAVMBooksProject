package com.example.navmbooks

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import org.junit.Rule
import org.junit.Test

class NAVMAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navMAppBar_displaysTitleAndLogo() {
        composeTestRule.setContent {
            NAVMBooksTheme {
                NAVMAppBar(
                    navigateUp = { /* No action needed for test */ }
                )
            }
        }

        // Verify the title is displayed
        composeTestRule.onNodeWithText("NAVM Books")
            .assertIsDisplayed()

        // Verify the logo is displayed
        composeTestRule.onNodeWithContentDescription("App Logo")
            .assertIsDisplayed()
    }

    @Test
    fun navMAppBar_navigatesOnClick() {
        var navigateCalled = false
        composeTestRule.setContent {
            NAVMBooksTheme {
                NAVMAppBar(
                    navigateUp = { navigateCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back")
            .assertIsDisplayed()
            .performClick()

        assert(navigateCalled) { "The navigateUp function was not called." }
    }
}
