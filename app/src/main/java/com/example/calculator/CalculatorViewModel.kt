package com.example.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.math.exp

class CalculatorViewModel : ViewModel() {

    private val currentExpression = mutableListOf<String>()

    fun calculate() = Calculator.calculateExpression(currentExpression)

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

    private fun lastStringIsOperator(): Boolean {
        val operators = Operator.values().map { it.symbol }
        return currentExpression.lastOrNull() in operators
    }

    private fun lastStringIsParentheses(): Boolean {
        return currentExpression.last() == "(" ||
               currentExpression.last() == ")"
    }

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
}

