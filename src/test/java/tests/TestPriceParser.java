package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qstrader.PriceParser;

public class TestPriceParser {

	long longValue;
	double doubleValue ;
	double delta = 0.000000001;
	
	@Before
	public void setUp() {
		longValue = 200;
		doubleValue = 10.1234567D;
	}
	
	@Test
	public void test_price_from_double() {
		long parsed = PriceParser.parse(doubleValue);
		Assert.assertEquals(parsed, 101234567);
	}
	
	@Test
	public void test_price_from_int() {
		long parsed = PriceParser.parse(longValue);
		Assert.assertEquals(parsed, 200);
	}
	
	@Test
	public void test_display() {
		long parsed = PriceParser.parse(doubleValue);
		double displayed = PriceParser.display(parsed);
		Assert.assertEquals(displayed, 10.12D, delta);
	}
	
	@Test
	public void test_unparsed_display() {
		double displayed = PriceParser.display(doubleValue);
		Assert.assertEquals(displayed, 10.12D, delta);
	}
}
