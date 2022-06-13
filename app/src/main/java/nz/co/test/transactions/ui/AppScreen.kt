package nz.co.test.transactions.ui

enum class AppScreen {
    Home,
    Transaction;

    companion object {
        fun fromRoute(route: String?): AppScreen =
            when (route?.substringBefore("/")) {
                Transaction.name -> Transaction
                Home.name, null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}