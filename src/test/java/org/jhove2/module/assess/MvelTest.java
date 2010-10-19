package org.jhove2.module.assess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.mvel2.MVEL;

/** 
 * This class is used to test and document know problems with the MVEL library
 * that is used by assessment.  I have added an @Ignore annotation so these
 * tests will not be run during normal times.
 */
@Ignore
public class MvelTest {
	
	@Test
	public void testMvelVersion() {
		assertEquals("2.0.18", MVEL.VERSION);
	}
       
	// This test is failing because of a bug in MVEL which has been reported
    @Test
    public void testIntegerEvaluations() {
    	// Maximum size of an integer is 2147483647 [0x7fffffff]
    	Integer myInteger = new Integer(Integer.MAX_VALUE);
    	assertEquals(2147483647, myInteger.intValue());
    	// However MVEL converts this string to a Long
    	// (see org.mvel2.util.ParseTools.numericTest 
    	// which returns LONG if string length > 9)
    	Object mvelObject = MVEL.eval("2147483647");
    	assertEquals("java.lang.Long", mvelObject.getClass().getName());
    	// Boolean comparison of the Integer object's value 
    	// to the string value returns FALSE !!! WRONG !!!.
    	boolean result = MVEL.evalToBoolean("intValue() == 2147483647", myInteger);
    	assertTrue(result);
    	
//    	Why does the comparison return FALSE?  because...  
//    	a debugging trace shows the following sequence
//    			MVEL.evalToBoolean(String, Object) line: 678
//    			MVEL.eval(String, Object, Class<T>) line: 218
//    			MVELInterpretedRuntime.parse() line: 45
//    			MVELInterpretedRuntime.parseAndExecuteInterpreted() line: 133
//    			MVELInterpretedRuntime(AbstractParser).arithmeticFunctionReduction(int) line: 2473
//    			MVELInterpretedRuntime(AbstractParser).reduce() line: 2508
//    			ExecutionStack.op(int) line: 166
//    			MathProcessor.doOperations(Object, int, Object) line: 45
//    			MathProcessor.doOperations(int, Object, int, int, Object) line: 79
//    			MathProcessor._doOperations(int, Object, int, int, Object) line: 155
//
//    			Here is the full method signature with argument names
//    			MathProcessor._doOperations(int type1, Object val1, int operation, int type2, Object val2)
//
//    			At this point the method argument values are:
//    			    int type1 = 106  (DataTypes.W_INTEGER)
//    			    Object val1 = 2147483647 (java.lang.Integer)
//    			    int operation = 18 (Operator.EQUAL)
//    			    int type2 = 107  (DataTypes.W_LONG)
//    			    Object val2 = 2147483647 (java.lang.Long)
//
//    			The _doOperations method makes the following call:
//    			  return doBigDecimalArithmetic(getInternalNumberFromType(val1, type1),
//    			             operation,
//    			             getInternalNumberFromType(val2, type2), 
//    			             true, box(type2) > box(type1) ? box(type2) : box(type1));
//
//    			Contained in the above are 2 calls to getInternalNumberFromType(Object, int)
//    			The first of these has the argument values:
//    			    Object in = 2147483647 (java.lang.Integer)
//    			    int type = 106  (DataTypes.W_INTEGER)
//
//    			Which causes the following constructor call to be made:
//    			    return new InternalNumber((Integer) in, MathContext.DECIMAL32);
//    			 
//    			InternalNumber calls its super class constructor:
//    			    public BigDecimal(int val, MathContext mc) {
//    			        intCompact = val;
//    			        if (mc.precision > 0)
//    			            roundThis(mc);
//    			    }
//
//    			The value of mc.precision is 7 for MathContext.DECIMAL32
//
//    			Therefore there is a call to BigDecimal.roundThis(MathContext mc),
//    			which in turn calls BigDecimal.doRound(BigDecimal d, MathContext mc),
//    			which sets:
//    			    intVal	= null
//    			    intCompact	= 2147484
//    			    scale	= -3
//    			    precision	= 10
//
//    			What the hell is going on here?  Why is the integer being manipulated in this way?
//
//    			The second call to getInternalNumberFromType results in the following:
//    			    return new InternalNumber((Long) in, MathContext.DECIMAL64);
//
//    			The value of mc.precision is 16 for MathContext.DECIMAL64
//    			which yields the following settings for the BigDecimal that is created:
//    			    intVal	= null
//    			    intCompact	= 2147483647
//    			    scale	= 0
//    			    precision	= 10
//
//    			Now that the BigDecimals have been created, MVEL can finally enter the 
//    			MathProcessor.doBigDecimalArithmetic method which compares like this:
//    			   case EQUAL:
//    			      return val1.compareTo(val2) == 0 ? Boolean.TRUE : Boolean.FALSE;
//
//    			I am having trouble following the logic at this point, 
//    			but the bottom line is that the comparison returns FALSE,
//    			which is totally unexpected.
    }

    @Test
    public void testCompareLong() {
    	// Create a Long object
    	Long myLong = new Long(987654321);
    	// MVEL can correctly retrieve the long value
    	Object mvelValue = MVEL.eval("longValue()",myLong);
    	assertEquals(myLong.longValue(), ((Long) mvelValue).longValue());
    	// MVEL correctly compares value if long constant is explicitly typed with "L" 
    	boolean result1 = MVEL.evalToBoolean("longValue() == 987654321L", myLong);
    	assertTrue(result1);
    	// comparison fails if the constant is not explicitly typed
    	boolean result2 = MVEL.evalToBoolean("longValue() == 987654321", myLong);
    	assertFalse(result2);
    }
    
    @Test
    public void testIsdef() {
    	// Create a Long object
    	Long myLong = new Long(987654321);
    	// The following statement throws an exception
    	boolean result = MVEL.evalToBoolean("isdef longValue()", myLong);
    	assertTrue(result); 
    	
    	/* 
    	This is the sequence of calls being made
    	
    	MVEL.evalToBoolean(String, Object) line: 678	
		MVEL.eval(String, Object, Class<T>) line: 218	
		MVELInterpretedRuntime.parse() line: 45	
		MVELInterpretedRuntime.parseAndExecuteInterpreted() line: 94	
		IsDef.getReducedValue(Object, Object, VariableResolverFactory) line: 34	
		
		Here is the getReducedValue method:
		public Object getReducedValue(Object ctx, Object thisValue, VariableResolverFactory factory) {
           return factory.isResolveable(nameCache) || (thisValue != null && getFieldOrAccessor(thisValue.getClass(), nameCache) != null);
        }
        
        A NullPointerException is being raised because the factory variable is null

    	 */
    }

}
