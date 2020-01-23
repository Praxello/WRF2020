package com.praxello.smartevent.model;

public class DashBoardData {

    public int Name;
    public int ImagePath;

    public DashBoardData(int name, int imagePath) {
        Name = name;
        ImagePath = imagePath;
    }

    public int getName() {
        return Name;
    }

    public void setName(int name) {
        Name = name;
    }

    public int getImagePath() {
        return ImagePath;
    }

    public void setImagePath(int imagePath) {
        ImagePath = imagePath;
    }
}
