package nz.co.test.transactions.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nz.co.test.transactions.R
import nz.co.test.transactions.ui.AppScreen

@Composable
fun ActionBar(
    onBackClick: () -> Unit,
    currentScreen: AppScreen
) {
    TopAppBar(
        title = {
            Text(
                text = currentScreen.name,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
        },
        navigationIcon = {
            if (currentScreen != AppScreen.Home) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to home",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.width(54.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}