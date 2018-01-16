package edu.uwp.appfactory.eventsmanagement.model.CalendarAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jeremiah on 6/11/17.
 */

public class Item {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("htmlLink")
    @Expose
    private String htmlLink;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("creator")
    @Expose
    private Creator creator;
    @SerializedName("organizer")
    @Expose
    private Organizer organizer;
    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("end")
    @Expose
    private End end;
    @SerializedName("iCalUID")
    @Expose
    private String iCalUID;
    @SerializedName("sequence")
    @Expose
    private Integer sequence;
    @SerializedName("reminders")
    @Expose
    private Reminders reminders;
    @SerializedName("attachments")
    @Expose
    private List<Attachment> attachments = null;
    @SerializedName("transparency")
    @Expose
    private String transparency;
    @SerializedName("recurrence")
    @Expose
    private List<String> recurrence = null;
    @SerializedName("recurringEventId")
    @Expose
    private String recurringEventId;
    @SerializedName("originalStartTime")
    @Expose
    private OriginalStartTime originalStartTime;
    @SerializedName("hangoutLink")
    @Expose
    private String hangoutLink;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

    public String getICalUID() {
        return iCalUID;
    }

    public void setICalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Reminders getReminders() {
        return reminders;
    }

    public void setReminders(Reminders reminders) {
        this.reminders = reminders;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getTransparency() {
        return transparency;
    }

    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }

    public List<String> getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(List<String> recurrence) {
        this.recurrence = recurrence;
    }

    public String getRecurringEventId() {
        return recurringEventId;
    }

    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }

    public OriginalStartTime getOriginalStartTime() {
        return originalStartTime;
    }

    public void setOriginalStartTime(OriginalStartTime originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    public String getHangoutLink() {
        return hangoutLink;
    }

    public void setHangoutLink(String hangoutLink) {
        this.hangoutLink = hangoutLink;
    }

    @Override
    public String toString() {
        return "Item{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", htmlLink='" + htmlLink + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", creator=" + creator +
                ", organizer=" + organizer +
                ", start=" + start +
                ", end=" + end +
                ", iCalUID='" + iCalUID + '\'' +
                ", sequence=" + sequence +
                ", reminders=" + reminders +
                ", attachments=" + attachments +
                ", transparency='" + transparency + '\'' +
                ", recurrence=" + recurrence +
                ", recurringEventId='" + recurringEventId + '\'' +
                ", originalStartTime=" + originalStartTime +
                ", hangoutLink='" + hangoutLink + '\'' +
                '}';
    }
}