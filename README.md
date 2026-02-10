# OurPascal Compiler

A full compiler for a Pascal-like programming language implemented in Java using JavaCC. The compiler follows a traditional multi-phase pipeline, transforming source programs into a stack-based virtual-machine instruction set through parsing, semantic analysis, and code generation.

---

## Overview

This project implements an end-to-end compiler with clear separation between compilation phases. Rather than directly interpreting source programs, the compiler builds an intermediate representation and performs semantic validation before emitting low-level instructions, closely mirroring real-world compiler design.

---

## Compiler Architecture

### Lexical Analysis and Parsing
- Grammar specified using JavaCC
- Recursive-descent parser generated from the grammar
- Produces a strongly-typed Abstract Syntax Tree (AST)

### Abstract Syntax Tree (AST)
- Explicit AST node hierarchy representing expressions, statements, and control-flow constructs
- AST serves as the intermediate representation for later compiler phases

### Semantic Analysis
- Hash-based symbol tables with nested scope management
- Type checking and consistency validation
- Ensures correct variable usage, expression typing, and control-flow correctness

### Code Generation
- AST-driven code generation phase
- Emits low-level, stack-based virtual-machine instructions
- Manages stack frames and instruction sequencing to ensure correct execution

---

## Repository Structure

    grammar/
      OurPascal.jj        JavaCC grammar and compiler entry point

    src/
      Statement.java     AST statement hierarchy
      Variable.java      Variable representation
      VarStore.java      Symbol table and scope handling
      Type.java          Type system definitions
      Value.java         Runtime value representation
      Operation.java     High-level operations
      VmOperation.java   VM-level instructions
      PotentialInstruction.java
      Direction.java


---

## Build and Run

Exact commands may vary depending on JavaCC configuration.

1. Generate the parser and lexer:

    javacc grammar/OurPascal.jj

2. Compile the Java sources:

    javac *.java src/*.java

3. Run the compiler:

    java OurPascal <input-file>

The compiler consumes a Pascal-like source program and produces a sequence of stack-based virtual-machine instructions.

---

## Key Concepts Demonstrated

- Recursive-descent parsing
- Abstract Syntax Tree design
- Semantic analysis and type checking
- Symbol table and scope management
- Instruction-level code generation
- Compiler phase separation

---

