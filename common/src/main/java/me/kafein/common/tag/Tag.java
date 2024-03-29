package me.kafein.common.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
@Getter @Setter
public class Tag {

    private final String userName;
    private final UUID userUUID;

    private String otherUserName;
    private UUID otherUserUUID;

    private TagCause tagCause;
    private TagReason tagReason;

    private long duration;

    public void addDuration(long duration) {
        this.duration += duration;
    }

    public void removeDuration(long duration) {
        this.duration -= duration;
    }

    public void setOtherUser(String otherUserName, UUID otherUserUUID) {
        this.otherUserName = otherUserName;
        this.otherUserUUID = otherUserUUID;
    }

}
