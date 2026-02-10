# OurPascal Compiler (Java)

A Pascal-like compiler built in Java using JavaCC. The pipeline includes parsing, AST construction, semantic/type checking, and code generation to a small virtual-machine / assembly-style instruction set.

## What’s in this repo
- `grammar/OurPascal.jj` — JavaCC grammar + the `OurPascal` driver (includes `main`)
- `src/` — compiler support classes (AST nodes, types, symbol/variable storage, VM ops, instruction representation, etc.)

## Build / Run (typical JavaCC workflow)
> Adjust commands to match how your course/IDE generates JavaCC sources.

1. Generate parser/lexer from the grammar (JavaCC):
   - Run JavaCC on `grammar/OurPascal.jj` (often produces `OurPascal.java`, `OurPascalTokenManager.java`, etc.)
2. Compile:
   - `javac` all `.java` sources (including the generated JavaCC sources)
3. Run:
   - `java OurPascal <options> <input-file>`

## Notes on sharing
If this was completed for a course, consider keeping the repo **private** or removing any instructor-provided tests/handouts before making it public.

## Resume bullets (for reference)
- Compiler pipeline with JavaCC (lexing/parsing), AST construction, semantic/type checking
- Hash-based symbol tables + scope management + consistency checks
- AST-walking code generator with stack-frame management and instruction translation
