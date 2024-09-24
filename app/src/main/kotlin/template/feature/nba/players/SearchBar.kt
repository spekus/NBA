package template.feature.nba.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

val searchText = mutableStateOf("")

@Composable
fun SearchSection(
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText.value,
        onValueChange = {
            searchText.value = it
        },
        placeholder = {
            Text(text = "Search here...", color = Color(0xFFCCCCCC))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
            .background(Color(0xFFF4F4F4))
            .onKeyEvent { event ->
                return@onKeyEvent if (event.key.keyCode == Key.Enter.keyCode) {
                    onSearch(searchText.value)
                    true
                } else {
                    false
                }
            },
        shape = CircleShape,
        singleLine = true,
        leadingIcon = {
            Icon(
                Icons.Rounded.Search,
                contentDescription = null
            )
        }
    )
}

