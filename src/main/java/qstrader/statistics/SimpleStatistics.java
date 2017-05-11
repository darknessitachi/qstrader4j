package qstrader.statistics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.abigdreamer.ark.commons.collection.Mapx;
import com.abigdreamer.ark.commons.collection.TwoTuple;

import qstrader.Config;
import qstrader.Portfolio;
import qstrader.PriceParser;

/**
 *  简单的统计
 *  包含： 夏普比率，回撤
 *  投资绩效分析指标：最大回撤（MaxDrawDown），最大回撤持续期（MaxDrawDownDuration）
 *  
 *  Simple Statistics provides a bare-bones example of statistics
    that can be collected through trading.

    Statistics included are Sharpe Ratio, Drawdown, Max Drawdown,
    Max Drawdown Duration.

    TODO think about Alpha/Beta, compare strategy of benchmark.
    TODO think about speed -- will be bad doing for every tick
    on anything that trades sub-minute.
    TODO think about slippage, fill rate, etc
    TODO brokerage costs?

    TODO need some kind of trading-frequency parameter in setup.
    Sharpe calculations need to know if daily, hourly, minutely, etc.
    
 * @author darkness
 *
 */
public class SimpleStatistics implements AbstractStatistics {

	List<LocalDateTime> timeseries;
	List<Double> equity;
	List<Double> equity_returns;
	List<Double> hwm;
	List<Double> drawdowns;
	private Config config;
	
	/**
	 * Takes in a portfolio handler.
	 * @param config
	 * @param portfolio_handler
	 */
    public SimpleStatistics(Config config, Portfolio portfolio) {
		this.config = config;
		this.drawdowns = new ArrayList<>();
		this.drawdowns.add(0.0);
		
		this.equity = new ArrayList<>();
		this.equity_returns = new ArrayList<>();
		this.equity_returns.add(0.0);
		
		// Initialize timeseries. Correct timestamp not available yet.
		this.timeseries = new ArrayList<>();
		this.timeseries.add(LocalDateTime.MIN);
		
		// Initialize in order for first-step calculations to be correct.
		double current_equity = PriceParser.display(portfolio.getEquity());
		this.hwm = new ArrayList<>();
		this.hwm.add(current_equity);
		
		this.equity.add(current_equity);
    }
    
    /**
     * Update all statistics that must be tracked over time.
     */
	public void update(LocalDateTime timestamp, Portfolio portfolio) {
		if (!timestamp.equals(this.timeseries.get(this.timeseries.size()-1))) {
			// Retrieve equity value of Portfolio
			double current_equity = PriceParser.display(portfolio.getEquity());
			this.equity.add(Double.valueOf(current_equity));
			this.timeseries.add(timestamp);

			// Calculate percentage return between current and previous equity value.
			double today = this.equity.get(this.equity.size() - 1);
			double yesterday = this.equity.get(this.equity.size() - 2);
			double pct = ((today - yesterday) / today) * 100;
			this.equity_returns.add(PriceParser.round(pct, 4));
			// Calculate Drawdown.
			this.hwm.add(Math.max(this.hwm.get(this.hwm.size() - 1), this.equity.get(equity.size() - 1)));
			this.drawdowns.add(this.hwm.get(this.hwm.size() - 1) - this.equity.get(equity.size() - 1));
		}
    }
    
    /**
     * Return a dict with all important results & stats.
     */
    public Mapx<String, Object> get_results(){
//        Modify timeseries in local scope only. We initialize with 0-date,
//        but would rather show a realistic starting date.
//        timeseries = this.timeseries;
//        timeseries[0] = pd.to_datetime(timeseries[1]) - pd.Timedelta(days=1)
//
        Mapx<String, Object> statistics = new Mapx<>();
        statistics.put("sharpe", this.calculate_sharpe());
////        statistics.put("drawdowns", this.calculate_sharpe());
//////        statistics["drawdowns"] = pd.Series(this.drawdowns, index=timeseries)
        statistics.put("max_drawdown", max(this.drawdowns).first);
        statistics.put("max_drawdown_pct", this.calculate_max_drawdown_pct());
////        statistics.put("equity", this.calculate_max_drawdown_pct());
//////        statistics["equity"] = pd.Series(this.equity, index=timeseries)
//////        statistics["equity_returns"] = pd.Series(this.equity_returns, index=timeseries)
////        statistics.put("equity_returns", this.calculate_max_drawdown_pct());
//        return statistics;
    	return statistics;
    }
    
    public TwoTuple<Double, Integer> max(List<Double> values) {
    	double max = Double.MIN_VALUE;
    	int maxIndex = -1;
    	int i = 0;
    	for (Double value : values) {
			if(value > max) {
				max = value;
				maxIndex = i;
			}
			i++;
		}
    	return new TwoTuple<>(max, maxIndex);
    }
    
    /**
     * Calculate the sharpe ratio of our equity_returns.

       Expects benchmark_return to be, for example, 0.01 for 1%
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:48:40
     * @version V1.0
     */
    public double calculate_sharpe( ){
    	double riskFreeReturn = 0.00;
    	return calculate_sharpe(riskFreeReturn);
    }
    
    public double calculate_sharpe(double benchmark_return){
//        excess_returns = pd.Series(this.equity_returns) - benchmark_return / 252;
//
//        // Return the annualised Sharpe ratio based on the excess daily returns
//        return round(this.annualised_sharpe(excess_returns), 4)
    	return annualised_sharpe(this.equity_returns, benchmark_return);
    }
    
    /**
     * Calculate the annualised Sharpe ratio of a returns stream
        based on a number of trading periods, N. N public voidaults to 252,
        which then assumes a stream of daily returns.

        The function assumes that the returns are the excess of
        those compared to a benchmark.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:49:26
     * @version V1.0
     */
    public double annualised_sharpe(List<Double> returns, double benchmark_return){
    	return sharpeRatio(returns, 252, benchmark_return);
    }
    
    public void annualised_sharpe(Object returns,int N){
//        return np.sqrt(N) * returns.mean() / returns.std()
    }
    
    public double sharpeRatio (List<Double> returns, int N, double benchmark_return) {
    	DescriptiveStatistics stats = new DescriptiveStatistics();
        for( double item : returns) {
            stats.addValue(item - (benchmark_return/N));
        }

        double mean = stats.getMean();

        double std = stats.getStandardDeviation();

		double sharpeRatio = (mean) / std * Math.sqrt(N);

        return sharpeRatio;
    }
    
    /**
     * Calculate the percentage drop related to the "worst" drawdown seen.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:49:42
     * @version V1.0
     */
    public double calculate_max_drawdown_pct(){
    	int maxDrawdownsIndex = max(this.drawdowns).second;
    	int topIndex = max(this.equity.subList(0, maxDrawdownsIndex+1)).second;
    	
    	double pct = (equity.get(topIndex) - equity.get(maxDrawdownsIndex)) / equity.get(topIndex) *100;
    	return PriceParser.round(pct, 4);
//        drawdown_series = pd.Series(this.drawdowns)
//        equity_series = pd.Series(this.equity)
//        bottom_index = drawdown_series.idxmax()
//        try:
//            top_index = equity_series[:bottom_index].idxmax()
//            pct = (
//                (equity_series.ix[top_index] - equity_series.ix[bottom_index]) /
//                equity_series.ix[top_index] * 100
//            )
//            return round(pct, 4)
//        except ValueError:
//            return np.nan
    }
    
    /**
     * A simple script to plot the balance of the portfolio, or "equity curve", as a function of time.
     */
    public void plot_results(){
//    	sns.set_palette("deep", desat=.6)
//        sns.set_context(rc={"figure.figsize": (8, 4)})
//
//        # Plot two charts: Equity curve, period returns
//        fig = plt.figure()
//        fig.patch.set_facecolor('white')
//
//        df = pd.DataFrame()
//        df["equity"] = pd.Series(self.equity, index=self.timeseries)
//        df["equity_returns"] = pd.Series(self.equity_returns, index=self.timeseries)
//        df["drawdowns"] = pd.Series(self.drawdowns, index=self.timeseries)
//
//        # Plot the equity curve
//        ax1 = fig.add_subplot(311, ylabel='Equity Value')
//        df["equity"].plot(ax=ax1, color=sns.color_palette()[0])
//
//        # Plot the returns
//        ax2 = fig.add_subplot(312, ylabel='Equity Returns')
//        df['equity_returns'].plot(ax=ax2, color=sns.color_palette()[1])
//
//        # drawdown, max_dd, dd_duration = self.create_drawdowns(df["Equity"])
//        ax3 = fig.add_subplot(313, ylabel='Drawdowns')
//        df['drawdowns'].plot(ax=ax3, color=sns.color_palette()[2])
//
//        # Rotate dates
//        fig.autofmt_xdate()
//
//        # Plot the figure
//        plt.show()
}

    public void get_filename(String filename){
//        if filename == "":
//            now = datetime.datetime.utcnow()
//            filename = "statistics_" + now.strftime("%Y-%m-%d_%H%M%S") + ".pkl"
//            filename = os.path.expanduser(os.path.join(this.config.OUTPUT_DIR, filename))
//        return filename;
     }

    public void save(String filename){
//        filename = this.get_filename(filename)
//        print("Save results to '%s'" % filename)
//        with open(filename, 'wb') as fd:
//            pickle.dump(self, fd)
    }
    
    public List<LocalDateTime> getTimeseries() {
		return timeseries;
	}

	public List<Double> getEquity() {
		return equity;
	}

	public List<Double> getEquity_returns() {
		return equity_returns;
	}

	public List<Double> getHwm() {
		return hwm;
	}

	public List<Double> getDrawdowns() {
		return drawdowns;
	}

	public Config getConfig() {
		return config;
	}
}
