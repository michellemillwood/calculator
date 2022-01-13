package com.example.calculator

import android.util.Log
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val input = mutableListOf<String>()

    fun receiveDigitInput(digit: String) {
        if (input.isEmpty() || lastCharIsOperator()) {
            input.add(digit)
        }
        else {
            input[input.lastIndex] = input.last().plus(digit)
        }
    }

    fun receiveOperatorInput(operator: String) {
        if (input.isNotEmpty() && !lastCharIsOperator()) {
            input.add(operator)
        }
    }

    private fun lastCharIsOperator() : Boolean {
        val operators = Operator.values().map { it.symbol }
        return input.last() in operators
    }

    fun receiveDecimalSeparatorInput() {
        if (input.isEmpty()) {
            input.add("0.")
        }
        if (!input.last().contains(".")) {
            if (!input.last().last().isDigit()) {
                input.add("0.")
            }
            else {
                input[input.lastIndex] = input.last().plus(".")
            }
        }
    }

    fun getCurrentExpression(): String {
        Log.d("viewModel", input.toString())
        return input.joinToString("")
    }

    fun allClear() = input.clear()

    fun deleteLast() {
        if (input.isNotEmpty()) {
            if (input.last().length == 1) {
                input.removeLast()
            }
            else {
                input[input.lastIndex] = input.last().dropLast(1)
            }
        }
    }

    fun calculateExpression(): String {
        val expression = mutableListOf<String>().apply { addAll(input) }

        while (expression.contains(Operator.MULTIPLICATION.symbol)) {
            val operatorIndex = expression.indexOf(Operator.MULTIPLICATION.symbol)
            val n1 = expression[operatorIndex - 1].toDouble()
            val n2 = expression[operatorIndex + 1].toDouble()

            val product = calculate(n1, n2, Operator.MULTIPLICATION)

            expression[operatorIndex] = product.toString()
            expression.removeAt(operatorIndex + 1)
            expression.removeAt(operatorIndex - 1)
        }
        return expression.joinToString()
    }

    private fun calculate(
        n1: Double,
        n2: Double,
        operator: Operator
    ): Double {
        return when (operator) {
            Operator.MULTIPLICATION -> n1 * n2
            Operator.SUBTRACTION -> n1 - n2
            Operator.DIVISION -> n1 / n2
            Operator.ADDITION -> n1 + n2
        }
    }
}
