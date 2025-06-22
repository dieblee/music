package com.xm.entity;

import java.util.Date;

public class Statistic {
    private Integer id;
    private Integer songId;
    private Integer songListId;
    private Date date;
    private String singerName;
    private Integer click;
    private String songPic;
    private String songUrl;
    private String songTitle;
    private String createName;
    private String songListTitle;
    private String songListStyle;
    private Integer singerId;
    private Integer totalClicks ;
    private String songListPic;
    private Integer oldclick;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public Integer getSongListId() {
        return songListId;
    }

    public void setSongListId(Integer songListId) {
        this.songListId = songListId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getClick() {
        return click;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongPic() {
        return songPic;
    }

    public void setSongPic(String songPic) {
        this.songPic = songPic;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getSongListTitle() {
        return songListTitle;
    }

    public void setSongListTitle(String songListTitle) {
        this.songListTitle = songListTitle;
    }

    public String getSongListStyle() {
        return songListStyle;
    }

    public void setSongListStyle(String songListStyle) {this.songListStyle = songListStyle;}

    public Integer getSingerId() {
        return singerId;
    }

    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    public Integer getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(Integer totalClicks) {
        this.totalClicks = totalClicks;
    }

    public String getSongListPic() {
        return songListPic;
    }

    public void setSongListPic(String songListPic) {
        this.songListPic = songListPic;
    }

    public Integer getOldclick() {
        return oldclick;
    }

    public void setOldclick(Integer oldclick) {
        this.oldclick = oldclick;
    }
}
