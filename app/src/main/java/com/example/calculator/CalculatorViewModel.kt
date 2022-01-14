package com.example.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.math.exp

class CalculatorViewModel : ViewModel() {

    private val currentExpression = mutableListOf<String>()

    private val operators = Operator.values().map { it.symbol }

    fun parseDigitInput(digit: String) {
        if (currentExpression.isEmpty() ||
            lastStringIsOperator() ||
            lastStringIsParentheses()
        ) {
            currentExpression.add(digit)
        } else {
            appendToLastNumber(digit)
        }
    }

    fun parseOperatorInput(operator: String) {
        if (currentExpression.isNotEmpty() && !lastStringIsOperator()) {
            if (lastStringEndsWithDot()) {
                appendToLastNumber("0")
            }
            if (currentExpression.last() == "(" &&
                operator != Operator.SUBTRACTION.symbol
            ) {
                return
            }
            currentExpression.add(operator)
        }
    }

    fun parseDecimalSeparatorInput() {
        if (currentExpression.isEmpty()) {
            currentExpression.add("0.")
        }
        if (!currentExpression.last().contains(".")) {
            if (!currentExpression.last().last().isDigit()) {
                currentExpression.add("0.")
            } else {
                appendToLastNumber(".")
            }
        }
    }

    fun parseParenthesesInput(parentheses: String) {
        if (currentExpression.isNotEmpty() && lastStringEndsWithDot()) {
            appendToLastNumber("0")
        }
        if (parentheses == ")") {
            if (!currentExpression.contains("(") ||
                lastStringIsOperator() ||
                currentExpression.last() == "("
            ) return
        }
        currentExpression.add(parentheses)
    }

    private fun lastStringEndsWithDot() = currentExpression.last().last() == '.'

    private fun lastStringIsOperator() = currentExpression.lastOrNull() in operators

    private fun lastStringIsParentheses() =
        currentExpression.last() == "(" || currentExpression.last() == ")"

    private fun appendToLastNumber(digitOrDot: String) {
        currentExpression[currentExpression.lastIndex] = currentExpression.last().plus(digitOrDot)
    }

    fun getCurrentExpression(): String {
        Log.d("viewModel", currentExpression.toString())
        return currentExpression.joinToString(" ")
    }

    fun allClear() = currentExpression.clear()

    fun deleteLast() {
        if (currentExpression.isNotEmpty()) {
            if (currentExpression.last().length == 1) {
                currentExpression.removeLast()
            } else {
                currentExpression[currentExpression.lastIndex] =
                    currentExpression.last().dropLast(1)
            }
        }
    }

    private fun hasEvenNumberOfParentheses(): Boolean {
        val opening = currentExpression.filter { it == "(" }
        val closing = currentExpression.filter { it == ")" }
        return opening.size == closing.size
    }

    fun expressionIsValid() = hasEvenNumberOfParentheses() && currentExpression.isNotEmpty()

    fun calculateExpression(exp: MutableList<String> = currentExpression): Double {
        val expression = mutableListOf<String>().apply { addAll(exp) }

        if (lastStringIsOperator()) {
            expression.removeLast()
        }

        while (expression.contains("(") || expression.contains(")")) {
            calculateSubExpression(expression)
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

    private fun calculateSubExpression(expression: MutableList<String>) {
        val openingIndex = expression.indexOfLast { it == "(" }
        val closingIndex = expression.indexOfFirst { it == ")" }

        val subExpression = expression.subList(openingIndex + 1, closingIndex)
        val result = calculateExpression(subExpression)

        // remove both parentheses and the expression inside it
        (closingIndex.downTo(openingIndex)).forEach {
            expression.removeAt(it)
        }
        // put the result where the opening parenthesis was
        expression.add(openingIndex, result.toString())

        if (openingIndex + 1 in expression.indices &&
            expression[openingIndex + 1] !in operators
        ) {
            expression.add(openingIndex + 1, Operator.MULTIPLICATION.symbol)
        }

        if (openingIndex - 1 in expression.indices &&
            expression[openingIndex - 1] !in operators
        ) {
            expression.add(openingIndex, Operator.MULTIPLICATION.symbol)
        }
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
