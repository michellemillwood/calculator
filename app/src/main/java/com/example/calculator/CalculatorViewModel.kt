package com.example.calculator

import android.util.Log
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val currentExpression = mutableListOf<String>()

    fun receiveDigitInput(digit: String) {
        if (currentExpression.isEmpty() || lastCharIsOperator()) {
            currentExpression.add(digit)
        }
        else {
            currentExpression[currentExpression.lastIndex] = currentExpression.last().plus(digit)
        }
    }

    fun receiveOperatorInput(operator: String) {
        if (currentExpression.isNotEmpty() && !lastCharIsOperator()) {
            currentExpression.add(operator)
        }
    }

    private fun lastCharIsOperator() : Boolean {
        val operators = Operator.values().map { it.symbol }
        return currentExpression.last() in operators
    }

    fun receiveDecimalSeparatorInput() {
        if (currentExpression.isEmpty()) {
            currentExpression.add("0.")
        }
        if (!currentExpression.last().contains(".")) {
            if (!currentExpression.last().last().isDigit()) {
                currentExpression.add("0.")
            }
            else {
                currentExpression[currentExpression.lastIndex] = currentExpression.last().plus(".")
            }
        }
    }

    fun getCurrentExpression(): String {
        Log.d("viewModel", currentExpression.toString())
        return currentExpression.joinToString("")
    }

    fun allClear() = currentExpression.clear()

    fun deleteLast() {
        if (currentExpression.isNotEmpty()) {
            if (currentExpression.last().length == 1) {
                currentExpression.removeLast()
            }
            else {
                currentExpression[currentExpression.lastIndex] = currentExpression.last().dropLast(1)
            }
        }
    }

    fun calculateExpression(): Double {
        val expression = mutableListOf<String>().apply { addAll(currentExpression) }
        if (lastCharIsOperator()) {
            expression.removeLast()
        }
        while (expression.contains(Operator.MULTIPLICATION.symbol)) {
            calculateInOrder(expression, Operator.MULTIPLICATION)
        }
        while (expression.contains(Operator.DIVISION.symbol)) {
            calculateInOrder(expression, Operator.DIVISION)
        }
        while (expression.contains(Operator.ADDITION.symbol)) {
            calculateInOrder(expression, Operator.ADDITION)
        }
        while (expression.contains(Operator.SUBTRACTION.symbol)) {
            calculateInOrder(expression, Operator.SUBTRACTION)
        }
        Log.d("viewModel", expression.toString())

        if (expression.size > 1) {
            throw ArithmeticException("invalid calculation")
        }
        return expression.first().toDouble()
    }

    private fun calculateInOrder(
        expression: MutableList<String>,
        operator: Operator
    ) {
        val operatorIndex = expression.indexOf(operator.symbol)
        val n1 = expression[operatorIndex - 1].toDouble()
        val n2 = expression[operatorIndex + 1].toDouble()

        val result = calculate(n1, n2, operator)

        expression[operatorIndex] = result.toString()
        expression.removeAt(operatorIndex + 1)
        expression.removeAt(operatorIndex - 1)
    }

    private fun calculate(
        n1: Double,
        n2: Double,
        operator: Operator
    ): Double {
        return when (operator) {
            Operator.MULTIPLICATION -> n1 * n2
            Operator.DIVISION -> n1 / n2
            Operator.ADDITION -> n1 + n2
            Operator.SUBTRACTION -> n1 - n2
        }
    }
}
