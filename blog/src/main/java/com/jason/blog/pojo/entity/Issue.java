package com.jason.blog.pojo.entity;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * Created by jason on 2016/8/10.
 */
@Entity
@Table(name = "T_ISSUE")
public class Issue {

    @Id
    @Column(name = "issue_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @Column(name = "issue_content")
    private String issueContent;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "column_belong")
    private String columnBelong;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Column(name = "issue_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date issueTime = new Date();

    public Issue() {
    }

    public Issue(String issueContent, Long creatorId, String creatorName, String columnBelong, List<Comment> comments) {
        this.issueContent = issueContent;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.columnBelong = columnBelong;
        this.comments = comments;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getColumnBelong() {
        return columnBelong;
    }

    public void setColumnBelong(String columnBelong) {
        this.columnBelong = columnBelong;
    }
}
