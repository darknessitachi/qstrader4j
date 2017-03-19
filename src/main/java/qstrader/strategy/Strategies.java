package qstrader.strategy;

import java.util.Arrays;
import java.util.List;

import qstrader.event.TickerEvent;

/**
 * Strategies is a collection of strategy
 * 
 * @author Darkness
 * @date 2016年12月13日 上午11:16:04
 * @version V1.0
 */
public class Strategies extends AbstractStrategy {
	List<AbstractStrategy> _lst_strategies;
	
    public Strategies(AbstractStrategy...strategies) {
        this._lst_strategies = Arrays.asList(strategies);
    }
    
	@Override
	public void calculate_signals(TickerEvent event) {
		for (AbstractStrategy strategy : this._lst_strategies) {
			strategy.calculate_signals(event);
		}
	}
}
