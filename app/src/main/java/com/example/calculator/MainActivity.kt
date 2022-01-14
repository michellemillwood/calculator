package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.calculator.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: CalculatorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.input.text = viewModel.getCurrentExpression()
        setupButtons()
    }

    private fun setupButtons() {
        setupClearButton()
        setupBackSpaceButton()
        setupDigitButtons()
        setupEqualsButton()
        setupDecimalButton()
        setupOperatorButtons()
        setupParenthesesButton()
    }

    private fun setupDecimalButton() {
        with(binding) {
            dot.setOnClickListener {
                viewModel.parseDecimalSeparatorInput()
                showCurrentExpression()
            }
        }
    }

    private fun setupEqualsButton() {
        with(binding) {
            buttonEquals.setOnClickListener {
                if (viewModel.expressionIsValid()) {
                    result.text = NumberFormat.getInstance().format(viewModel.calculateExpression())
                }
                else {
                    result.text = getString(R.string.invalid_expression)
                }
            }
        }
    }

    private fun setupParenthesesButton() {
        with(binding) {
            val parenthesesButtons = listOf(
                openParentheses,
                closeParentheses
            )
            parenthesesButtons.forEach { parenthesesButton ->
                parenthesesButton.setOnClickListener {
                    viewModel.parseParenthesesInput(parenthesesButton.text.toString())
                    showCurrentExpression()
                }
            }
        }
    }

    private fun showCurrentExpression() {
        binding.input.text = viewModel.getCurrentExpression()
    }

    private fun setupDigitButtons() {
        with(binding) {
            val numberButtons = listOf(
                buttonZero,
                buttonOne,
                buttonTwo,
                buttonThree,
                buttonFour,
                buttonFive,
                buttonSix,
                buttonSeven,
                buttonEight,
                buttonNine,
            )
            numberButtons.forEach { button ->
                button.setOnClickListener {
                    viewModel.parseDigitInput(button.text.toString())
                    showCurrentExpression()
                }
            }
        }
    }

    private fun setupBackSpaceButton() {
        with(binding) {
            backspace.setOnClickListener {
                viewModel.deleteLast()
                showCurrentExpression()
            }
        }
    }


    private fun setupClearButton() {
        with(binding) {
            allClear.setOnClickListener {
                viewModel.allClear()
                input.text = viewModel.getCurrentExpression()
                result.text = viewModel.getCurrentExpression()
            }
        }
    }

    private fun setupOperatorButtons() {
        with(binding) {
            val operatorButtons = listOf(
                buttonDivision,
                buttonMultiply,
                buttonSubtraction,
                buttonAddition,
            )
            operatorButtons.forEach { button ->
                button.setOnClickListener {
                    viewModel.parseOperatorInput(button.text.toString())
                    showCurrentExpression()
                }
            }
        }
    }
}