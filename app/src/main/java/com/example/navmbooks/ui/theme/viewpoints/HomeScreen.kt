package com.example.navmbooks.ui.theme.viewpoints

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.navmbooks.R


/**
 * this screen is the home screen which you see on load
 */
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // Background image
        Image(
            painter = painterResource(R.drawable.app_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App logo
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_padding)))
            Image(
                painter = painterResource(R.drawable.navm),
                contentDescription = stringResource(R.string.logo_desc),
                modifier = Modifier.height(120.dp)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_padding)))

            // Welcome message
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))

            Text(
                text = stringResource(R.string.app_description),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small_padding)),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

            // Team members
            Text(
                text = stringResource(R.string.team_members),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.team_names),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small_padding))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_padding)))

            // Get Started button
            Button(
                onClick = { navController.navigate("library_screen") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.testTag("StartedButton")
            ) {
                Text(
                    text = stringResource(R.string.get_started),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
