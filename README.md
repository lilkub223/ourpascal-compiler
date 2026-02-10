# OurPascal Compiler (Java)

A Pascal-like compiler implemented in Java using JavaCC. The compiler includes a full pipeline consisting of lexical analysis, recursive-descent parsing, Abstract Syntax Tree (AST) construction, semantic and type checking, and code generation to a small stack-based virtual machine / assembly-style instruction set.

---

## Compiler Pipeline

- **Lexing and Parsing**
  - Grammar defined in JavaCC
  - Recursive-descent parser generated from the grammar
  - Produces an Abstract Syntax Tree (AST)

- **AST Construction**
  - Explicit AST node hierarchy representing expressions, statements, and control-flow structures
  - AST used as the intermediate representation for later compiler passes

- **Semantic Analysis**
  - Hash-based symbol tables with nested scope management
  - Type checking and consistency validation for variables, expressions, and control flow

- **Code Generation**
  - AST-walking code generator
  - Emits low-level, stack-based instructions
  - Manages stack frames and instruction translation for execution on a small virtual machine

---

## Repository Structure

