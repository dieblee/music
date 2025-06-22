package com.xm.entity;

import java.util.Date;

public class ListSong {
    private Integer id;
    private Integer song_id;
    private Integer song_list_id;

    private String singerName;
    private String name;
    private String introduction;
    private String pic;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSong_id() {
        return song_id;
    }

    public void setSong_id(Integer song_id) {
        this.song_id = song_id;
    }

    public Integer getSong_list_id() {
        return song_list_id;
    }

    public void setSong_list_id(Integer song_list_id) {
        this.song_list_id = song_list_id;
    }


    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }



    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
