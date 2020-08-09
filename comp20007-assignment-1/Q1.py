def precedence(character):
    if character in ['+','-']:
        return 0
    elif character in ['*', '/']:
        return 1
    elif character in ['(', ')']:
        return 2
    else:
        print("Error calculating precedence")
        return False

def operate(operator, val1, val2):
    val1 = int(val1)
    val2 = int(val2)
    if operator == '+':
        return val1 + val2
    if operator == '-':
        return val1 - val2
    if operator == '*':
        return val1 * val2
    if operator == '/':
        return val1 / val2

def main():
    expression = input("Type your input here: ")
    numList = ['0','1','2','3','4','5','6','7','8','9']
    opList = ['+','-','/','*']

    opStack = []
    valStack = []

    for char in expression:
        # print(opStack, valStack, end = ", ")
        # If it is a number we can put it straight onto the value stack
        if char in numList:
            valStack.append(char)
            # print(f"{char} added")

        # Opening brackets go straight onto the operator stack
        elif char == '(':
            opStack.append(char)
            # print(f"{char} added")

        # Closing brackets mean we should evaluate the expression inside the
        # brackets and update the value stack
        elif char == ')':
            # print("Closing bracket...")
            if len(opStack) > 0 and len(valStack) > 1:
                topElement = opStack.pop()
                while topElement != '(':
                    if len(valStack) < 2:
                        return NOTWELLFORMED
                    operand1 = valStack.pop()
                    operand2 = valStack.pop()
                    newVal = operate(topElement, operand2, operand1)
                    # print(f"Operating {topElement} on {operand2} and {operand1}")
                    valStack.append(newVal)
                    topElement = opStack.pop()
            else:
                return "NOTWELLFORMED"
        # If an operator is found, check if it can be pushed straight onto the stack
        elif char in opList:
            if len(opStack) == 0:
                if len(valStack) == 0:
                    return "NOTWELLFORMED"
                opStack.append(char)
                # print(f"{char} added")
            else:
                topElement = opStack.pop()
                # print(f' Here: {topElement}', end = '-->')
                if topElement == '(':
                    opStack.append(topElement)
                    opStack.append(char)
                    # print(f"{char} added")
                else: 
                    while precedence(char) < precedence(topElement):
                        operand1 = valStack.pop()
                        operand2 = valStack.pop()
                        newVal = operate(topElement, operand2, operand1)    
                        valStack.append(newVal)
                        if len(opStack) == 0:
                            break
                        topElement = opStack.pop()
                    opStack.append(char)
        elif char != ' ':
            return "NOTWELLFORMED"

    # Once the whole expression has been read in, exhaust the operator Stack
    while len(opStack) > 0:
        topElement = opStack.pop()
        operand1 = valStack.pop()
        operand2 = valStack.pop()
        newVal = operate(topElement, operand2, operand1)    
        valStack.append(newVal)
    if len(valStack) != 1:
        return "NOTWELLFORMED"
    return valStack.pop()
print(main())




















# #https://www.geeksforgeeks.org/expression-evaluation/

# def precedence(character):
#     print(character)
#     if character in ['+','-']:
#         return 0
#     elif character in ['*', '/']:
#         return 1
#     else:
#         print("Error calculating precedence")
#         return False

# def insertOperator(stack, operator):
#     operand1 = stack.pop()
#     operand2 = stack.pop()
#     stack.append(operator)
#     stack.append(operand2)
#     stack.append(operand1)

# def main():
#     expression = input("Type your input here: ")
#     numList = ['0','1','2','3','4','5','6','7','8','9']
#     opList = ['+','-','/','*','(',')']

#     opStack = []
#     valStack = []

#     openBracket = []
#     # Converting the expression to prefix notation
#     for char in expression:
#         if char in numList:
#             valStack.append(char)
#         elif char in opList:
#             if char == '(':
#                 opStack.append(char)
#                 openBracket.append('1')
#             elif char == ')':
#                 if len(openBracket) > 0 and len(valStack) > 1:
#                     topElement = opStack.pop()
#                     while topElement != '(':
#                         insertOperator(valStack, topElement)
#                         topElement = opStack.pop()
#                     openBracket.pop()
#                 else:
#                     print("NOTWELLFORMED")
#                     return False
#             elif len(valStack) > 0:
#                 if len(opStack) > 0:
#                     topElement = opStack.pop()
#                     if topElement != '(' and topElement != ')':
#                         while precedence(char) < precedence(topElement) and len(opStack) > 0: 
#                             insertOperator(valStack, topElement)
#                             topElement = opStack.pop()
#                     opStack.append(topElement)
#                 opStack.append(char)
#             else:
#                 print("NOTWELLFORMED")
#                 return False
#         elif char == ' ':
#             continue
#         else:
#             print("NOTWELLFORMED")
#             return 
    
#     print(valStack)
#     print(opStack)


# main()
# exit