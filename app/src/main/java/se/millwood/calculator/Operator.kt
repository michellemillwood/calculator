package se.millwood.calculator

interface Token


enum class Parentheses : Token { OPENING, CLOSING }

enum class Operator : Token { MULTIPLICATION, DIVISION, ADDITION, SUBTRACTION }

class Constant(val number: Double) : Token
