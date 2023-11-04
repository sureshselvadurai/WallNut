package com.example.wallnut.model

/**
 * Data class representing a report.
 *
 * @property budget The budget for the report.
 * @property foodSpend The amount spent on food.
 * @property utilitiesSpend The amount spent on utilities.
 * @property loanSpend The amount spent on loans.
 * @property entertainmentSpend The amount spent on entertainment.
 * @property previousMonthSpend The amount spent in the previous month.
 * @property totalSpends The total amount spent.
 * @property totalIncome The total income.
 * @property netBalance The net balance.
 * @property billReminder The bill reminders.
 */
data class Report(
    private var budget: String,
) {
    private var foodSpend: String = "0"
    private var utilitiesSpend: String = "0"
    private var loanSpend: String = "0"
    private var entertainmentSpend: String = "0"
    private var previousMonthSpend: String = ""
    private var totalSpends: String = "0"
    private var totalIncome: String = "0"
    private var netBalance: String = "0"
    private var billReminder: String = ""

    override fun toString(): String {
        return "{totalSpends='$totalSpends', previousMonthSpend='$previousMonthSpend', entertainmentSpend='$entertainmentSpend', " +
                "loanSpend='$loanSpend', foodSpend='$foodSpend', utilitiesSpend='$utilitiesSpend', billReminder='$billReminder', " +
                "totalIncome='$totalIncome', budget='$budget', netBalance='$netBalance'}"
    }

    /**
     * Builder class for creating Report objects.
     *
     * @param budget The budget for the report.
     */
    data class Builder(val budget: String) {
        var foodSpend: String = "0"
        var utilitiesSpend: String = "0"
        var loanSpend: String = "0"
        var entertainmentSpend: String = "0"
        var previousMonthSpend: String = ""
        var totalSpends: String = "0"
        var totalIncome: String = "0"
        var netBalance: String = "0"
        var billReminder: String = ""

        /**
         * Set the amount spent on food.
         *
         * @param value The amount spent on food.
         */
        fun foodSpend(value: String) = apply { foodSpend = value }

        /**
         * Set the amount spent on utilities.
         *
         * @param value The amount spent on utilities.
         */
        fun utilitiesSpend(value: String) = apply { utilitiesSpend = value }

        /**
         * Set the amount spent on loans.
         *
         * @param value The amount spent on loans.
         */
        fun loanSpend(value: String) = apply { loanSpend = value }

        /**
         * Set the amount spent on entertainment.
         *
         * @param value The amount spent on entertainment.
         */
        fun entertainmentSpend(value: String) = apply { entertainmentSpend = value }

        /**
         * Set the amount spent in the previous month.
         *
         * @param value The amount spent in the previous month.
         */
        fun previousMonthSpend(value: String) = apply { previousMonthSpend = value }

        /**
         * Set the total amount spent.
         *
         * @param value The total amount spent.
         */
        fun totalSpends(value: String) = apply { totalSpends = value }

        /**
         * Set the total income.
         *
         * @param value The total income.
         */
        fun totalIncome(value: String) = apply { totalIncome = value }

        /**
         * Set the net balance.
         *
         * @param value The net balance.
         */
        fun netBalance(value: String) = apply { netBalance = value }

        /**
         * Set the bill reminders.
         *
         * @param value The bill reminders.
         */
        fun billReminder(value: String) = apply { billReminder = value }

        /**
         * Build a Report object based on the builder's properties.
         *
         * @return The constructed Report object.
         */
        fun build() = Report(this)
    }

    /**
     * Constructor to create a Report object from a Builder.
     *
     * @param builder The builder with property values.
     */
    constructor(builder: Builder) : this(builder.budget) {
        foodSpend = builder.foodSpend
        utilitiesSpend = builder.utilitiesSpend
        loanSpend = builder.loanSpend
        entertainmentSpend = builder.entertainmentSpend
        previousMonthSpend = builder.previousMonthSpend
        totalSpends = builder.totalSpends
        totalIncome = builder.totalIncome
        netBalance = builder.netBalance
        billReminder = builder.billReminder
    }

    /**
     * Get the net balance.
     *
     * @return The net balance as a string.
     */
    fun getNetBalance(): String {
        return netBalance
    }

    /**
     * Get the total amount spent.
     *
     * @return The total amount spent as a string.
     */
    fun getTotalSpends(): String {
        return totalSpends
    }

    /**
     * Get the budget.
     *
     * @return The budget as a string.
     */
    fun getBudget():  String {
        return budget
    }

    /**
     * Get the amount spent in the previous month.
     *
     * @return The amount spent in the previous month as a string.
     */
    fun getPreviousMonthSpend():  String {
        return previousMonthSpend
    }

    /**
     * Get the total income.
     *
     * @return The total income as a string.
     */
    fun getTotalIncome():  String {
        return totalIncome
    }

    /**
     * Get the amount spent on loans.
     *
     * @return The amount spent on loans as a string.
     */
    fun getLoanSpend():  String {
        return loanSpend
    }

    /**
     * Get the amount spent on utilities.
     *
     * @return The amount spent on utilities as a string.
     */
    fun getUtilitiesSpend():  String {
        return utilitiesSpend
    }

    /**
     * Get the amount spent on food.
     *
     * @return The amount spent on food as a string.
     */
    fun getFoodSpend():  String {
        return foodSpend
    }

    /**
     * Get the amount spent on entertainment.
     *
     * @return The amount spent on entertainment as a string.
     */
    fun getEntertainmentSpend(): String {
        return entertainmentSpend
    }

    /**
     * Get the bill reminders.
     *
     * @return The bill reminders as a string.
     */
    fun getBillReminder(): String {
        return billReminder
    }

    /**
     * Set the budget amount.
     *
     * @param budgetAmount The new budget amount.
     */
    fun setBudget(budgetAmount: String) {
        this.budget = budgetAmount
    }
}
