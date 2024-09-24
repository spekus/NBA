package template.feature.nba.teams.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import template.feature.nba.teams.SortType
import template.feature.nba.teams.SortType.City
import template.feature.nba.teams.SortType.Conference
import template.feature.nba.teams.SortType.Name

@Composable
fun TeamsDialog(
    onDismissRequest: () -> Unit,
    onSelect: (SortType) -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(24.dp),
                    text = "Filter by:"
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {

                    TextButton(
                        onClick = { onSelect.invoke(Name) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Name")
                    }
                    TextButton(
                        onClick = { onSelect.invoke(Conference) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Conference")
                    }
                    TextButton(
                        onClick = { onSelect.invoke(City) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("City")
                    }
                }
            }
        }
    }
}
