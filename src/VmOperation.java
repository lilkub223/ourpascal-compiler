/*-------------------------------------------------------------------------*
 *---                                                                   ---*
 *---        VmOperation.java                                           ---*
 *---                                                                   ---*
 *---        This file declares an enum that represents the operations  ---*
 *---    that are implemented by the Virtual Machine.                   ---*
 *---                                                                   ---*
 *---    ----    ----    ----    ----    ----    ----    ----    ----   ---*
 *---                                                                   ---*
 *---    Version 1a        2025 June 08      Joseph Phillips            ---*
 *---                                                                   ---*
 *-------------------------------------------------------------------------*/

enum	VmOperation
	{
	 // 0 vars
	 NO_VM_OP,

	 // 1 var
	 BOOL_CLEAR_VM_OP,
	 INT_CLEAR_VM_OP,
	 REAL_CLEAR_VM_OP,
	 STDOUT_PRINT_VM_OP,
	 STDOUT_PRINTLN_VM_OP,

	 // 2 vars
	 INT_COPY_VM_OP,
	 REAL_COPY_VM_OP,
	 IDEA_COPY_VM_OP,
	 INT_ADD_VAR_VAR_VM_OP,
	 REAL_ADD_VAR_VAR_VM_OP,
	 INT_SUB_VAR_VAR_VM_OP,
	 REAL_SUB_VAR_VAR_VM_OP,
	 INT_MUL_VAR_VAR_VM_OP,
	 REAL_MUL_VAR_VAR_VM_OP,
	 LOGIC_NOT_VM_OP,
	 INT_TO_REAL_VM_OP,

	 // 3 vars
	 INT_DIV_VM_OP,
	 REAL_DIV_VM_OP,
	 MOD_VM_OP,
	 LOGIC_AND_VM_OP,
	 LOGIC_OR_VM_OP,
	 INT_EQUAL_VM_OP,
	 INT_NOT_EQUAL_VM_OP,
	 REAL_EQUAL_VM_OP,
	 REAL_NOT_EQUAL_VM_OP,
	 INT_LESSER_VM_OP,
	 REAL_LESSER_VM_OP,
	 INT_LESSER_EQUAL_VM_OP,
	 REAL_LESSER_EQUAL_VM_OP,
	 INT_GREATER_VM_OP,
	 REAL_GREATER_VM_OP,
	 INT_GREATER_EQUAL_VM_OP,
	 REAL_GREATER_EQUAL_VM_OP,

	 // 1 addr
	 GOTO_VM_OP,
	 ADDR_LABEL_OP,

	 // 1 var, 1 addr
	 IF_FALSE_GOTO_VM_OP,
	 IF_TRUE_GOTO_VM_OP
	}


