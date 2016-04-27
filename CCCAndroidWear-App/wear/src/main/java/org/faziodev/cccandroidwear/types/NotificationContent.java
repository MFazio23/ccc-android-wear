package org.faziodev.cccandroidwear.types;

/**
 * Created by MFazio on 4/26/2016.
 */
public class NotificationContent {

    private String subject;
    private String content;

    public NotificationContent() { }

    public NotificationContent(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NotificationContent{" +
            "subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}
