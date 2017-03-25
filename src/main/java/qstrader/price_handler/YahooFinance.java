package qstrader.price_handler;

import static java.lang.String.format;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abigdreamer.ark.framework.collection.DataTable;
import com.abigdreamer.ark.framework.collection.DataTableUtil;

public class YahooFinance {
	
    public static final String SEP = ",";
//    public static final CsvReader.ParseFunction<Instant> DATE_COLUMN = ofColumn("Date").map(s -> LocalDate.from(DateTimeFormatter.ISO_DATE.parse(s)).atStartOfDay(ZoneOffset.UTC.normalized()).toInstant());
//    public static final CsvReader.ParseFunction<Double> CLOSE_COLUMN = doubleColumn("Close");
//    public static final CsvReader.ParseFunction<Double> HIGH_COLUMN = doubleColumn("High");
//    public static final CsvReader.ParseFunction<Double> LOW_COLUMN = doubleColumn("Low");
//    public static final CsvReader.ParseFunction<Double> OPEN_COLUMN = doubleColumn("Open");
//    public static final CsvReader.ParseFunction<Double> ADJ_COLUMN = doubleColumn("Adj Close");
//    public static final CsvReader.ParseFunction<Double> VOLUME_COLUMN = doubleColumn("Volume");
    public static final LocalDate DEFAULT_FROM = LocalDate.of(2016, 1, 1);

    private static final Logger log = LoggerFactory.getLogger(YahooFinance.class);

//	public DoubleSeries getHistoricalAdjustedPrices(String symbol) {
//		return getHistoricalAdjustedPrices(symbol, DEFAULT_FROM.toInstant());
//	}
//
//    public DoubleSeries getHistoricalAdjustedPrices(String symbol, Instant from) {
//        return getHistoricalAdjustedPrices(symbol, from, Instant.now());
//    }
//
//    public DoubleSeries getHistoricalAdjustedPrices(String symbol, Instant from, Instant to) {
//    	String csv = getHistoricalPricesCsv(symbol, from, to);
//        return csvToDoubleSeries(csv, symbol);
//    }

    private static String getHistoricalPricesCsv(String symbol, LocalDate from, LocalDate to) {
    	String url = createHistoricalPricesUrl(symbol, from, to);
//    	System.out.println(url);
		return Http.getResponseText(url);
    }
    
    public static DataTable getHistoricalPrices(String symbol, LocalDate from, LocalDate to) {
    	String csv = getHistoricalPricesCsv(symbol, from, to);;
    	DataTable dataTable = DataTableUtil.txtToDataTable(csv);
    	dataTable.sort("Date", "asc");
		return dataTable;
    }

//    private static DoubleSeries csvToDoubleSeries(String csv, String symbol) {
//        DoubleSeries prices = SeriesCsvReader.parse(csv, SEP, DATE_COLUMN, ADJ_COLUMN);
//        prices.setName(symbol);
//        prices = prices.toAscending();
//        return prices;
//    }

    private static String createHistoricalPricesUrl(String symbol, LocalDate from, LocalDate to) {
        return format("http://ichart.yahoo.com/table.csv?s=%s&%s&%s&g=d&ignore=.csv", symbol, toYahooQueryDate(from, "abc"), toYahooQueryDate(to, "def"));
    }

    private static String toYahooQueryDate(LocalDate instant, String names) {
        LocalDate time = instant;//instant.atOffset(ZoneOffset.UTC);
        String[] strings = names.split("");
        return format("%s=%d&%s=%d&%s=%d", strings[0], time.getMonthValue() - 1, strings[1], time.getDayOfMonth(), strings[2], time.getYear());
    }
    
    public static void main(String[] args) {
    	String symbol = "GOOG";
    	DataTable dataTable = YahooFinance.getHistoricalPrices(symbol, DEFAULT_FROM, LocalDate.now());
		System.out.println(dataTable);
	}
}
