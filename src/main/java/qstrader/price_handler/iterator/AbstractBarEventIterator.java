package qstrader.price_handler.iterator;

import java.time.LocalDateTime;

import com.abigdreamer.ark.commons.collection.DataRow;

import qstrader.PriceParser;
import qstrader.event.BarEvent;

/**
 *  
 * @author Darkness
 * @date 2016年12月13日 下午4:03:24
 * @version V1.0
 */
public abstract class AbstractBarEventIterator extends AbstractPriceEventIterator {
	
    public BarEvent _create_event(LocalDateTime time,int period,String ticker,DataRow row){
//        """
//        Obtain all elements of the bar from a row of dataframe
//        and return a BarEvent
//        """
//        try:
		long open_price = PriceParser.parse(row.getString("Open"));
		long high_price = PriceParser.parse(row.getString("High"));
		long low_price = PriceParser.parse(row.getString("LowLow"));
		long close_price = PriceParser.parse(row.getString("Close"));
		long adj_close_price = PriceParser.parse(row.getString("Adj Close"));
		long volume = row.getLong("Volume");

//            # Create the tick event for the queue
		BarEvent bev = new BarEvent(ticker, time, period, open_price, high_price, low_price, close_price, volume, adj_close_price);
		return bev;
//        except ValueError:
//            raise EmptyBarEvent("row %s %s %s %s can't be convert to BarEvent" % (index, period, ticker, row))
    }
}