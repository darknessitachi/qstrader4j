package qstrader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;

import com.abigdreamer.ark.commons.collection.Mapx;
import com.sun.star.uno.RuntimeException;

import qstrader.compliance.AbstractCompliance;
import qstrader.compliance.ExampleCompliance;
import qstrader.event.Event;
import qstrader.event.EventType;
import qstrader.event.FillEvent;
import qstrader.event.OrderEvent;
import qstrader.event.SentimentEvent;
import qstrader.event.SignalEvent;
import qstrader.event.TickerEvent;
import qstrader.execution_handler.AbstractExecutionHandler;
import qstrader.execution_handler.IBSimulatedExecutionHandler;
import qstrader.position_sizer.AbstractPositionSizer;
import qstrader.position_sizer.FixedPositionSizer;
import qstrader.price_handler.AbstractPriceHandler;
import qstrader.price_handler.YahooDailyCsvBarPriceHandler;
import qstrader.risk_manager.AbstractRiskManager;
import qstrader.risk_manager.ExampleRiskManager;
import qstrader.sentiment_handler.AbstractSentimentHandler;
import qstrader.statistics.AbstractStatistics;
import qstrader.statistics.SimpleStatistics;
import qstrader.strategy.AbstractStrategy;

/**
 * Enscapsulates the settings and components for
    	carrying out either a backtest or live trading session.
 * @author Darkness
 * @date 2016年12月16日 下午2:28:13
 * @version V1.0
 */
public class TradingSession {
	
	public static class TradingSessionBuilder {
		
		public static TradingSessionBuilder create() {
			return new TradingSessionBuilder();
		}
		
		private AbstractPriceHandler price_handler;
		private AbstractStrategy strategy;
		public TradingSessionBuilder setStrategy(AbstractStrategy strategy) {
			this.strategy = strategy;
			return this;
		}

		public void setCur_time(LocalDateTime cur_time) {
			this.cur_time = cur_time;
		}

		public void setEnd_session_time(LocalDate end_session_time) {
			this.end_session_time = end_session_time;
		}

		private PortfolioHandler portfolio_handler;
		private AbstractExecutionHandler execution_handler;
		private AbstractPositionSizer position_sizer;
		private AbstractRiskManager risk_manager;
		private AbstractStatistics statistics;
		private long equity;
		AbstractCompliance compliance;
		
		Queue<Event> events_queue;
		LocalDateTime cur_time;

		Config config;
		List<String> tickers;
		LocalDate start_date;
		LocalDate end_date;
		LocalDate end_session_time;
		AbstractSentimentHandler sentiment_handler;
		String title;
		Object benchmark;
		String session_type = "backtest";
		
		private TradingSessionBuilder() {
		}
		
		private AbstractPositionSizer position_sizer() {
			if (this.position_sizer == null) {
				this.position_sizer = new FixedPositionSizer();
			}
			return this.position_sizer;
		}
		
		private AbstractRiskManager risk_manager() {
			if (this.risk_manager == null) {
				this.risk_manager = new ExampleRiskManager();
			}
			return this.risk_manager;
		}
		
		private PortfolioHandler portfolio_handler() {
			if (this.portfolio_handler == null) {
				this.portfolio_handler = new PortfolioHandler(
						this.equity,
						this.events_queue,
						this.price_handler,
						this.position_sizer,
						this.risk_manager
	            );
			}
			return portfolio_handler;
		}
		
		private AbstractCompliance compliance() {
			if (this.compliance == null) {
				this.compliance = new ExampleCompliance(config);
			}
			return compliance;
		}
		
		private AbstractExecutionHandler execution_handler() {
			if (this.execution_handler == null) {
				this.execution_handler = new IBSimulatedExecutionHandler(
						this.events_queue,
						this.price_handler,
						this.compliance
		            );
			}
			return execution_handler;
		}
		
		private AbstractStatistics statistics() {
			if (this.statistics == null) {
//				this.statistics = new TearsheetStatistics(this.config, 
//						this.portfolio_handler,
//						this.title, this.benchmark);
				this.statistics = new SimpleStatistics(config, portfolio_handler.getPortfolio());
			}
			return statistics;
		}
		
		public AbstractPriceHandler price_handler() {
			if (this.price_handler == null && this.session_type == "backtest") {
	            this.price_handler = new YahooDailyCsvBarPriceHandler(
	                this.config.CSV_DATA_DIR, this.events_queue,
	                this.tickers, start_date, end_date
	            );
			}
			return price_handler;
		}
		
		public TradingSession build() {
			
			// Use Yahoo Daily Price Handler
			AbstractPriceHandler price_handler = price_handler();
			
			// Use an example Position Sizer
			AbstractPositionSizer position_sizer = position_sizer();
		
			// Use an example Risk Manager
			AbstractRiskManager risk_manager = risk_manager();
		
			// Use the default Portfolio Handler
			PortfolioHandler portfolio_handler = portfolio_handler();
		
			// Use the ExampleCompliance component
			AbstractCompliance compliance = compliance();
		
			// Use a simulated IB Execution Handler
			AbstractExecutionHandler execution_handler = execution_handler();
		
			// Use the default Statistics
			AbstractStatistics statistics = statistics();
			
			TradingSession tradingSession = new TradingSession(config, strategy, tickers, 
					equity, start_date, end_date, events_queue, 
					session_type, end_session_time, price_handler, 
					portfolio_handler, compliance, position_sizer, 
					execution_handler, risk_manager, statistics, 
					sentiment_handler, title, benchmark);
			
			return tradingSession;
		}

		public TradingSessionBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public TradingSessionBuilder setBenchmark(Object benchmark) {
			this.benchmark = benchmark;return this;
		}

		public TradingSessionBuilder setEquity(long equity) {
			this.equity = equity;return this;
		}

		public TradingSessionBuilder setConfig(Config config) {
			this.config = config;return this;
		}

		public TradingSessionBuilder setEvents_queue(Queue<Event> events_queue) {
			this.events_queue = events_queue;return this;
		}

		public TradingSessionBuilder setPrice_handler(AbstractPriceHandler price_handler) {
			this.price_handler = price_handler;return this;
		}

		public TradingSessionBuilder setPortfolio_handler(PortfolioHandler portfolio_handler) {
			this.portfolio_handler = portfolio_handler;return this;
		}

		public TradingSessionBuilder setCompliance(AbstractCompliance compliance) {
			this.compliance = compliance;return this;
		}

		public TradingSessionBuilder setPosition_sizer(AbstractPositionSizer position_sizer) {
			this.position_sizer = position_sizer;return this;
		}

		public TradingSessionBuilder setExecution_handler(AbstractExecutionHandler execution_handler) {
			this.execution_handler = execution_handler;return this;
		}

		public TradingSessionBuilder setRisk_manager(AbstractRiskManager risk_manager) {
			this.risk_manager = risk_manager;return this;
		}

		public TradingSessionBuilder setStatistics(AbstractStatistics statistics) {
			this.statistics = statistics;return this;
		}

		public TradingSessionBuilder setSentiment_handler(AbstractSentimentHandler sentiment_handler) {
			this.sentiment_handler = sentiment_handler;return this;
		}
		
		public TradingSessionBuilder setTickers(List<String> tickers) {
			this.tickers = tickers;return this;
		}

		public TradingSessionBuilder setStart_date(LocalDate start_date) {
			this.start_date = start_date;return this;
		}

		public TradingSessionBuilder setEnd_date(LocalDate end_date) {
			this.end_date = end_date;return this;
		}

		public TradingSessionBuilder setSession_type(String session_type) {
			this.session_type = session_type;return this;
		}
	}
	
	private AbstractPriceHandler price_handler;
	private AbstractStrategy strategy;
	private PortfolioHandler portfolio_handler;
	private AbstractExecutionHandler execution_handler;
	private AbstractPositionSizer position_sizer;
	private AbstractRiskManager risk_manager;
	private AbstractStatistics statistics;
	private long equity;
	AbstractCompliance compliance;
	
	Queue<Event> events_queue;
	LocalDateTime cur_time;

	Config config;
	List<String> tickers;
	LocalDate start_date;
	LocalDate end_date;
	LocalDate end_session_time;
	AbstractSentimentHandler sentiment_handler;
	String title;
	Object benchmark;
	String session_type;
	
	/**
	 * 
	 * @param strategy
	 * @param tickers
	 * @param equity
	 * @param start_date
	 * @param end_date
	 * @param events_queue
	 * @param session_type
	 * @param price_handler
	 * @param portfolio_handler
	 * @param execution_handler
	 * @param position_sizer
	 * @param risk_manager
	 * @param statistics
	 */
	public TradingSession(Config config,
			AbstractStrategy strategy, List<String> tickers, 
			long equity, LocalDate start_date, LocalDate end_date, Queue<Event> events_queue,
			String session_type, LocalDate end_session_time,
			AbstractPriceHandler price_handler, PortfolioHandler portfolio_handler,
			AbstractCompliance compliance, AbstractPositionSizer position_sizer, 
			AbstractExecutionHandler execution_handler, AbstractRiskManager risk_manager, 
			AbstractStatistics statistics, AbstractSentimentHandler sentiment_handler,
			String title, Object benchmark) {
		// Set up the backtest variables according to
		// what has been passed in.
		this.config = config;
		this.strategy = strategy;
		this.tickers = tickers;
		this.equity = equity;
		this.start_date = start_date;
		this.end_date = end_date;
		this.end_session_time = end_session_time;
		this.events_queue = events_queue;
		this.price_handler = price_handler;
		this.portfolio_handler = portfolio_handler;
		this.compliance = compliance;
		this.execution_handler = execution_handler;
		this.position_sizer = position_sizer;
		this.risk_manager = risk_manager;
		this.statistics = statistics;
		this.sentiment_handler = sentiment_handler;
		this.title = title;
		this.benchmark = benchmark;
		this.session_type = session_type;
		this._config_session();
		this.cur_time = null;
		
		if(this.session_type.equals("live")) {
            if(end_session_time == null) {
                throw new RuntimeException("Must specify an end_session_time when live trading");
            }
		}
	}

	/**
	 * Initialises the necessary classes used within the session.
	 */
	public void _config_session() {
		
	}
	
	public boolean _continue_loop_condition(){
        if (this.session_type == "backtest") {
            return this.price_handler.continue_backtest;
        }else{
            return LocalDate.now() .isBefore( this.end_session_time);
        }
	}
            		
	/**
	 * Carries out an infinite while loop that polls the
        events queue and directs each event to either the
        strategy component of the execution handler. The
        loop continue until the event queue has been
        emptied.
	 */
	public void _run_session() {
		if (this.session_type == "backtest") {
			System.out.println("Running Backtest...");
		} else {
			System.out.println("Running Realtime Session until " + this.end_session_time);
		}
        
		while (_continue_loop_condition()) {
			Event event = this.events_queue.poll();
			if (event == null) {
				this.price_handler.stream_next();
			} else {
				if (event.type == EventType.TICK || event.type == EventType.BAR) {
					TickerEvent tickerEvent = (TickerEvent) event;
					this.cur_time = tickerEvent.getTime();
					
					// Generate any sentiment events here
					if (this.sentiment_handler != null) {
						LocalDate stream_date = this.cur_time.toLocalDate();
						this.sentiment_handler.stream_next(stream_date);
					}
					
					this.strategy.calculate_signals(tickerEvent);
					this.portfolio_handler.update_portfolio_value();
					this.statistics.update(tickerEvent.getTime(), this.portfolio_handler.getPortfolio());
				} else if (event.type == EventType.SENTIMENT) {
					this.strategy.calculate_signals((SentimentEvent) event);
				} else if (event.type == EventType.SIGNAL) {
					this.portfolio_handler.on_signal((SignalEvent) event);
				} else if (event.type == EventType.ORDER) {
					this.execution_handler.execute_order((OrderEvent) event);
				} else if (event.type == EventType.FILL) {
					this.portfolio_handler.on_fill((FillEvent) event);
				} else {
					System.out.println(String.format("Unsupported event.type '%s'", event.type));
				}
			}
		}
    }
    
	public Mapx<String, Object> start_trading() {
		return start_trading(false);
	}

	/**
	 * Runs either a backtest or live session, and outputs performance when complete.
	 * @param testing
	 * @return
	 */
	public Mapx<String, Object> start_trading(boolean testing) {
        this._run_session();
        
        Mapx<String, Object> results = this.statistics.get_results();
        System.out.println("---------------------------------");
        System.out.println("Backtest complete.");
        System.out.println(String.format("Sharpe Ratio: %s", results.getString("sharpe")));
//        System.out.println(String.format("Max Drawdown: %s" , results.getString("max_drawdown")));
        System.out.println(String.format("Max Drawdown Pct: %s" , results.getString("max_drawdown_pct")));// * 100
        if (!testing) {
            this.statistics.plot_results();
        }
        
        return results;
   }
}