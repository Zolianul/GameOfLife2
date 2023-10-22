package com.example.gameoflife;

public class Cell {
    private String cellType;
    private String foodStatus;
    public Cell(String cellType, String foodStatus) {
        this.cellType = cellType;
        this.foodStatus = foodStatus;
    }

    public String getCellType() {
        return cellType;
    }

    public String getFoodStatus() {
        return foodStatus;
    }
}
