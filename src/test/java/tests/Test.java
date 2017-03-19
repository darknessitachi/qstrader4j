package tests;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import joinery.DataFrame;

public class Test {

	public static void main(String[] args) throws IOException {
		DataFrame<Object> df = DataFrame.readCsv("C:\\Users\\Administrator\\git\\qstrader4j\\data\\GOOG.csv");
		System.out.println(df);
		System.out.println("===================================================");
		DataFrame<Object> df2 = DataFrame.readCsv("C:\\Users\\Administrator\\git\\qstrader4j\\data\\AMZN.csv");
		System.out.println(df2);
		System.out.println("===================================================");
		DataFrame<Object> dfMerged = new DataFrame<>(df2.columns());
		System.out.println(dfMerged.columns());
		
		DataFrame<Object> all = new DataFrame<>(df.columns());
		ListIterator<List<Object>> rows = df.iterrows();
		while(rows.hasNext()) {
			all.append(rows.next());
		}
		
		ListIterator<List<Object>> rows2 = df2.iterrows();
		while(rows2.hasNext()) {
			all.append(rows2.next());
		}
		all.sortBy("Time");
		System.out.println("===================================================");
		System.out.println(all);
//		dfMerged = dfMerged.merge(df);
//		dfMerged = df.merge(other)
//		dfMerged.reindex("Time");
		
//		DataFrame<Object> df = DataFrame.readCsv("https://www.quandl.com/api/v1/datasets/GOOG/NASDAQ_AAPL.csv");
//		System.out.println(df);
//		System.out.println(df.types());
//		
//		df = df.sortBy("Date");
//		System.out.println(df);
		
	}
}
