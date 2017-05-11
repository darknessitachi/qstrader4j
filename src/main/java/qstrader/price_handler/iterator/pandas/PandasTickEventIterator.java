package qstrader.price_handler.iterator.pandas;

import com.abigdreamer.ark.commons.collection.DataTable;

import qstrader.event.TickEvent;
import qstrader.price_handler.iterator.AbstractTickEventIterator;

/**
 *  
 * @author Darkness
 * @date 2016年12月13日 下午5:08:56
 * @version V1.0
 */
public class PandasTickEventIterator {
	public void PandasTickEventIterator(DataTable data, String ticker){
//	    """
//	    PandasTickEventIterator returns a price iterator designed to read
//	    a Pandas DataFrame (or a Pandas Panel) with tick data (bid/ask)
//	    for one (or several) financial instrument and iterate TickEvents.
//	    """
//	    if hasattr(data, 'minor_axis'):
//	        return PandasPanelTickEventIterator(data)
//	    else:
//	        return PandasDataFrameTickEventIterator(data, ticker)
	}
}

class PandasPanelTickEventIterator extends AbstractTickEventIterator {
//  """
//  PandasPanelBarEventIterator is designed to read a Pandas Panel like
//
//  <class 'pandas.core.panel.Panel'>
//  Dimensions: 2 (items) x 20 (major_axis) x 2 (minor_axis)
//  Items axis: Bid to Ask
//  Major_axis axis: 2016-02-01 00:00:01.358000 to 2016-02-01 00:00:14.153000
//  Minor_axis axis: GOOG to MSFT
//
//  with tick data (bid/ask)
//  for several financial instruments and iterate TickEvents.
//  """
  public PandasPanelTickEventIterator(DataTable panel) {
//      this.data = panel
//      this._itr_ticker_bar = this.data.transpose(1, 0, 2).iteritems()
//      this.tickers_lst = this.data.minor_axis
//      this._next_ticker_bar()
  }
  
  public void _next_ticker_bar() {
//      this.index, this.df = next(this._itr_ticker_bar)
//      this._itr_bar = this.df.iteritems()
  }
  
  public TickEvent next() {
//      try:
//          ticker, row = next(this._itr_bar)
//      except StopIteration:
//          this._next_ticker_bar()
//          ticker, row = next(this._itr_bar)
//      bev = this._create_event(this.index, ticker, row)
//      return bev
	  return null;
  }
}

class PandasDataFrameTickEventIterator extends AbstractTickEventIterator {
//  """
//  PandasPanelBarEventIterator is designed to read a Pandas DataFrame like
//
//                                 Bid        Ask
//  Time
//  2016-02-01 00:00:01.358  683.56000  683.58000
//  2016-02-01 00:00:02.544  683.55998  683.58002
//  2016-02-01 00:00:03.765  683.55999  683.58001
//  ...
//  2016-02-01 00:00:10.823  683.56001  683.57999
//  2016-02-01 00:00:12.221  683.56000  683.58000
//  2016-02-01 00:00:13.546  683.56000  683.58000
//
//  with tick data (bid/ask)
//  for one financial instrument and iterate TickEvents.
//  """
  public void PandasDataFrameTickEventIterator(DataTable df,String ticker) {
//      """
//      Takes the the events queue, ticker and Pandas DataFrame
//      """
//      this.data = df
//      this.ticker = ticker
//      this.tickers_lst = [ticker]
//      this._itr_bar = this.data.iterrows()
  }
  
  public TickEvent next() {
//      index, row = next(this._itr_bar)
//      price_event = this._create_event(index, this.ticker, row)
//      return price_event
	  return null;
  }

}
