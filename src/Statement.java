/*-------------------------------------------------------------------------*
 *---									---*
 *---		Statement.java						---*
 *---									---*
 *---	    This file declares classes that represent nodes in the	---*
 *---	parse tree for the ourPascal interpreter and compiler.		---*
 *---									---*
 *---	----	----	----	----	----	----	----	----	---*
 *---									---*
 *---	Version 1A		2025 November 20	Joseph Phillips	---*
 *---									---*
 *-------------------------------------------------------------------------*/

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.PrintStream;


//  PURPOSE: To represent an abstract node in the parse tree.
public abstract
class Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  //  PURPOSE:  To initialize 'this'.  No parameters.  No return value.
  public Statement()
  {
  }

  //  II. Accessor(s):
  //  PURPOSE: To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public abstract
  Type		getType		();

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public abstract
  Value		compute		();


  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
  				throws RuntimeException
  {
  }


  //  PURPOSE:  To convert 'this' into PotentialInstruction* instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction>	list
				)
  {
    return(null);
  }


  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public abstract
  Statement	copy		();

  //  V.  Protected methods:

  //  VI. Private member vars:
}


//  PURPOSE: To represent an 'if' statement in the parse tree.
class IfStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  //  PURPOSE:  To initialize 'this'.  No return value.
  public
  IfStatement			(Statement	cond,
				 Statement	then,
				 Statement	elseRef
				)
  {
    cond_	= cond;
    then_	= then;
    elseRef_	= elseRef;
  }

  //  PURPOSE:  To initialize 'this'.  'elseRef' is optional, assumed to be
  //	'null' if not provided. No return value.
  public
  IfStatement			(Statement	cond,
  				 Statement	then
				)
  {
    this(cond,then,null);
  }

  //  II. Accessor(s):
  //  PURPOSE: To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(Type.NONE);
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    Value	value	= cond_.compute();

    if  (value.getBoolean())
    {
      then_.compute();
    }
    else
    if  (elseRef_ != null)
    {
      elseRef_.compute();
    }

    return(null);
  }

  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
				throws RuntimeException
  {
    cond_.check();;
    if(cond_.getType()!=Type.BOOLEAN){
      throw new RuntimeException("requires boolean expression");
    }
    if(then_!=null){
      then_.check();
    }
    if(elseRef_!=null){
      elseRef_.check();
    }
  }


  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    Variable condV= cond_.toAssembly(list);
    long elseStart=PotentialInstruction.getNextAddressLabel();
    Long ifEnd=null;
    if(elseRef_!=null){
      ifEnd=PotentialInstruction.getNextAddressLabel();
    }
list.add(new VarAddrPotentialInstruction(VmOperation.IF_FALSE_GOTO_VM_OP,condV,elseStart));
    if(then_!=null){
      then_.toAssembly(list);
    }
    if(elseRef_!=null) {
      list.add(new AddrPotentialInstruction(VmOperation.GOTO_VM_OP, ifEnd.longValue()));

      PotentialInstruction else2 = new PotentialInstruction(VmOperation.NO_VM_OP);
      else2.setAddressLabel(ifEnd.longValue());
      list.add(else2);
    }
    return null;

  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return( (elseRef_ == null)
    	    ? new IfStatement(cond_.copy(),then_.copy())
	    : new IfStatement(cond_.copy(),then_.copy(),elseRef_.copy())
	  );
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE: To hold the address of the condition.
  private
  Statement		cond_;

  //  PURPOSE: To hold the address of the then (body).
  private
  Statement		then_;

  //  PURPOSE: To hold the address of the else.
  private
  Statement		elseRef_;
}


//  PURPOSE: To represent a 'while' statement in the parse tree.
class WhileStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  public WhileStatement(Statement cond, Statement block)
  {
    cond_	= cond;
    block_	= block;
  }

  //  II. Accessor(s):
  //  PURPOSE: To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(Type.NONE);
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    Value	value;

    while  ( (value = cond_.compute()).getBoolean() )
    {
      block_.compute();
    }

    return(null);
  }

  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
				throws RuntimeException
  {
    cond_.check();
    if(cond_.getType()!=Type.BOOLEAN){
      throw new RuntimeException("While condition requires boolean expression");
    }

    block_.check();


  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
   long beginning=PotentialInstruction.getNextAddressLabel();
   long end=PotentialInstruction.getNextAddressLabel();
   PotentialInstruction beginningNoOp=new PotentialInstruction(VmOperation.NO_VM_OP);
   beginningNoOp.setAddressLabel(beginning);
   list.add(beginningNoOp);

   Variable condV=cond_.toAssembly(list);
   list.add(new VarAddrPotentialInstruction(VmOperation.IF_FALSE_GOTO_VM_OP,condV,end));
   block_.toAssembly(list);
   list.add(new AddrPotentialInstruction(VmOperation.GOTO_VM_OP,beginning));
   PotentialInstruction endNoOp=new PotentialInstruction(VmOperation.NO_VM_OP);
   endNoOp.setAddressLabel(end);
   list.add(endNoOp);
   return null;

  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return( new WhileStatement(cond_.copy(), block_.copy()) );
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE: To hold the address of the condition.
  private
  Statement		cond_;

  //  PURPOSE: To hold the address of the body.
  private
  Statement		block_;
}


//  PURPOSE: To represent a block of statements (e.g., statements inside a {}
//	or BEGIN...END block).
class	BlockStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  public BlockStatement()
  {
    // list_ is initialized as an ArrayList by default
  }

  //  II. Accessor(s):
  //  PURPOSE: To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(Type.NONE);
  }

  //  III. Mutator(s):
  public
  void		append		(Statement newInstruct)
  {
    list_.add(newInstruct);
  }

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    for  (Statement statement : list_)
    {
      statement.compute();
    }
    return(null);
  }

  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
				throws RuntimeException
  {
    for  (Statement statement : list_)
    {
      statement.check();
    }
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    for  (Statement statement : list_)
    {
      statement.toAssembly(list);
    }

    return(null);
  }


  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    BlockStatement	returnMe	= new BlockStatement();

    for  (Statement statement : list_)
    {
      returnMe.append(statement.copy());
    }

    return(returnMe);
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE: To hold the addresses of the code to run.
  private List<Statement>	list_ = new ArrayList<>();
}



//  PURPOSE: To represent a variable reference in the parse tree.
class VariableStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  //  PURPOSE:  To initialize 'this'.  No return value.
  public
  VariableStatement		(Variable newVar)
  {
    variable_ = newVar;
  }

  //  II. Accessor(s):
  //  PURPOSE:  To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(getVariable().getType());
  }

  //  PURPOSE: To return the address of the variable being referenced.
  public
  Variable	getVariable	()
  {
    return(variable_);
  }

  //  III. Mutator(s):
  //  PURPOSE:  To set the type of 'this' variable to 'type'.
  public
  void		setType		(Type type)
  {
    variable_.setType(type);
  }

  //  PURPOSE:  To set the value to 'newValue'
  public
  void		setValue	(Value newValue)
  {
    variable_.setValue(newValue);
  }

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    return(getVariable().getValue().copy());
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    return(getVariable());
  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return(new VariableStatement(variable_));
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE: To hold the address of the variable being referenced.
  private
  Variable			variable_;
}

//  PURPOSE: To represent an assignment statement (e.g., `var := expr`).
class AssignStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  public
  AssignStatement		(Variable newVar,
				 Statement newExpr
				)
  {
    var_	= newVar;
    expr_	= newExpr;
  }

  //  II. Accessor(s):
  //  PURPOSE:  To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(Type.NONE);
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    var_.setValue(expr_.compute());
    return(null);
  }

  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
				throws RuntimeException
  {
    expr_.check();;
    Type leftSideT=var_.getType();
    Type rightSideT=expr_.getType();
    if(leftSideT==rightSideT){
      return;
    }
    if(leftSideT==Type.REAL&&rightSideT==Type.INTEGER){
      return;
    }
    throw new RuntimeException("Invalid assignment types");
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    Variable rightSideV=expr_.toAssembly(list);
    Type leftSideT=var_.getType();
    Type rightSideT=rightSideV.getType();

    if(leftSideT==rightSideT){
      VmOperation operation;

    if(leftSideT==Type.REAL){
      operation=VmOperation.REAL_COPY_VM_OP;
    } else if (leftSideT==Type.INTEGER) {
      operation=VmOperation.INT_COPY_VM_OP;
    }else{
      operation=VmOperation.IDEA_COPY_VM_OP;
    }
    list.add(new VarVarPotentialInstruction(operation,var_,rightSideV));
      
    } else if (leftSideT==Type.REAL&&rightSideT==Type.INTEGER) {
      list.add(new VarVarPotentialInstruction(VmOperation.INT_TO_REAL_VM_OP,var_,rightSideV));
      
    }
    return null;
  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return(new AssignStatement(var_,expr_.copy()));
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE: To hold the address of the body.
  private
  Variable			var_;

  //  PURPOSE: To hold the address of the expression.
  private
  Statement			expr_;
}

//  PURPOSE: To represent a unary operation (e.g., 'NOT expr').
class UnaryOpStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  //  PURPOSE:  To initialize 'this'. No return value.
  public
  UnaryOpStatement		(Operation	newOperation,
  				 Statement	newOperand
				)
  {
    operation_	= newOperation;
    operand_	= newOperand;
  }

  //  II. Accessor(s):
  //  PURPOSE:  To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    switch  (getOperation())
    {
    case NOT :
      return(Type.BOOLEAN);

    default :
      return(Type.NONE);
    }
  }

  //  PURPOSE:  To return the operation to do.
  public
  Operation	getOperation	()
  {
    return(operation_);
  }

  //  PURPOSE:  To return the address of node on the left hand side.
  public
  Statement	getOperand	()
  {
    return(operand_);
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To return the value computed 'this'.  No parameters.
  public
  Value		compute		()
  {
    Value	operandVal	= getOperand().compute();
    Value	returnMe	= null;

    switch  (getOperation())
    {
    case NOT :
      if  (getOperand().getType() == Type.BOOLEAN)
      {
	returnMe = new BooleanValue(!operandVal.getBoolean());
      }
      else
      {
	throw new IllegalArgumentException("Type mismatch in comparison");
      }

      break;

    default :
      break;
    }

    return(returnMe);
  }

  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
  				throws RuntimeException
  {
    getOperand().check();

    switch  (getOperation())
    {
    case NOT :
      if  (getOperand().getType() != Type.BOOLEAN)
      {
	throw new IllegalArgumentException("NOT requires boolean");
      }
      break;
    }
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    Variable	operand	= getOperand().toAssembly(list);
    Variable	result	= VarStore.get().obtainTempVar(getType());

    switch  (getOperation())
    {
    case NOT :
      list.add
	(new VarVarPotentialInstruction
		( VmOperation.LOGIC_NOT_VM_OP,
		  result,
		  operand
		)
	);
      break;
    }

    return(result);
  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return(new UnaryOpStatement(getOperation(), getOperand().copy()));
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE:  To tell the operation to do.
  private
  Operation		operation_;

  //  PURPOSE:  To hold the address of node on the left hand side.
  private
  Statement		operand_;
}


//  PURPOSE: To represent a binary operation (e.g., 'expr + expr').
class BinaryOpStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  //  PURPOSE:  To initialize 'this'. No return value.
  public
  BinaryOpStatement		(Operation	newOperation,
				 Statement	lhs,
				 Statement	rhs
				)
  {
    lhs_	= lhs;
    operation_	= newOperation;
    rhs_	= rhs;
  }

  //  II. Accessor(s):
  //  PURPOSE:  To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    switch  (getOperation())
    {
    case EQUALS:
    case NOT_EQUALS:
    case LESSER:
    case LESSER_EQUALS:
    case GREATER:
    case GREATER_EQUALS:
    case OR:
    case AND:
      return(Type.BOOLEAN);

    case SLASH:
      return(Type.REAL);

    case MOD:
    case DIV:
      return(Type.INTEGER);

    case PLUS:
    case MINUS:
    case STAR:
      if  ( (getLeft().getType() == Type.INTEGER)	&&
	    (getRight().getType() == Type.INTEGER)
	  )
      {
	return(Type.INTEGER);
      }

      return(Type.REAL);

    default :
      return(Type.NONE);
    }
  }

  //  PURPOSE:  To return the operation to do.
  public
  Operation	getOperation	()
  {
    return(operation_);
  }

  //  PURPOSE:  To return the address of node on the left hand side.
  public
  Statement	getLeft	()
  {
    return(lhs_);
  }

  //  PURPOSE:  To return the address of node on the right hand side.
  public
  Statement	getRight	()
  {
    return(rhs_);
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To return the value computed 'this'.  No parameters.
  public
  Value		compute		()
  {
    Value	leftValue	= getLeft().compute();
    Value	rightValue	= getRight().compute();
    Value	returnMe	= null;

    switch  (getOperation())
    {
    case EQUALS:
    case NOT_EQUALS:
    case LESSER:
    case LESSER_EQUALS:
    case GREATER:
    case GREATER_EQUALS:
      if (getType() == Type.BOOLEAN)
      {
        if (leftValue.getType() == Type.BOOLEAN)
	{
          boolean result = false;
	  
          switch (getOperation())
	  {
          case EQUALS:
	    result = leftValue.getBoolean() == rightValue.getBoolean();
	    break;
          case NOT_EQUALS:
	    result = leftValue.getBoolean() != rightValue.getBoolean();
	    break;
          default:
	    throw new IllegalArgumentException
				("Non-boolean comparison on boolean types");
          }
          returnMe = new BooleanValue(result);
        }
	else
	if (leftValue.getType() == Type.STRING)
	{
          boolean result = false;

          switch (getOperation())
	  {
          case EQUALS:
	    result = leftValue.getString().equals(rightValue.getString());
	    break;
          case NOT_EQUALS:
	    result = !leftValue.getString().equals(rightValue.getString());
	    break;
          case LESSER:
	    result = leftValue.getString().compareTo(rightValue.getString())<0;
	    break;
          case LESSER_EQUALS:
	    result = leftValue.getString().compareTo(rightValue.getString())
	    	     <= 0;
	    break;
          case GREATER:
	    result = leftValue.getString().compareTo(rightValue.getString())>0;
	    break;
          case GREATER_EQUALS:
	    result = leftValue.getString().compareTo(rightValue.getString())
	    	     >= 0;
	    break;
          }
          returnMe = new BooleanValue(result);
        }
	else
	{
          Type resultType = getType();
	  
          if  ( leftValue.getType() == Type.INTEGER &&
	        rightValue.getType() == Type.INTEGER
	      )
	  {
            long	lhs	= leftValue.getInteger();
            long	rhs	= rightValue.getInteger();	    
            boolean	result	= false;
	    
            switch (getOperation())
	    {
            case EQUALS:	 result = (lhs == rhs); break;
            case NOT_EQUALS:	 result = (lhs != rhs); break;
            case LESSER:	 result = (lhs <  rhs); break;
            case LESSER_EQUALS:	 result = (lhs <= rhs); break;
            case GREATER:	 result = (lhs >  rhs); break;
            case GREATER_EQUALS: result = (lhs >= rhs); break;
            }
	    
            returnMe = new BooleanValue(result);
          }
	  else
	  {   // At least one is REAL, compare as doubles
            double	lhs	= leftValue.getReal();
            double	rhs	= rightValue.getReal();
            boolean	result	= false;

            switch (getOperation())
	    {
            case EQUALS:	  result = (lhs == rhs); break;
            case NOT_EQUALS:	  result = (lhs != rhs); break;
            case LESSER:	  result = (lhs <  rhs); break;
            case LESSER_EQUALS:	  result = (lhs <= rhs); break;
            case GREATER:	  result = (lhs >  rhs); break;
            case GREATER_EQUALS:  result = (lhs >= rhs); break;
            }
	    
            returnMe = new BooleanValue(result);
          }
        }
      }
      break;

    case DIV:
    case MOD:
      {
	long	lhs	= leftValue.getInteger();
	long	rhs	= rightValue.getInteger();

	switch  (getOperation())
	{
	case DIV :
	  returnMe	= new IntegerValue(lhs / rhs);
	  break;

	case MOD :
	  returnMe	= new IntegerValue(lhs % rhs);
	  break;

	default :
	  break;
	}
      }
      break;

    case PLUS:
    case MINUS:
    case STAR:
    case SLASH:
      {
        Type	resultType = getType();

        if  (resultType == Type.INTEGER)
	{
          long lhs = leftValue.getInteger();
          long rhs = rightValue.getInteger();

          switch (getOperation())
	  {
          case PLUS:	returnMe = new IntegerValue(lhs + rhs); break;
          case MINUS:	returnMe = new IntegerValue(lhs - rhs); break;
          case STAR:	returnMe = new IntegerValue(lhs * rhs); break;
          default:	break; // Should not happen for INTEGER
          }
        }
	else
	{ // REAL
          double lhs = leftValue.getReal();
          double rhs = rightValue.getReal();
          switch (getOperation())
	  {
          case PLUS:	returnMe = new RealValue(lhs + rhs); break;
          case MINUS:	returnMe = new RealValue(lhs - rhs); break;
          case STAR:	returnMe = new RealValue(lhs * rhs); break;
          case SLASH:	returnMe = new RealValue(lhs / rhs); break;
          default:	break; // Should not happen for REAL
          }
        }
      }
      break;

    case AND:
    case OR:
      {
	boolean	lhs	= leftValue.getBoolean();
	boolean	rhs	= rightValue.getBoolean();

	switch  (getOperation())
	{
	case AND :
	  returnMe	= new BooleanValue(lhs && rhs);
	  break;

	case OR :
	  returnMe	= new BooleanValue(lhs || rhs);
	  break;

	default :
	  break;
	}
      }
      break;

    default :
      break;
    }

    return(returnMe);
  }


  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
				throws RuntimeException
  {
    getLeft().check();
    getRight().check();

    switch  (getOperation())
    {
    case EQUALS:
    case NOT_EQUALS:
    case LESSER:
    case LESSER_EQUALS:
    case GREATER:
    case GREATER_EQUALS:
      // Simplified: checks for numeric/boolean/string compatibility.
      if  (getLeft().getType() != getRight().getType())
      {
	if  ( ( (getLeft().getType() != Type.INTEGER) &&
      	        (getLeft().getType() != Type.REAL)
	      )							||
      	      ( (getRight().getType() != Type.INTEGER) &&
	        (getRight().getType() != Type.REAL)
	      )
	    )
	{
	  throw new IllegalArgumentException
			("Comparisons require compatible operands");
	}
      }
      break;

    case PLUS:
    case MINUS:
    case STAR:
    case SLASH:
      if  ( ( (getLeft().getType() != Type.INTEGER) &&
      	      (getLeft().getType() != Type.REAL)
	    )							||
      	    ( (getRight().getType() != Type.INTEGER) &&
	      (getRight().getType() != Type.REAL)
	    )
	  )
      {
	throw new IllegalArgumentException("+,-,*,/ require numeric operands");
      }
      break;

    case DIV:
    case MOD:
      if  ( (getLeft().getType() != Type.INTEGER) ||
      	    (getRight().getType() != Type.INTEGER)
	  )
      {
	throw new IllegalArgumentException
			("DIV and MOD require integer operands");
      }
      break;

    case AND:
    case OR:
      if  ( (getLeft().getType() != Type.BOOLEAN) ||
      	    (getRight().getType() != Type.BOOLEAN)
	  )
      {
	throw new IllegalArgumentException
			("Boolean operators require boolean operands");
      }
      break;
    }
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    Variable	leftVar		= getLeft().toAssembly(list);
    Variable	rightVar	= getRight().toAssembly(list);
    Variable	result		= VarStore.get().obtainTempVar(getType());
    Variable	leftConvert	= null;
    Variable	rightConvert	= null;
    VmOperation	vmOp		= VmOperation.NO_VM_OP;
    Type	operandType;

    switch  (getOperation())
    {
    case EQUALS :
    case NOT_EQUALS :
    case LESSER :
    case LESSER_EQUALS :
    case GREATER :
    case GREATER_EQUALS :
      operandType	= getLeft().getType();

      if  (getLeft().getType() == Type.INTEGER)
      {
	if  (getRight().getType() == Type.INTEGER)
	{
	  operandType	= Type.INTEGER;
	}
	else
	if  (getRight().getType() == Type.REAL)
	{
	  operandType	= Type.REAL;
	  leftConvert	= VarStore.get().obtainTempVar(Type.REAL);
	  list.add
		(new VarVarPotentialInstruction
			( VmOperation.INT_TO_REAL_VM_OP,
			  leftConvert,
			  leftVar
			)
		);
	  leftVar	= leftConvert;
	}
      }
      else
      if  (getLeft().getType() == Type.REAL)
      {
	if  (getRight().getType() == Type.INTEGER)
	{
	  operandType	= Type.REAL;
	  rightConvert	= VarStore.get().obtainTempVar(Type.REAL);
	  list.add
		(new VarVarPotentialInstruction
			( VmOperation.INT_TO_REAL_VM_OP,
			  rightConvert,
			  rightVar
			)
		);
	  rightVar	= rightConvert;
	}
	else
	if  (getRight().getType() == Type.REAL)
	{
	  operandType	= Type.REAL;
	}
      }

      switch  (getOperation())
      {
      case EQUALS :
	vmOp	= (operandType == Type.INTEGER)
		  ? VmOperation.INT_EQUAL_VM_OP : VmOperation.REAL_EQUAL_VM_OP;
	break;
      case NOT_EQUALS :
	vmOp	= (operandType==Type.INTEGER)
		  ? VmOperation.INT_NOT_EQUAL_VM_OP
		  : VmOperation.REAL_NOT_EQUAL_VM_OP;
	break;
      case LESSER :
	vmOp	= (operandType==Type.INTEGER)
		  ? VmOperation.INT_LESSER_VM_OP
		  : VmOperation.REAL_LESSER_VM_OP;
	break;
      case LESSER_EQUALS :
	vmOp	= (operandType == Type.INTEGER)
		  ? VmOperation.INT_LESSER_EQUAL_VM_OP
		  : VmOperation.REAL_LESSER_EQUAL_VM_OP;
	break;
      case GREATER :
	vmOp	= (operandType==Type.INTEGER)
		  ? VmOperation.INT_GREATER_VM_OP
		  : VmOperation.REAL_GREATER_VM_OP;
	break;
      case GREATER_EQUALS :
	vmOp	= (operandType == Type.INTEGER)
	  	  ? VmOperation.INT_GREATER_EQUAL_VM_OP
		  : VmOperation.REAL_GREATER_EQUAL_VM_OP;
	break;
      }
	
      list.add
	(new VarVarVarPotentialInstruction
		( vmOp,
		  result,
		  leftVar,
		  rightVar
		)
	);
      break;

    case SLASH :
      if  (getLeft().getType() == Type.INTEGER)
      {
	leftConvert	= VarStore.get().obtainTempVar(Type.REAL);
	list.add
		(new VarVarPotentialInstruction
			( VmOperation.INT_TO_REAL_VM_OP,
			  leftConvert,
			  leftVar
			)
		);
	leftVar	= leftConvert;
      }

      if  (getRight().getType() == Type.INTEGER)
      {
	rightConvert	= VarStore.get().obtainTempVar(Type.REAL);
	list.add
		(new VarVarPotentialInstruction
			( VmOperation.INT_TO_REAL_VM_OP,
			  rightConvert,
			  rightVar
			)
		);
	rightVar	= rightConvert;
      }

      list.add
	(new VarVarVarPotentialInstruction
		( VmOperation.REAL_DIV_VM_OP,
		  result,
		  leftVar,
		  rightVar
		)
	);
      break;

    case DIV :
    case MOD :
      list.add
	(new VarVarVarPotentialInstruction
		( (getOperation() == Operation.DIV)
		  ? VmOperation.INT_DIV_VM_OP
		  : VmOperation.MOD_VM_OP,
		  result,
		  leftVar,
		  rightVar
		)
	);
      break;
      
    case AND :
    case OR :
      list.add
	(new VarVarVarPotentialInstruction
		( (getOperation() == Operation.AND)
		  ? VmOperation.LOGIC_AND_VM_OP
		  : VmOperation.LOGIC_OR_VM_OP,
		  result,
		  leftVar,
		  rightVar
		)
	);
      break;

    case PLUS:
    case MINUS:
    case STAR:
      operandType	= leftVar.getType();

      if  (leftVar.getType() == Type.INTEGER)
      {
	if  (rightVar.getType() == Type.REAL)
	{
	  operandType	= Type.REAL;
	  leftConvert	= VarStore.get().obtainTempVar(Type.REAL);
	  list.add
		(new VarVarPotentialInstruction
			( VmOperation.INT_TO_REAL_VM_OP,
			  leftConvert,
			  leftVar
			)
		);
	  leftVar	= leftConvert;
	}
      }
      else
      if  (leftVar.getType() == Type.REAL)
      {
	if  (rightVar.getType() == Type.INTEGER)
	{
	  operandType	= Type.REAL;
	  rightConvert	= VarStore.get().obtainTempVar(Type.REAL);
	  list.add
		(new VarVarPotentialInstruction
			( VmOperation.INT_TO_REAL_VM_OP,
			  rightConvert,
			  rightVar
			)
		);
	  rightVar	= rightConvert;
	}
	else
	if  (rightVar.getType() == Type.REAL)
	{
	  operandType	= Type.REAL;
	}
      }

      switch  (getOperation())
      {
      case PLUS :
	vmOp	= (getType() == Type.INTEGER)
		  ? VmOperation.INT_ADD_VAR_VAR_VM_OP
		  : VmOperation.REAL_ADD_VAR_VAR_VM_OP;
	break;
      case MINUS :
	vmOp	= (getType() == Type.INTEGER)
		  ? VmOperation.INT_SUB_VAR_VAR_VM_OP
		  : VmOperation.REAL_SUB_VAR_VAR_VM_OP;
	break;
      case STAR :
	vmOp	= (getType() == Type.INTEGER)
		  ? VmOperation.INT_MUL_VAR_VAR_VM_OP
		  : VmOperation.REAL_MUL_VAR_VAR_VM_OP;
	break;

      default :
        throw new IllegalArgumentException
			("Non-handled op case in "	+
			 "BinaryOpStatement.toAssembly()"
			);
      }

      list.add
	(new VarVarPotentialInstruction
		( (getType()==Type.INTEGER)
		  ? VmOperation.INT_COPY_VM_OP
		  : VmOperation.REAL_COPY_VM_OP,
		  result,
		  leftVar
		)
	);
       list.add(new VarVarPotentialInstruction(vmOp,result,rightVar));
      break;
    }

    return(result);
  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return
	(new BinaryOpStatement
			(getOperation(),
			 getLeft().copy(),
			 getRight().copy()
			)
	);
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE:  To tell the operation to do.
  private
  Operation		operation_;

  //  PURPOSE:  To hold the address of node on the left hand side.
  private
  Statement		lhs_;

  //  PURPOSE:  To hold the address of node on the right hand side.
  private
  Statement		rhs_;
}



//  PURPOSE: To represent a constant value in the parse tree.
class	ConstantStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  public
  ConstantStatement	(boolean boolValue)
  {
    value_	= new BooleanValue(boolValue);
  }

  public
  ConstantStatement	(long constant)
  {
    value_	= new IntegerValue(constant);
  }

  public
  ConstantStatement	(double constant)
  {
    value_	= new RealValue(constant);
  }

  public
  ConstantStatement	(String textStr)
  {
    value_	= new StringValue(textStr);
  }

  public
  ConstantStatement	(Value newValue)
  {
    value_	= newValue.copy();
  }

  //  II. Accessor(s):
  //  PURPOSE:  To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(value_.getType());
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    return(value_.copy());
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly	(List<PotentialInstruction> list)
  {
    return(VarStore.get().obtainTempVar(value_));
  }

  //	PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return(new ConstantStatement(value_.copy()));
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE:  To hold the constant.
  private Value	value_;
}

//  PURPOSE: To represent a print statement (e.g., 'write(expr)' or
//	'writeln(expr)'.
class PrintStatement extends Statement
{
  //  0.  Constants and hidden classes (if any):

  //  I.  Constructor(s) and factory(s):
  public
  PrintStatement	(boolean newShouldPrintNewline, Statement newExpr)
  {
    shouldWriteNewline_ = newShouldPrintNewline;
    expression_      = newExpr;
  }

  //  II. Accessor(s):
  //  PURPOSE:  To return the type that 'this' node returns, or 'NONE' if
  //	it does not return a value.
  public
  Type		getType		()
  {
    return(Type.NONE);
  }

  //  PURPOSE:  To return 'true' if 'this' node should print a newline after
  //	printing its expression value, or 'false' otherwise.
  public
  boolean	getShouldWriteNewline
				()
  {
    return(shouldWriteNewline_);
  }

  //  III. Mutator(s):

  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE:  To compute 'this'.  No parameters.  Returns either the
  //	address of a heap-allocated Value that tells the computation of
  //	'this' node, or 'null' if no such value is appropriate.
  public
  Value		compute		()
  {
    Value	value	= expression_.compute();

    value.print();

    if  (getShouldWriteNewline())
    {
      System.out.println();
    }

    return(null);
  }


  //  PURPOSE:  To check that 'this' node is semantically proper. Throws
  //	'RuntimeException' describing problem if one is found. No return
  //	value.
  public
  void		check		()
				throws RuntimeException
  {
    expression_.check();
  }

  //  PURPOSE:  To convert 'this' into PotentialInstruction instances in
  //	'list' that implement 'this'.  Returns Variable that will have the
  //	result of 'this' computation, or 'null' if there is no such value.
  public
  Variable	toAssembly		(List<PotentialInstruction> list)
  {
    VmOperation	op	= getShouldWriteNewline()
			  ? VmOperation.STDOUT_PRINTLN_VM_OP
			  : VmOperation.STDOUT_PRINT_VM_OP;

    list.add
	(new VarPotentialInstruction
		(op,
		 expression_.toAssembly(list)
		)
	);
    return(null);
  }

  //  PURPOSE:  To return a copy of 'this'.  No parameters.
  public
  Statement	copy		()
  {
    return(new PrintStatement(shouldWriteNewline_, expression_.copy()));
  }

  //  V. Protected methods:

  //  VI. Private member vars:
  //  PURPOSE:  To hold 'true' if 'this' node should print a newline after
  //	printing its expression value, or 'false' otherwise.
  private
  boolean		shouldWriteNewline_;

  //  PURPOSE:  To hold the expression whose value should be computed and
  //	written.
  private
  Statement		expression_;
}

