package qstrader.compliance;

import qstrader.event.FillEvent;

/**
 *  
 *  The Compliance component should be given every trade
    that occurs in qstrader.

    It is designed to keep track of anything that may
    be required for regulatory or audit (or debugging)
    purposes. Extended versions can write trades to a
    CSV, or a database.

 * @author darkness
 *
 */
public interface AbstractCompliance {
	
	/**
	 *  Takes a FillEvent from an ExecutionHandler
        and logs each of these.

	 * @param fill - A FillEvent with information about the
        trade that has just been executed.
	 */
    void record_trade(FillEvent fill);
}