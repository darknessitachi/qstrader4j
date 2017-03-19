package qstrader;

import java.math.BigDecimal;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

/**
 *  PriceParser is designed to abstract away the underlying number used as a price
    within qstrader. Due to efficiency and floating point precision limitations,
    QSTrader uses an integer to represent all prices. This means that $0.10 is,
    internally, 10,000,000. Because such large numbers are rather unwieldy
    for humans, the PriceParser will take "normal" 2dp numbers as input, and show
    "normal" 2dp numbers as output when requested to `display()`

    For consistency's sake, PriceParser should be used for ALL prices that enter
    the qstrader system. Numbers should also always be parsed correctly to view.

 * @author Administrator
 *
 */
public class PriceParser {

//    # 10,000,000
   public static long PRICE_MULTIPLIER = 1000_0000L;

    public static long parse(long x) {//:  # flake8: noqa
        return x;
    }

    public static long parse(String x) {//:  # flake8: noqa
        return (long)(Double.valueOf(x) * PriceParser.PRICE_MULTIPLIER);
    }
    
    public static long parse(double x){//:  # flake8: noqa
        return (long)(x * PriceParser.PRICE_MULTIPLIER);
    }
    
//    """Display Methods. Multiplies a float out into an int if needed."""

    public static double display(long x){//:  # flake8: noqa
    	return round((x + 0.0D) / PriceParser.PRICE_MULTIPLIER, 2);
    }

    public static double display(double x){//  # flake8: noqa
        return round(x, 2);
    }

    public static double display(long x,int dp){//  # flake8: noqa
        return round((x + 0.0D) / PriceParser.PRICE_MULTIPLIER, dp);
    }

    public static double display(double x, int dp){//  # flake8: noqa
        return round(x, dp);
    }
        		
	public static double round(double value, int scale) {
		BigDecimal bd = new BigDecimal(value + "");
		return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}