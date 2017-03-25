//import time
package qstrader;

import qstrader.event.Event;

public class profiling {
	
	public static long speed(long ticks, long t0) {
		return ticks / (System.currentTimeMillis() - t0);
	}

	public static String s_speed(Event time_event, long ticks, long t0) {
		long sp = speed(ticks, t0);
		String s_typ = time_event.type.toString() + "S";
		return String.format("%d %s processed @ %s %s/s", ticks, s_typ, sp, s_typ);
	}

}