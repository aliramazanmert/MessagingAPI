package com.alirmert.offlinemessaging.userlog;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class UserLog {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String log;
    private String date;
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public UserLog(String log) {
        this.log = log;
        this.date = LocalDate.now().toString();
        this.time = LocalTime.now().toString();
    }

    public UserLog(){}

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
