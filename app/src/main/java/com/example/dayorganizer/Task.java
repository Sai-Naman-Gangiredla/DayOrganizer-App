package com.example.dayorganizer;

public class Task {
    private int id;
    private String title;
    private String category;
    private String priority;
    private String date;
    private String time;
    private boolean completed;

    public Task(int id, String title, String category, String priority, String date, String time, boolean completed) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.date = date;
        this.time = time;
        this.completed = completed;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
} 