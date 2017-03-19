package qstrader.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.rapidark.framework.collection.Mapx;

import qstrader.PortfolioHandler;
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

	List<String> timeseries;
	List<Double> equity;
	List<Double> equity_returns;
	List<Double> hwm;
	List<Double> drawdowns;
	
    public SimpleStatistics(Object config, PortfolioHandler portfolio_handler) {
//        """
//        Takes in a portfolio handler.
//        """
//        this.config = config
//        this.drawdowns = [0]
//        this.equity = new ArrayList<>();
//        this.equity_returns = [0.0];
//////        Initialize timeseries. Correct timestamp not available yet.
//        this.timeseries = Arrays.asList("0000-00-00 00:00:00");
//////        Initialize in order for first-step calculations to be correct.
//        current_equity = PriceParser.display(portfolio_handler.portfolio.equity);
//        this.hwm = [current_equity];
//        this.equity.append(current_equity);
    }
    
    /**
     * Update all statistics that must be tracked over time.
     */
    public void update( String timestamp, PortfolioHandler portfolio_handler){
		if (!timestamp.equals(this.timeseries.get(this.timeseries.size()))) {
			// Retrieve equity value of Portfolio
			String current_equity = PriceParser.display(portfolio_handler.getPortfolio().getEquity());
			this.equity.add(Double.valueOf(current_equity));
			this.timeseries.add(timestamp);

			// Calculate percentage return between current and previous equity value.
			double today = this.equity.get(this.equity.size() - 1);
			double yesterday = this.equity.get(this.equity.size() - 2);
			double pct = ((today - yesterday) / today) * 100;
			this.equity_returns.add(pct);
			// Calculate Drawdown.
			this.hwm.add(Math.max(this.hwm.get(this.hwm.size() - 1), this.equity.get(equity.size() - 1)));
			this.drawdowns.add(this.hwm.get(this.hwm.size() - 1) - this.equity.get(equity.size() - 1));
		}
    }
    
    /**
     * Return a dict with all important results & stats.
     */
    public Mapx<String, Object> get_results(){
////        Modify timeseries in local scope only. We initialize with 0-date,
////        but would rather show a realistic starting date.
//        timeseries = this.timeseries;
//        timeseries[0] = pd.to_datetime(timeseries[1]) - pd.Timedelta(days=1)
//
//        Mapx<String, Object> statistics = new Mapx<>();
//        statistics.put("sharpe", this.calculate_sharpe());
////        statistics.put("drawdowns", this.calculate_sharpe());
//////        statistics["drawdowns"] = pd.Series(this.drawdowns, index=timeseries)
////        statistics.put("max_drawdown", Math.max(this.drawdowns));
////        statistics.put("max_drawdown_pct", this.calculate_max_drawdown_pct());
////        statistics.put("equity", this.calculate_max_drawdown_pct());
//////        statistics["equity"] = pd.Series(this.equity, index=timeseries)
//////        statistics["equity_returns"] = pd.Series(this.equity_returns, index=timeseries)
////        statistics.put("equity_returns", this.calculate_max_drawdown_pct());
//        return statistics;
    	return null;
    }
    
    /**
     * Calculate the sharpe ratio of our equity_returns.

       Expects benchmark_return to be, for example, 0.01 for 1%
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:48:40
     * @version V1.0
     */
    public void calculate_sharpe( ){
    	calculate_sharpe(0.00);
    }
    
    public void calculate_sharpe(double benchmark_return){
//        excess_returns = pd.Series(this.equity_returns) - benchmark_return / 252;
//
//        // Return the annualised Sharpe ratio based on the excess daily returns
//        return round(this.annualised_sharpe(excess_returns), 4)
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
    public void annualised_sharpe(Object returns){
    	annualised_sharpe(returns, 252);
    }
    public void annualised_sharpe(Object returns,int N){
//        return np.sqrt(N) * returns.mean() / returns.std()
    }
    
    /**
     * Calculate the percentage drop related to the "worst" drawdown seen.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:49:42
     * @version V1.0
     */
    public void calculate_max_drawdown_pct(){
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
//        sns.set_palette("deep", desat=.6)
//        sns.set_context(rc={"figure.figsize": (8, 4)})
//
//        Plot two charts: Equity curve, period returns
//        fig = plt.figure()
//        fig.patch.set_facecolor('white')
//
//        df = pd.DataFrame()
//        df["equity"] = pd.Series(this.equity, index=this.timeseries)
//        df["equity_returns"] = pd.Series(this.equity_returns, index=this.timeseries)
//        df["drawdowns"] = pd.Series(this.drawdowns, index=this.timeseries)
//
//        Plot the equity curve
//        ax1 = fig.add_subplot(311, ylabel='Equity Value')
//        df["equity"].plot(ax=ax1, color=sns.color_palette()[0])
//
//        Plot the returns
//        ax2 = fig.add_subplot(312, ylabel='Equity Returns')
//        df['equity_returns'].plot(ax=ax2, color=sns.color_palette()[1])
//
//        drawdown, max_dd, dd_duration = this.create_drawdowns(df["Equity"])
//        ax3 = fig.add_subplot(313, ylabel='Drawdowns')
//        df['drawdowns'].plot(ax=ax3, color=sns.color_palette()[2])
//
//        Rotate dates
//        fig.autofmt_xdate()
//
//        Plot the figure
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
}
