package com.example.calculator

import android.util.Log

object Calculator {

    fun calculateExpression(currentExpression: MutableList<String>): Double {
        val expression = mutableListOf<String>().apply { addAll(currentExpression) }

        if (expression.lastOrNull() in Operator.values().map { it.symbol }) {
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
        val x = expression.subList(openingIndex, expression.size)

        val closingIndex = x.indexOfFirst { it == ")" } + openingIndex

        val subExpression = expression.subList(openingIndex + 1, closingIndex)
        val result = calculateExpression(subExpression)

        // remove both parentheses and the expression inside it
        (closingIndex.downTo(openingIndex)).forEach {
            expression.removeAt(it)
        }
        // put the result where the opening parenthesis was
        expression.add(openingIndex, result.toString())

        // number before or after a parentheses is multiplied
        if (openingIndex + 1 in expression.indices &&
            expression[openingIndex + 1].first().isDigit()
        ) {
            expression.add(openingIndex + 1, Operator.MULTIPLICATION.symbol)
        }
        if (openingIndex - 1 in expression.indices &&
            expression[openingIndex - 1].first().isDigit()
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