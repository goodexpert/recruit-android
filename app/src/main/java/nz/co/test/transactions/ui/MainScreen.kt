package nz.co.test.transactions.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import nz.co.test.transactions.R
import nz.co.test.transactions.extensions.format
import nz.co.test.transactions.extensions.toCurrency
import nz.co.test.transactions.services.Transaction
import nz.co.test.transactions.ui.component.ActionBar
import nz.co.test.transactions.ui.component.ProgressIndicator
import nz.co.test.transactions.ui.theme.AppTheme
import nz.co.test.transactions.ui.theme.Credit
import nz.co.test.transactions.ui.theme.Debit
import nz.co.test.transactions.viewmodels.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.fromRoute(backstackEntry.value?.destination?.route)

    LaunchedEffect(Unit) {
        viewModel.fetchTransactions()
    }

    AppTheme {
        Scaffold(
            topBar = {
                ActionBar(
                    onBackClick = { coroutineScope.launch { navController.popBackStack() } },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )

            if (viewModel.uiState.loading) {
                ProgressIndicator()
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val transactions = viewModel.uiState.data

    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier
    ) {
        composable(AppScreen.Home.name) {
            TransactionListScreen(
                onItemClick = { id ->
                    navController.navigate("${AppScreen.Transaction.name}/${id}")
                },
                transactions = transactions
            )
        }
        composable(
            route = "${AppScreen.Transaction.name}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            val transaction = transactions.first { it.id == id }

            TransactionDetailsScreen(
                transaction = transaction
            )
        }
    }
}

@Composable
fun TransactionListScreen(
    onItemClick: (Int) -> Unit,
    transactions: List<Transaction>
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag("transactionList"),
            state = listState,
            contentPadding = PaddingValues(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions) { transaction ->
                Card(
                    modifier = Modifier.clickable { onItemClick(transaction.id) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = transaction.summary,
                            style = MaterialTheme.typography.subtitle1
                        )

                        Text(
                            text = transaction.transactionDate.format(stringResource(id = R.string.transaction_date_pattern)),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }

        if (listState.firstVisibleItemIndex > 0) {
            OutlinedButton(
                onClick = { coroutineScope.launch { listState.animateScrollToItem(0) } },
                modifier = Modifier.padding(16.dp).size(80.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colors.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.top),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun TransactionDetailsScreen(
    transaction: Transaction,
    paddingValues: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.transaction_date),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                style = MaterialTheme.typography.subtitle1
            )

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = transaction.transactionDate.format(stringResource(id = R.string.transaction_date_pattern)),
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.summary),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                style = MaterialTheme.typography.subtitle1
            )

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = transaction.summary,
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.debit_amount),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                style = MaterialTheme.typography.subtitle1
            )

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = transaction.debit.toCurrency(),
                        color = Color.Debit,
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.credit_amount),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                style = MaterialTheme.typography.subtitle1
            )

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = transaction.credit.toCurrency(),
                        color = Color.Credit,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}