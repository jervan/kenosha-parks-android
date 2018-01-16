package edu.uwp.appfactory.eventsmanagement.model.CalendarAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeremiah on 6/11/17.
 */

public class Attachment {

    @SerializedName("fileUrl")
    @Expose
    private String fileUrl;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("iconLink")
    @Expose
    private String iconLink;
    @SerializedName("fileId")
    @Expose
    private String fileId;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "fileUrl='" + fileUrl + '\'' +
                ", title='" + title + '\'' +
                ", iconLink='" + iconLink + '\'' +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
