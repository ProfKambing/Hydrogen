package tk.peanut.phosphor.events;

import com.darkmagician6.eventapi.events.Event;

public class EventKey implements Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
