package com.example.calculator

import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val input = mutableListOf<Char>()

    fun receiveDigitInput(digit: Char) {
        input.add(digit)
    }

    fun receiveOperatorInput(operator: Char) {
        if (input.isNotEmpty() && input.last().isDigit()) {
            input.add(operator)
        }
    }

    fun receiveDecimalSeparatorInput() {
        if (!lastNumberHasDecimal()) {
            if (input.isEmpty() || !input.last().isDigit()) {
                input.add('0')
            }
            input.add('.')
        }
    }

    fun getCurrentCalculation() = input.joinToString("")

    fun allClear() = input.clear()

    fun deleteLast() {
        if (input.isNotEmpty()) {
            input.removeLast()
        }
    }


    private fun lastNumberHasDecimal(): Boolean {
        val indexOfOperator = input.indexOfLast { char ->
            Operator.values().any { it.symbol == char }
        }
        return input.subList(indexOfOperator + 1, input.size).contains('.')
    }

    private fun splitInputNumbers() {
       val operators = Operator.values().map { it.symbol }
    }

    private fun orderNumbers() {

    }
}
