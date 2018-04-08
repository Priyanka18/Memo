package com.example.priyanka.noteit;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by priya on 13-02-2018.
 */

public class notes implements Serializable {
    private String title;
    private String note;
    private String update;

    private static int ctr = 1;

    public notes(String title, String noteText, String update) {
        this.title = title;
        this.update = update;
        this.note = noteText;
        ctr++;
    }

    public String getTitle(){return title;}
    public void setTitle(String title){
        this.title = title;
    }

    public String getNote(){return note;}
    public void setNote(String note){
        this.note = note;
    }

    public String getUpdate(){return update;}
    public void setUpdate(String update){
//        this.update = (Long.parseLong(update));
        this.update = update;
    }

    public String toString(){
        return title + "(" + note + ")" + update;
    }
}
