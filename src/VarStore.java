/*-------------------------------------------------------------------------*
 *---                                                                   ---*
 *---       VarStore.java					        ---*
 *---                                                                   ---*
 *---	    This file declares classes that holds information about	---*
 *---	Variable instances.						---*
 *---                                                                   ---*
 *---    ----    ----    ----    ----    ----    ----    ----    ----   ---*
 *---                                                                   ---*
 *---    Version 1a        2025 May 31       Joseph Phillips            ---*
 *---                                                                   ---*
 *-------------------------------------------------------------------------*/

import java.util.HashMap;
import java.util.Map;
import java.io.PrintStream;

public class VarStore
{
  //  I.  Constructor:

  //  II.  Accessor(s):
  public static
  final char		VAR_PREFIX_CHAR			= '@';

  public static
  final String		typeNameArray[]
			= { "no-type",
			    "Boolean",
			    "Integer",
			    "Real",
			    "String"
			  };

  //  PURPOSE:  To return the reference to the singleton instance.
  public static
  VarStore	get		()
  {
    return singleton_static;
  }

  //  PURPOSE:  To return the address of an existing variable with the same
  //	name as 'var'.  Throws exception if not seen.
  public
  Variable	findExisting	(String	nameStr)
  {
    Variable	existingVar	= (Variable)nameToVarMap_.get(nameStr);

    if  (existingVar == null)
    {
       throw new IllegalArgumentException
			("Attempt to use non-declared variable " + nameStr);
    }

    return existingVar;
  }

  //  PURPOSE: To attempt to declare a variable named 'var' to have type 'type'.
  public
  void		declare		(Variable	var,
  				 Type		type
				)
  {
    String	nameStr	= var.getNameStr();

    if  (nameToVarMap_.containsKey(nameStr))
    {
      throw new IllegalArgumentException
			("Attempt to double-declare variable " + nameStr);
    }

    var.setType(type);
    nameToVarMap_.put(nameStr, var);
  }


  //  PURPOSE:  To create and return the address of a new Variable of Type
  //	'type'.
  public
  Variable	obtainTempVar	(Type	type
  				)
  {
    int		varIndex	= nextTempVarIndex_++;
    String	varName		= "_tempVar" + varIndex;
    Variable	returnMe	= new Variable(varName);

    returnMe.setType(type);
    nameToVarMap_.put(varName,returnMe);
    return(returnMe);
  }


  //  PURPOSE:  To create and return the address of a new Variable that is
  //	initialized to value 'value'.
  public
  Variable	obtainTempVar	(Value	value
  				)
  {
    int		varIndex	= nextTempVarIndex_++;
    String	varName		= "_tempVar" + varIndex;
    Variable	returnMe	= new Variable(varName,value);

    nameToVarMap_.put(varName,returnMe);
    return(returnMe);
  }



  //  PURPOSE:  To print the assembly language representation of '*this' to
  //	'file'.  No return value.
  public
  void		toAssembly	(PrintStream		file
				)
  {
    file.println("  %beginVarDecl");

    for  (Map.Entry<String,Variable> entry : nameToVarMap_.entrySet())
    {
      Variable	var	= entry.getValue();
      Value	value	= var.getValue();

      if  (value.getType() == Type.STRING)
      {
	file.println
	    ("    %var\t"				+
	     VAR_PREFIX_CHAR + var.getNameStr()		+
	     ","					+
	     typeNameArray[var.getType().ordinal()]	+
	     ",\"" + value + "\""
      	    );
      }
      else
      {
	file.println
	    ("    %var\t"				+
	     VAR_PREFIX_CHAR + var.getNameStr()		+
	     ","					+
	     typeNameArray[var.getType().ordinal()]	+
	     "," + value
	    );
      }

    }

    file.println("  %endVarDecl");
  }

  //  V.  Member vars:
  //  PURPOSE: To keep track of the variables.
  private
  Map<String,Variable>		nameToVarMap_;

  //  PURPOSE:  To hold the index of the next temporary var.
  int				nextTempVarIndex_;

  //  PURPOSE: To hold the singleton instance of this class.
  private static
  VarStore			singleton_static	= new VarStore();

  //  PURPOSE: To initialize 'this' to be empty. No parameters.
  //	No return value.
  private
  VarStore			()
  {
    this.nameToVarMap_		= new HashMap<>();
    this.nextTempVarIndex_	= 0;
  }

}
