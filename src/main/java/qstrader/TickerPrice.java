package qstrader;

import com.mchange.io.impl.LazyStringMemoryFileImpl;

public class TickerPrice {

	private long bid;
	private long ask;
	private String timestamp;

	public long getBid() {
		return bid;
	}

	public void setBid(long bid) {
		this.bid = bid;
	}

	public long getAsk() {
		return ask;
	}

	public void setAsk(long ask) {
		this.ask = ask;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
