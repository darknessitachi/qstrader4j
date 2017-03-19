package qstrader.event;

/**
 * Event is base class providing an interface for all subsequent
    (inherited) events, that will trigger further events in the
    trading infrastructure.
    
 * @author Administrator
 */
public class Event {
	
	public EventType type;
	
    public String typename() {
        return this.type.toString();
    }


}