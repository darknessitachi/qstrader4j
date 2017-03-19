package qstrader.strategy;

import qstrader.event.TickerEvent;

/**
 *  AbstractStrategy is an abstract base class providing an interface for
    all subsequent (inherited) strategy handling objects.

    The goal of a (derived) Strategy object is to generate Signal
    objects for particular symbols based on the inputs of ticks
    generated from a PriceHandler (derived) object.

    This is designed to work both with historic and live data as
    the Strategy object is agnostic to data location.
 *  
 * @author Darkness
 * @date 2016年12月14日 下午3:56:51
 * @version V1.0
 */
public abstract class AbstractStrategy {

	/**
	 * Provides the mechanisms to calculate the list of signals.
	 *  
	 * @author Darkness
	 * @date 2016年12月14日 下午3:56:41
	 * @version V1.0
	 */
    public abstract void calculate_signals(TickerEvent event);
}

