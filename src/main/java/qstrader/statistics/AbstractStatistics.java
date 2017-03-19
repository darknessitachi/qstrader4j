package qstrader.statistics;

import com.github.rapidark.framework.collection.Mapx;

import qstrader.PortfolioHandler;

/**
 *  统计
 *  Statistics is an abstract class providing an interface for
    all inherited statistic classes (live, historic, custom, etc).

    The goal of a Statistics object is to keep a record of useful
    information about one or many trading strategies as the strategy
    is running. This is done by hooking into the main event loop and
    essentially updating the object according to portfolio performance
    over time.

    Ideally, Statistics should be subclassed according to the strategies
    and timeframes-traded by the user. Different trading strategies
    may require different metrics or frequencies-of-metrics to be updated,
    however the example given is suitable for longer timeframes.
 * @author darkness
 *
 */
public interface AbstractStatistics {

	/**
	 *  Update all the statistics according to values of the portfolio
        and open positions. This should be called from within the
        event loop.
	 *  
	 * @author Darkness
	 * @date 2016年12月16日 下午3:05:54
	 * @version V1.0
	 */
    void update(String time, PortfolioHandler portfolio_handler);

    /**
     * Return a dict containing all statistics.
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:06:17
     * @version V1.0
     */
    Mapx<String, Object> get_results();

    /**
     * Plot all statistics collected up until 'now'
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:06:28
     * @version V1.0
     */
    void plot_results();

    /**
     * Save statistics results to filename
     *  
     * @author Darkness
     * @date 2016年12月16日 下午3:06:40
     * @version V1.0
     */
    void save(String filename);

//    public void load(cls, filename):
//        with open(filename, 'rb') as fd:
//            stats = pickle.load(fd)
//        return stats


//public void load(filename):
//    return AbstractStatistics.load(filename)
}