package qstrader.compliance;

import java.io.File;
import java.time.LocalDate;

import com.github.rapidark.framework.utility.FileUtil;
import com.google.common.base.Joiner;

import qstrader.Config;
import qstrader.event.FillEvent;

/**
 *  A basic compliance module which writes trades to a
    CSV file in the output directory.
 * @author darkness
 *
 */
public class ExampleCompliance implements AbstractCompliance {

	String csv_filename;

	/**
	 *  Wipe the existing trade log for the day, leaving only
        the headers in an empty CSV.

        It allows for multiple backtests to be run
        in a simple way, but quite likely makes it unsuitable for
        a production environment that requires strict record-keeping.

	 * @param config
	 */
    public ExampleCompliance(String dir) {
        this.csv_filename = dir + File.separator + "tradelog_" + LocalDate.now() + ".csv";

        FileUtil.mkdir(dir);

		// Remove the previous CSV file
		FileUtil.delete(this.csv_filename);
        
		// Write new file header
		String header = join("timestamp", "ticker", "action", "quantity", "exchange", "price", "commission");
        FileUtil.writeText(this.csv_filename, header);
    }

    public ExampleCompliance(Config config) {
		// TODO Auto-generated constructor stub
	}

	/**
     * Append all details about the FillEvent to the CSV trade log.
     */
	public void record_trade(FillEvent fill) {
		String row = join(fill.getTimestamp(), fill.getTicker(), fill.getAction(), fill.getQuantity(), fill.getExchange(), fill.getPrice(), fill.getCommission());
		FileUtil.writeText(this.csv_filename, row, true);
	}
	
	private String join(Object... strings) {
		return Joiner.on(",").join(strings) + "\n";
	}
}