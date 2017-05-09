package qstrader;

import java.io.File;

public class Settings {

	public static final Config DEFAULT;
	public static final Config TEST;

	static {
		String basePath = System.getProperty("user.home") + File.separator + "qstrader" + File.separator;
		DEFAULT = new Config(basePath + "data", basePath + "out");

		String testBasePath = System.getProperty("user.dir") + File.separator;
		TEST = new Config(testBasePath + "data", testBasePath + "out");
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		System.out.println(System.getProperty("user.home"));
	}
}
