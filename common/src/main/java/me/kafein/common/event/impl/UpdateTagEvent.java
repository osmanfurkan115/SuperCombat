package me.kafein.common.event.impl;

import lombok.Getter;
import lombok.Setter;
import me.kafein.common.event.Event;
import me.kafein.common.event.EventType;
import me.kafein.common.tag.Tag;

public class UpdateTagEvent implements Event {

    @Getter private final Tag tag;
    @Getter @Setter private boolean cancelled;

    /**
     * tag's timer will be updated
     *
     * @param tag updated tag
     * @param cancelled if event is cancelled
     */
    public UpdateTagEvent(Tag tag, boolean cancelled) {
        this.tag = tag;
        this.cancelled = cancelled;
    }

    public EventType getType() {
        return EventType.UPDATE_TAG;
    }

}
