package se.millwood.calculator

import android.util.Log

object Calculator {

    fun calculateExpression(currentExpression: MutableList<String>): Double {
        return calculateExpression(tokenize(currentExpression))
    }

    private fun tokenize(currentExpression: MutableList<String>): MutableList<Token> =
        currentExpression.map {
            when (it) {
                "(" -> Parentheses.OPENING
                ")" -> Parentheses.CLOSING
                "x" -> Operator.MULTIPLICATION
                "/" -> Operator.DIVISION
                "+" -> Operator.ADDITION
                "-" -> Operator.SUBTRACTION
                else -> Constant(it.toDouble())
            }
        }.toMutableList()

    @JvmName("calculateExpression1")
    private fun calculateExpression(currentExpression: MutableList<Token>): Double {
        val expression = mutableListOf<Token>().apply { addAll(currentExpression) }

        // ignore any operator at end of expression
        if (expression.lastOrNull() is Operator) {
            expression.removeLast()
        }
        
        while (expression.contains(Parentheses.OPENING) || expression.contains(Parentheses.CLOSING)) {
            calculateSubExpression(expression)
        }

        while (expression.contains(Operator.MULTIPLICATION)) {
            calculateAndReplaceWithResult(expression, Operator.MULTIPLICATION)
        }
        while (expression.contains(Operator.DIVISION)) {
            calculateAndReplaceWithResult(expression, Operator.DIVISION)
        }
        while (expression.contains(Operator.ADDITION)) {
            calculateAndReplaceWithResult(expression, Operator.ADDITION)
        }
        while (expression.contains(Operator.SUBTRACTION)) {
            calculateAndReplaceWithResult(expression, Operator.SUBTRACTION)
        }
        if (expression.size > 1 && expression.first() !is Constant) {
            throw ArithmeticException("invalid calculation")
        }
        val constant = expression.first() as Constant
        //Log.d("calculator", constant.number.toString())
        return constant.number
    }

    private fun calculateSubExpression(expression: MutableList<Token>) {
        // find indices of innermost parentheses
        val openingIndex = expression.indexOfLast { it == Parentheses.OPENING }
        val expressionAfterOpeningParenthesis = expression.subList(openingIndex, expression.size)
        val closingIndex = expressionAfterOpeningParenthesis.indexOfFirst { it == Parentheses.CLOSING } + openingIndex

        val subExpression = expression.subList(openingIndex + 1, closingIndex)

        // calculate the expression inside the parentheses
        val result = calculateExpression(subExpression)

        // remove both parentheses and the expression inside it
        (closingIndex.downTo(openingIndex)).forEach {
            expression.removeAt(it)
        }

        // put the result where the opening parenthesis was
        expression.add(openingIndex, Constant(result))

        // any number before or after a parentheses will be multiplied
        if (openingIndex + 1 in expression.indices &&
            expression[openingIndex + 1] is Constant
        ) {
            expression.add(openingIndex + 1, Operator.MULTIPLICATION)
        }
        if (openingIndex - 1 in expression.indices &&
            expression[openingIndex - 1] is Constant
        ) {
            expression.add(openingIndex, Operator.MULTIPLICATION)
        }
    }

    private fun calculateAndReplaceWithResult(
        expression: MutableList<Token>,
        operator: Operator
    ) {
        val operatorIndex = expression.indexOf(operator)
        val n1 = (expression[operatorIndex - 1] as Constant).number
        val n2 = (expression[operatorIndex + 1] as Constant).number

        val result = doArithmetic(n1, n2, operator)

        expression[operatorIndex] = Constant(result)
        expression.removeAt(operatorIndex + 1)
        expression.removeAt(operatorIndex - 1)
    }

    private fun doArithmetic(
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