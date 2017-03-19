package qstrader.price_handler;

/**
 *  
 * @author Darkness
 * @date 2016年12月14日 下午1:24:10
 * @version V1.0
 */
public class TickerPrices {
	public String close;
	public String adj_close;
	public String timestamp;
	
	public TickerPrices(String close,String adj_close,String timestamp) {
		 this.close = close;
		 this.adj_close = adj_close;
		 this.timestamp = timestamp;
	}

}
