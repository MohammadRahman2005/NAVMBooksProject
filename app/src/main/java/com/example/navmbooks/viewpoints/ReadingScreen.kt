import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.navmbooks.BookViewModel

@Composable
fun ReadingScreen(
    navController: NavHostController,
    bookViewModel: BookViewModel
) {

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Reading Mode", modifier = Modifier.padding(end = 8.dp))
            Switch(
                checked = bookViewModel.isReadingMode.value,
                onCheckedChange = { bookViewModel.toggleReadingMode(it) }
            )
        }
        Text("Reading Content Here")
    }
}
