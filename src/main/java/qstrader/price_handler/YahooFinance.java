package qstrader.price_handler;

import static java.lang.String.format;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rapidark.framework.collection.DataTable;
import com.github.rapidark.framework.collection.DataTableUtil;

public class YahooFinance {
	
    public static final String SEP = ",";
//    public static final CsvReader.ParseFunction<Instant> DATE_COLUMN = ofColumn("Date").map(s -> LocalDate.from(DateTimeFormatter.ISO_DATE.parse(s)).atStartOfDay(ZoneOffset.UTC.normalized()).toInstant());
//    public static final CsvReader.ParseFunction<Double> CLOSE_COLUMN = doubleColumn("Close");
//    public static final CsvReader.ParseFunction<Double> HIGH_COLUMN = doubleColumn("High");
//    public static final CsvReader.ParseFunction<Double> LOW_COLUMN = doubleColumn("Low");
//    public static final CsvReader.ParseFunction<Double> OPEN_COLUMN = doubleColumn("Open");
//    public static final CsvReader.ParseFunction<Double> ADJ_COLUMN = doubleColumn("Adj Close");
//    public static final CsvReader.ParseFunction<Double> VOLUME_COLUMN = doubleColumn("Volume");
    public static final OffsetDateTime DEFAULT_FROM = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

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

    private static String getHistoricalPricesCsv(String symbol, Instant from, Instant to) {
		return Http.getResponseText(createHistoricalPricesUrl(symbol, from, to));
    }
    
    public static DataTable getHistoricalPrices(String symbol, Instant from, Instant to) {
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

    private static String createHistoricalPricesUrl(String symbol, Instant from, Instant to) {
        return format("http://ichart.yahoo.com/table.csv?s=%s&%s&%s&g=d&ignore=.csv", symbol, toYahooQueryDate(from, "abc"), toYahooQueryDate(to, "def"));
    }

    private static String toYahooQueryDate(Instant instant, String names) {
        OffsetDateTime time = instant.atOffset(ZoneOffset.UTC);
        String[] strings = names.split("");
        return format("%s=%d&%s=%d&%s=%d", strings[0], time.getMonthValue() - 1, strings[1], time.getDayOfMonth(), strings[2], time.getYear());
    }
    
    public static void main(String[] args) {
    	String symbol = "GOOG";
    	DataTable dataTable = YahooFinance.getHistoricalPrices(symbol, DEFAULT_FROM.toInstant(), Instant.now());
		System.out.println(dataTable);
	}
}
