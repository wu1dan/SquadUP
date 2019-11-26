package com.example.squadup;

public class Interest {
    private String title;
    private int image;

    public Interest(String title, int image){
        this.title = title;
        this.image = image;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(){
        this.title = title;
    }
    public int getImage(){
        return image;
    }
    public void setImage(){
        this.image = image;
    }
}
