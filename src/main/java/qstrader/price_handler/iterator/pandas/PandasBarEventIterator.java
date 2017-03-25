package qstrader.price_handler.iterator.pandas;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.abigdreamer.ark.framework.collection.DataRow;
import com.abigdreamer.ark.framework.collection.DataTable;

import qstrader.event.BarEvent;
import qstrader.price_handler.iterator.AbstractBarEventIterator;

/**
 *  
 * @author Darkness
 * @date 2016年12月13日 下午5:04:40
 * @version V1.0
 */
public class PandasBarEventIterator {

	/**
	 * 	PandasBarEventIterator returns a price iterator designed to read
	    a Pandas DataFrame (or a Pandas Panel)
	    with Open-High-Low-Close-Volume (OHLCV) data (bar)
	    for one (or several) financial instrument and iterate BarEvents.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午3:54:18
	 * @version V1.0
	 */
	public static AbstractBarEventIterator createPandasBarEventIterator(DataTable data, int period, String ticker) {
		if (data.containsColumn("minor_axis")) {
			return new PandasPanelBarEventIterator(data, period);
		}
		return new PandasDataFrameBarEventIterator(data, period, ticker);
	}
}

/**
 *  PandasDataFrameBarEventIterator is designed to read a Pandas DataFrame like

                      Open        High         Low       Close    Volume   Adj Close
    Date
    2010-01-04  626.951088  629.511067  624.241073  626.751061   3927000  313.062468
    2010-01-05  627.181073  627.841071  621.541045  623.991055   6031900  311.683844
    2010-01-06  625.861078  625.861078  606.361042  608.261023   7987100  303.826685
    2010-01-07  609.401025  610.001045  592.651008  594.101005  12876600  296.753749
    ...                ...         ...         ...         ...       ...         ...
    2016-07-18  722.710022  736.130005  721.190002  733.780029   1283300  733.780029
    2016-07-19  729.890015  736.989990  729.000000  736.960022   1222600  736.960022
    2016-07-20  737.330017  742.130005  737.099976  741.190002   1278100  741.190002
    2016-07-21  740.359985  741.690002  735.830994  738.630005    969100  738.630005

    [1649 rows x 6 columns]

    with Open-High-Low-Close-Volume (OHLCV) data (bar)
    for one financial instrument and iterate BarEvents.
 *  
 * @author Darkness
 * @date 2016年12月16日 下午3:54:52
 * @version V1.0
 */
class PandasDataFrameBarEventIterator extends AbstractBarEventIterator {
	
	DataTable data;
	int period;
	String ticker;
	List<String> tickers_lst;
	Iterator<DataRow> _itr_bar;
	
	/**
	 * Takes the the events queue, ticker and Pandas DataFrame
	 * @param df
	 * @param period
	 * @param ticker
	 */
    public PandasDataFrameBarEventIterator(DataTable df,int period, String ticker){
        this.data = df;
        this.period = period;
        this.ticker = ticker;
        this.tickers_lst = Arrays.asList(ticker);
        this._itr_bar = this.data.iterator();
    }
    
    public BarEvent next(){
    	if(this._itr_bar.hasNext()) {
	    	DataRow row = this._itr_bar.next();
	        String date = row.getString("Date");
	        
	        BarEvent price_event = this._create_event(LocalDateTime.parse(date), this.period, this.ticker, row);
	        return price_event;
    	} else {
    		BarEvent price_event = this._create_event(LocalDateTime.MIN, this.period, this.ticker, null);
	        return price_event;
    	}
    }

}

/**
 *  PandasPanelBarEventIterator is designed to read a Pandas Panel like

    <class 'pandas.core.panel.Panel'>
    Dimensions: 6 (items) x 1649 (major_axis) x 2 (minor_axis)
    Items axis: Open to Adj Close
    Major_axis axis: 2010-01-04 00:00:00 to 2016-07-21 00:00:00
    Minor_axis axis: GOOG to IBM

    with Open-High-Low-Close-Volume (OHLCV) data (bar)
    for several financial instruments and iterate BarEvents.
 *  
 * @author Darkness
 * @date 2016年12月16日 下午3:55:27
 * @version V1.0
 */
class PandasPanelBarEventIterator extends AbstractBarEventIterator {
	
	DataTable data;
	int period;
	
    public PandasPanelBarEventIterator(DataTable panel,int period){
        this.data = panel;
        this.period = period;
//        this._itr_ticker_bar = this.data.transpose(1, 0, 2).iteritems()
//        this.tickers_lst = this.data.minor_axis
//        this._next_ticker_bar()
    }

    public void _next_ticker_bar(){
//        this.index, this.df = next(this._itr_ticker_bar)
//        this._itr_bar = this.df.iteritems()
    }
    
    public BarEvent next(){
//        try:
//            ticker, row = next(this._itr_bar)
//        except StopIteration:
//            this._next_ticker_bar()
//            ticker, row = next(this._itr_bar)
//        price_event = this._create_event(this.index, this.period, ticker, row)
//        return price_event
    	return null;
    }
}