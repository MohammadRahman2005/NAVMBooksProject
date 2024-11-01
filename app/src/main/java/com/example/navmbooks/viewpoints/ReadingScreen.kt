import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun ReadingScreen(
    navController: NavHostController,
    onReadingModeChanged: (Boolean) -> Unit
) {
    var isReadingMode by rememberSaveable { mutableStateOf(false) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Reading Mode", modifier = Modifier.padding(end = 8.dp))
            Switch(
                checked = isReadingMode,
                onCheckedChange = {
                    isReadingMode = it
                    onReadingModeChanged(isReadingMode)
                }
            )
        }
        Text("Reading Content Here")
    }
}
