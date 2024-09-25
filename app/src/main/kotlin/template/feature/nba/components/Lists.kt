package template.feature.nba.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import template.feature.nba.InfiniteListHandler

@Composable
fun ListWithHeader(
    header: List<String>,
    rows: List<RowItem>,
    modifier: Modifier = Modifier,
    onLoadMore: (() -> Unit)? = null,
    onItemClick: ((Int) -> Unit)? = null,
) {
    val listState = rememberLazyListState()

    Header(labels = header)
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState
    ) {
        items(count = rows.size) { index ->
            rows[index].let {
                RowItem(
                    columns = it.titles,
                    onItemClick = { onItemClick?.invoke(it.onItemClickId) }
                )
            }
        }
    }

    onLoadMore?.let {
        InfiniteListHandler(listState = listState) {
            it()
        }
    }
}

data class RowItem(
    val titles: List<String>,
    val onItemClickId: Int,
)

@Composable
fun Header(labels: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        labels.forEach { label ->
            Text(
                label,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Start
            )
        }
    }
}
