/*-------------------------------------------------------------------------*
 *---                                                                   ---*
 *---        Variable.java                                              ---*
 *---                                                                   ---*
 *---    This file declares classes that represent Variables.           ---*
 *---                                                                   ---*
 *---    ----    ----    ----    ----    ----    ----    ----    ----   ---*
 *---                                                                   ---*
 *---    Version 1a        2025 May 31       Joseph Phillips            ---*
 *---                                                                   ---*
 *-------------------------------------------------------------------------*/

import java.util.Objects; // Import for Objects.requireNonNull

public class Variable
{
  //  I.  Constructor(s):
  //  PURPOSE: To initialize 'this' to be named 'newNameStr' and have type
  //	'newType'.
  public	Variable	(String newNameStr)
  {
    this.name_	= newNameStr;
    this.type_	= Type.NONE;
    this.value_ = null;
  }

  //  PURPOSE: To initialize 'this' to be named 'newNameStr' and have type
  //	'newType'.
  public	Variable	(String	newNameStr,
		  		 Value	value
		  		)
  {
    this.name_	= newNameStr;
    this.type_	= value.getType();
    this.value_ = value;
  }

  //  II.  Accessors:
  //  PURPOSE: To hold the name of the Variable.
  public
  String	getNameStr	()
  {
    return name_;
  }

  //  PURPOSE: To return the type of the variable.
  public
  Type		getType		()
  {
    return type_;
  }

  //  PURPOSE: To return the value.
  public
  Value		getValue	()
  {
    return value_.copy();
  }

  //  III. Mutators:
  //  PURPOSE: To set the type of 'this' variable to 'type'.
  public
  void		setType		(Type type)
  {
    this.type_ = type;

    switch (type_)
    {
    case BOOLEAN:
      value_	= new BooleanValue(false);
      break;
    case INTEGER:
      value_ = new IntegerValue(0);
      break;
    case REAL:
      value_ = new RealValue(0.0);
      break;
    case STRING:
      value_ = new StringValue("");
      break;
    default:
      throw new IllegalArgumentException
			("Attempt to define variable to"	+
			 " have a non-supported type");
    }
  }

  //  PURPOSE: To set the value.
  public
  void		setValue	(Value newValue)
  {
    if (newValue.getType() != getType())
    {
      throw new IllegalArgumentException
			("Attempt to assign variable"	+
			 " a non-compatible value"
			);
    }

    value_	= newValue;
  }

  //  V. Member vars:
  //  PURPOSE: To hold the name of the Variable.
  private
  String			name_;

  //  PURPOSE: To hold the type of the variable.
  private
  Type				type_;

  //  PURPOSE: To address of the current value.
  private
  Value				value_;

}
