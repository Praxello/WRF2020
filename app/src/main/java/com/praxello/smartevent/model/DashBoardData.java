package com.praxello.smartevent.model;

public class DashBoardData {

    public String Name;
    public int ImagePath;

    public DashBoardData(String name, int imagePath) {
        Name = name;
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImagePath() {
        return ImagePath;
    }

    public void setImagePath(int imagePath) {
        ImagePath = imagePath;
    }
}
