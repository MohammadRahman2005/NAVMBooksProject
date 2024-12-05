package com.example.navmbooks

import android.content.Context
import android.os.Environment
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class DownloadTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testDownloadAndUnZip() {
        // Navigate to Library
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Library").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag("LibraryText"), timeoutMillis = 5000)
        composeTestRule.onNodeWithTag("LibraryText").assertIsDisplayed()
        composeTestRule.waitForIdle()

        // Trigger the download
        composeTestRule.onNodeWithText("Download Viennese Medley").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasContentDescription("Viennese Medley"), timeoutMillis = 10000)

        // Check if the directory contains the expected files
        val context = ApplicationProvider.getApplicationContext<Context>()
        val directoryName = "DownloadedFiles"
        val downloadPath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), directoryName)

        // Check for the presence of the zipped file
        val zippedFile = File(downloadPath, "pg74757-h.zip")
        assertTrue("The zipped file 'pg74757-h.zip' should exist inside ${downloadPath.path}", zippedFile.exists() && zippedFile.isFile)

        // Check for the presence of the unzipped directory
        val unzippedDirectory = File(downloadPath, "pg74757-h")
        assertTrue("The unzipped directory 'pg74757-h' should exist inside ${downloadPath.path}", unzippedDirectory.exists() && unzippedDirectory.isDirectory)

        // Check if the unzipped directory contains files
        val unzippedFiles = unzippedDirectory.listFiles()
        assertTrue("The unzipped directory 'pg74757-h' should contain files", unzippedFiles != null && unzippedFiles.isNotEmpty())
    }
}
