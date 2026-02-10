/*-------------------------------------------------------------------------*
 *---                                                                   ---*
 *---       PotentialInstruction.java                                   ---*
 *---                                                                   ---*
 *---   This file declares a class that represents a potential virtual  ---*
 *---   machine instruction.                                            ---*
 *---                                                                   ---*
 *---    ----    ----    ----    ----    ----    ----    ----    ----   ---*
 *---                                                                   ---*
 *---                                                                   ---*
 *---                                                                   ---*
 *-------------------------------------------------------------------------*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

class PotentialInstruction
{
  // 0. Constants:
  protected static final
  long		NO_ADDRESS_LABEL	= -1;
    
  public static final
  char		VAR_PREFIX_CHAR		= '@';

  public static final
  char		ADDR_LABEL_SUFFIX_CHAR	= '%';
  
  public static final
  String	ADDR_TEMPLATE		= "label%d";

  public static final
  String 	vmOpNameArray[]
		= {
		    // 0 vars
		    "noOp",

		    // 1 var
		    "boolClearOp",
		    "intClearOp",
		    "realClearOp",
		    "stdOutPrintOp",
		    "stdOutPrintLnOp",

		    // 2 vars
		    "intCopyOp",
		    "realCopyOp",
		    "ideaCopyOp",
		    "intAddVarVarOp",
		    "realAddVarVarOp",
		    "intSubVarVarOp",
		    "realSubVarVarOp",
		    "intMulVarVarOp",
		    "realMulVarVarOp",
		    "logicNotOp",
		    "intToRealOp",

		    // 3 vars
		    "intDivOp",
		    "realDivOp",
		    "modOp",
		    "logicAndOp",
		    "logicOrOp",
		    "intEqualOp",
		    "intNotEqualOp",
		    "realEqualOp",
		    "realNotEqualOp",
		    "intLesserOp",
		    "realLesserOp",
		    "intLesserEqualOp",
		    "realLesserEqualOp",
		    "intGreaterOp",
		    "realGreaterOp",
		    "intGreaterEqualOp",
		    "realGreaterEqualOp",

		    // 1 addr
		    "gotoOp",
		    "addrLabelOp",

		    // 1 var, 1 addr
		    "ifFalseGotoOp",
		    "ifTrueGotoOp"
		   };

  //  I. Constructor(s), assignment op(s), factory(s) and destructor:
  //  PURPOSE: To initialize 'this' to do operation 'newOp'. No return value.
  public PotentialInstruction(VmOperation newOp)
  {
    op_			= newOp;
    addressLabel_	= NO_ADDRESS_LABEL;
  }

  //  II. Accessors:
  //  PURPOSE: To set the address label of 'this' instruction to 'newAddr'
  //	No return value.
  public static
  long		getNextAddressLabel	()
  {
    return (nextAddressLabel_static++);
  }

  //  PURPOSE: To return the virtual machine operation. No parameters.
  public
  VmOperation	getOp			()
  {
    return (op_);
  }

  //  PURPOSE: To return the address label of 'this' instruction,
  //	or 'NO_ADDRESS_LABEL' if it has none. No parameters.
  public
  long		getAddressLabel		()
  {
    return (addressLabel_);
  }

  //  III. Mutators:
  //  PURPOSE: To set the address label of 'this' to 'newAddrLabel'.  No
  //	return value.
  public
  void	setAddressLabel			(long newAddrLabel)
  {
    addressLabel_ = newAddrLabel;
  }

  //  IV. Protected methods:
  //  PURPOSE: To print the address prefix of 'this' (if it has one) to
  //	'file'. Prints "\t" otherwise. No return value:
  protected
  void		printAddress		(PrintStream file)
  {
    if (getAddressLabel() == NO_ADDRESS_LABEL)
    {
      file.print('\t');
    }
    else
    {
      file.print("label" + getAddressLabel() + ADDR_LABEL_SUFFIX_CHAR + " :\n\t");
    }
  }

  //  V. Methods that do the main and misc work of class:
  //  PURPOSE: To translate 'this' into assembly in 'text'. No return value.
  public
  void		toText		(PrintStream file)
  {
    printAddress(file);
    file.println(vmOpNameArray[op_.ordinal()]);
  }

  //  V.  Member vars:
  //  PURPOSE: To hold index of the next address label to use.
  protected static
  long				nextAddressLabel_static	= 0;

  //  PURPOSE: To hold virtual machine operation.
  protected
  VmOperation			op_;

  // PURPOSE: To hold the address label of 'this' instruction,
  // or 'NO_ADDRESS_LABEL' if it has none.
  protected
  long				addressLabel_;

}


class VarPotentialInstruction extends PotentialInstruction
{
  //  I.  Constructor(s), assignment op(s), factory(s) and destructor:
  //  PURPOSE: To initialize 'this' to do operation 'newOp' using
  //	'newVar'. No return value.
  public
  VarPotentialInstruction	(VmOperation	newOp,
  				 final Variable newVar
				)
  {
    super(newOp);
    var = newVar;
  }

  //  II. Accessors:
  //  PURPOSE: To return the single variable that 'this' instruction uses.
  public final
  Variable	getVar	()
  {
    return (var);
  }

  //  III. Mutators:

  //  IV. Methods that do the main and misc work of class:
  // PURPOSE: To translate 'this' into assembly in 'text'. No return value.
  @Override
  public
  void		toText		(PrintStream file)
  {
    printAddress(file);
    file.println(vmOpNameArray[getOp().ordinal()]	+ "\t" +
    		 VAR_PREFIX_CHAR + getVar().getNameStr()
		);
    }


  //  V. Member vars:
  //  PURPOSE: To hold the single variable that 'this' instruction uses.
  protected final
  Variable	var;

}

class VarVarPotentialInstruction extends PotentialInstruction
{
  //  I. Constructor(s), assignment op(s), factory(s) and destructor:
  // PURPOSE: To initialize 'this' to do operation 'newOp' using
  // 'newDest' and 'newSrc'. No return value.
  public
  VarVarPotentialInstruction	(VmOperation	newOp,
				 final Variable newDest,
				 final Variable newSrc
				)
  {
    super(newOp);
    dest_ = newDest;
    src0_ = newSrc;
  }

  //  II. Accessors:
  //  PURPOSE: To return the variable that 'this' instruction will change.
  public final
  Variable	getDest		()
  {
    return (dest_);
  }

  // PURPOSE: To return the variable that 'this' instruction uses as a source.
  public final
  Variable	getSrc0		()
  {
    return (src0_);
  }

  //  III.  Mutators:

  //  IV.  Methods that do the main and misc work of class:
  //  PURPOSE: To translate 'this' into assembly in 'text'. No return value.
  @Override
  public
  void		toText		(PrintStream file)
  {
    printAddress(file);
    file.println
	(vmOpNameArray[getOp().ordinal()]	+ "\t"	+
	 VAR_PREFIX_CHAR + getDest().getNameStr() + "," +
	 VAR_PREFIX_CHAR + getSrc0().getNameStr()
	);
  }

  //  V. Member vars:
  //  PURPOSE: To hold the variable that 'this' instruction will change.
  protected final
  Variable			dest_;

  // PURPOSE: To hold the variable that 'this' instruction uses as a source.
  protected final
  Variable			src0_;

}

class VarVarVarPotentialInstruction extends PotentialInstruction
{
  //  I. Constructor(s), assignment op(s), factory(s) and destructor:
  //  PURPOSE: To initialize 'this' to do operation 'newOp' using
  //	'newDest', 'newSrc0', and 'newSrc1'. No return value.
  public
  VarVarVarPotentialInstruction	(VmOperation	newOp,
				 final Variable newDest,
				 final Variable	newSrc0,
				 final Variable	newSrc1
				)
  {
    super(newOp);
    dest_	= newDest;
    src0_	= newSrc0;
    src1_	= newSrc1;
  }

  //  II. Accessors:
  //  PURPOSE: To return the variable that 'this' instruction will change.
  public
  final
  Variable	getDest		()
  {
    return (dest_);
  }

  //  PURPOSE: To return the variable that 'this' instruction uses as a
  //	source.
  public final
  Variable	getSrc0		()
  {
    return (src0_);
  }

  // PURPOSE: To return the variable that 'this' instruction uses as a
  //	source.
  public final
  Variable	getSrc1		()
  {
    return (src1_);
  }

  //  III. Mutators:

  //  IV. Methods that do the main and misc work of class:
  // PURPOSE: To translate 'this' into assembly in 'text'. No return value.
  @Override
  public
  void		toText		(PrintStream file)
  {
    printAddress(file);
    file.println(vmOpNameArray[getOp().ordinal()] +  "\t" +
		 VAR_PREFIX_CHAR + getDest().getNameStr() + "," +
		 VAR_PREFIX_CHAR + getSrc0().getNameStr() + "," +
		 VAR_PREFIX_CHAR + getSrc1().getNameStr()
		);
  }

  //  V.  Member vars:
  //  PURPOSE: To hold the variable that 'this' instruction will change.
  protected final
  Variable			dest_;

  // PURPOSE: To hold the first variable that 'this' instruction uses as a
  //  source.
  protected final
  Variable			src0_;

  // PURPOSE: To hold the second variable that 'this' instruction uses as a
  // source.
  protected final
  Variable			src1_;

}

class AddrPotentialInstruction extends PotentialInstruction
{
  //  I. Constructor(s), assignment op(s), factory(s) and destructor:
  //  PURPOSE: To initialize 'this' to do operation 'newOp' using
  // 'newAddr'. No return value.
  public
  AddrPotentialInstruction	(VmOperation newOp,
  				 long	     newAddr
				)
  {
    super(newOp);
    gotoAddressLabel_ = newAddr;
  }

  //  II. Accessors:
  //  PURPOSE: To return the label of the address to which to go.
  public final
  long		getGotoAddressLabel()
  {
    return (gotoAddressLabel_);
  }

  //  III. Mutators:

  //  IV. Methods that do the main and misc work of class:
  // PURPOSE: To translate 'this' into assembly in 'text'. No return value.
  @Override
  public
  void		toText(PrintStream file)
  {
    printAddress(file);
    file.println
	(vmOpNameArray[getOp().ordinal()] + "\tlabel" +
	 getGotoAddressLabel() + ADDR_LABEL_SUFFIX_CHAR
	);
  }

  //  V. Member vars:
  //  PURPOSE: To hold the label of the address to which to go.
  protected final
  long		gotoAddressLabel_;

}

class VarAddrPotentialInstruction extends PotentialInstruction
{
  //  I.  Constructor(s), assignment op(s), factory(s) and destructor:
  //  PURPOSE: To initialize 'this' to do operation 'newOp' using 'newVar'.
  //	No return value.
  public
  VarAddrPotentialInstruction	(VmOperation	newOp,
				 final Variable	newVar,
				 long  		newAddr
				)
  {
    super(newOp);
    var_		= newVar;
    gotoAddressLabel_	= newAddr;
  }


  //  II. Accessors:
  //  PURPOSE: To return the single variable that 'this' instruction uses.
  public final
  Variable	getVar		()
  {
    return (var_);
  }

  //  PURPOSE: To hold the label of the address to which to go.
  public final
  long		getGotoAddressLabel()
  {
    return (gotoAddressLabel_);
  }

  //  III. Mutators:

  //  IV. Methods that do the main and misc work of class:
  //  PURPOSE: To translate 'this' into assembly in 'text'. No return value.
  @Override
  public
  void		toText		(PrintStream file)
  {
    printAddress(file);
    file.println
	(vmOpNameArray[getOp().ordinal()] + "\t" + VAR_PREFIX_CHAR +
	 getVar().getNameStr() +  ",label" + getGotoAddressLabel() +
	 ADDR_LABEL_SUFFIX_CHAR
	);        
  }
  
  //  V. Member vars:
  //  PURPOSE: To hold the single variable that 'this' instruction uses.
  protected final
  Variable			var_;

  //  PURPOSE: To hold the label of the address to which to go.
  protected final
  long				gotoAddressLabel_;

}

