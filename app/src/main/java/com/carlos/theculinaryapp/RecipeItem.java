package com.carlos.theculinaryapp;

public class RecipeItem {
    private String name;
    private String imageFile;
    private String recipeUuid;
    private String ownerUuid;

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }


    public RecipeItem(String name, String imageFile) {

        this.name = name;
        this.imageFile = imageFile;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getRecipeUuid() {
        return recipeUuid;
    }

    public void setRecipeUuid(String s){
        recipeUuid = s;
    }
}
