package application.chatapp_dialog.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AdminReportInformation {
    private Integer reportedID;
    private String reportedUsername;
    private String reportedEmail;
    private String displayName;
    private Integer reporterID;
    private String reporterUsername;
    private Timestamp createDate;
    private String reportedUserStatus;
    public  AdminReportInformation(){
        reporterID = 0;
        reportedID = 0;
        reportedEmail = "";
        displayName = "";
        reportedUsername = "";
        createDate = Timestamp.valueOf(LocalDateTime.now().withNano(0));
        reporterUsername = "";
    }
    public AdminReportInformation(int reportedID, String reportedUsername, String reportedEmail, String displayName, int reporterID, String reporterUsername, Timestamp createDate, String status){
        this.reporterID = reporterID;
        this.reportedID = reportedID;
        this.reporterUsername = reporterUsername;
        this.reportedUsername = reportedUsername;
        this.reportedEmail  = reportedEmail;
        this.displayName = displayName;
        this.createDate = createDate;
        this.reportedUserStatus = status;
    }

    public String getReportedUserStatus() {
        return reportedUserStatus;
    }

    public void setReportedUserStatus(String reportedUserStatus) {
        this.reportedUserStatus = reportedUserStatus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Integer getReportedID() {
        return reportedID;
    }

    public Integer getReporterID() {
        return reporterID;
    }

    public String getReportedEmail() {
        return reportedEmail;
    }

    public String getReportedUsername() {
        return reportedUsername;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setReportedID(Integer reportedID) {
        this.reportedID = reportedID;
    }

    public void setReportedEmail(String reportedEmail) {
        this.reportedEmail = reportedEmail;
    }

    public void setReportedUsername(String reportedUsername) {
        this.reportedUsername = reportedUsername;
    }

    public void setReporterID(Integer reporterID) {
        this.reporterID = reporterID;
    }

    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AdminReportInformation c)
        {
            //whatever here
            return this.getReportedID().equals(c.getReportedID()) && this.getReporterID().equals(c.getReporterID());
        }
        return false;
    }

}
