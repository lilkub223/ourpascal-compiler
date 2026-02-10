/*-------------------------------------------------------------------------*
 *---                                                                   ---*
 *---        Value.java                                                 ---*
 *---                                                                   ---*
 *---    This file declares classes that represent computed values.     ---*
 *---                                                                   ---*
 *---    ----    ----    ----    ----    ----    ----    ----    ----   ---*
 *---                                                                   ---*
 *---    Version 1a        2025 May 26       Joseph Phillips            ---*
 *---                                                                   ---*
 *-------------------------------------------------------------------------*/

public abstract
class Value
{
  //  I.   Constructor(s):
  //  PURPOSE: To initialize 'this'. No parameters. No return value.
  public
  Value				()
  { }

  //  II. Accessor(s):
  //  PURPOSE: To tell the type of 'this'.
  public abstract
  Type		 getType	();

  //  PURPOSE: To return the boolean value, or throw an exception if
  //	'this' is not a boolean value.
  public
  boolean	getBoolean	()
  {
    throw new UnsupportedOperationException("Value is not a boolean");
  }

  //  PURPOSE: To return the integer value, or throw an exception if
  //	'this' is not an integer value.
  public
  long		getInteger	()
  {
    throw new UnsupportedOperationException("Value is not an integer");
  }

  //  PURPOSE: To return the real value, or throw an exception if
  //	'this' is not a real value.
  public
  double	getReal		()
  {
    throw new UnsupportedOperationException("Value is not a real");
  }

  //  PURPOSE: To return the numeric value, or throw an exception if
  //	'this' is not a numeric value.
  public
  Type		getNumber	(long[]		integer,
  				 double[]	real
				)
  {
    throw new UnsupportedOperationException("Value is not numeric");
  }

  //  PURPOSE: To return the string, or throw an exception if
  //	'this' is not a boolean value.
  public
  String	getString	()
  {
    throw new UnsupportedOperationException("Value is not a string");
  }

  //  III. Mutator(s):


  //  IV. Methods that do main and misc. work of class:
  //  PURPOSE: To return a copy of 'this'. No parameters.
  public abstract
  Value		copy		();

  // PURPOSE: To print 'this' value to stdout. No parameters. No return
  // value.
  public abstract
  void		print		();


  //  V. Member vars:
}


class	BooleanValue extends Value
{
  //  I.  Constructor(s):
  //  PURPOSE: To initialize 'this' to represent boolean 'newBoolean'.
  //	No return value.
  public
  BooleanValue	(boolean newBoolean)
  {
    super();
    this.value_ = newBoolean;
  }

  //  II. Accessor(s):
  // PURPOSE: To tell the type of 'this'.
  @Override
  public
  Type		getType		()
  {
    return Type.BOOLEAN;
  }

  // PURPOSE: To return the boolean value, or throw an exception if
  // 'this' is not a boolean value.
  @Override
  public
  boolean	getBoolean	()
  {
    return value_;
  }

  //  III. Mutator(s): (None)

  //  IV.  Methods that do main and misc. work of class:
  //  PURPOSE: To return a copy of 'this'. No parameters.
  @Override
  public
  Value		copy		()
  {
    return new BooleanValue(getBoolean());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return
  //	value.
  @Override
  public
  String	toString	()
  {
    return(getBoolean() ? "true" : "false");
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return
  //	value.
  @Override
  public
  void		print		()
  {
    System.out.print(toString());
  }

  //  V. Member vars:
  // PURPOSE: To hold the boolean.
  private
  boolean			value_;

}


class	IntegerValue extends Value
{
  //  I. Constructor(s):
  //  PURPOSE: To initialize 'this' to represent integer 'newInteger'.
  //	No return value.
  public
  IntegerValue			(long	newInteger)
  {
    super();
    this.integer_	 = newInteger;
  }


  //  II.  Accessor(s):
  //  PURPOSE: To tell the type of 'this'.
  @Override
  public
  Type		getType		()
  {
    return Type.INTEGER;
  }

  // PURPOSE: To return the integer value, or throw an exception if
  // 'this' is not an integer value.
  @Override
  public
  long	getInteger	()
  {
    return integer_;
  }

  //  PURPOSE: To return the real value, or throw an exception if
  //	'this' is not a real value.
  @Override
  public
  double	getReal		()
  {
    return (double) getInteger();
  }

  //  PURPOSE: To return the numeric value, or throw an exception if
  //	'this' is not a numeric value.
  @Override
  public
  Type		getNumber	(long[]		integerArr,
  				 double[]	realArr
				)
  {
    integerArr[0] = getInteger();
    return Type.INTEGER;
  }

  //  III. Mutator(s): (None)

  //  IV.   Methods that do main and misc. work of class:
  //  PURPOSE: To return a copy of 'this'. No parameters.
  @Override
  public
  Value		copy		()
  {
    return new IntegerValue(getInteger());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return value.
  @Override
  public
  String	toString	()
  {
    return("" + getInteger());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return value.
  @Override
  public
  void		print		()
  {
    System.out.print(getInteger());
  }

  //  V.  Member vars:
  //  PURPOSE: To hold the integer.
  private
  long				integer_;
}


class RealValue extends Value
{
  //  I.  Constructor(s):
  //  PURPOSE: To initialize 'this' to represent real 'newReal'.
  //	No return value.
  public
  RealValue			(double	newReal)
  {
    super();
    this.real_	= newReal;
  }

  //  II. Accessor(s):
  //  PURPOSE: To tell the type of 'this'.
  @Override
  public
  Type		getType		()
  {
    return Type.REAL;
  }

  //  PURPOSE: To return the real value, or throw an exception if
  //	'this' is not a real value.
  @Override
  public
  double	getReal		()
  {
    return real_;
  }

  //  PURPOSE: To return the numeric value, or throw an exception if 'this' is
  //	not a numeric value.
  @Override
  public
  Type		getNumber	(long[]		integerArr,
  				 double[]	realArr
				)
  {
    realArr[0]	= getReal();
    return Type.REAL;
  }

  //  III. Mutator(s): (None)

  //  IV.  Methods that do main and misc. work of class:
  //   PURPOSE: To return a copy of 'this'. No parameters.
  @Override
  public
  Value		copy		()
  {
    return new RealValue(getReal());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return value.
  @Override
  public
  String	toString	()
  {
    return("" + getReal());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return value.
  @Override
  public
  void		print		()
  {
    System.out.print(toString());
  }

  //  V.  Member vars:
  //  PURPOSE: To hold the real.
  private
  double			real_;

}


class	StringValue extends Value
{
  //  I.  Constructor(s):
  //  PURPOSE: To initialize 'this' to represent string 'newString'.
  //	No return value.
  public
  StringValue			(String newString)
  {
    super();
    this.string_	= newString;
  }

  //  II. Accessor(s):
  //  PURPOSE: To tell the type of 'this'.
  @Override
  public
  Type		getType		()
  {
    return Type.STRING;
  }

  //  PURPOSE: To return the string.
  @Override
  public
  String	getString	()
  {
    return string_;
  }

  //  III. Mutator(s):

  //  IV.  Methods that do main and misc. work of class:
  //  PURPOSE: To return a copy of 'this'. No parameters.
  @Override
  public
  Value		copy		()
  {
    return new StringValue(getString());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return value.
  @Override
  public
  void		print		()
  {
    System.out.print(getString());
  }

  //  PURPOSE: To print 'this' value to stdout. No parameters. No return value.
  @Override
  public
  String	toString	()
  {
    return(getString());
  }

  //  V. Member vars:
  //  PURPOSE: To hold the string.
  private
  String			string_;

}
