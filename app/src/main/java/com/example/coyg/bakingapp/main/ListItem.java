package com.example.coyg.bakingapp.main;

public class ListItem
{
    private String recipe_name;
    private String recipe_id;

    public ListItem(String recipe_name, String recipe_id)
    {
        this.recipe_name = recipe_name;
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name()
    {
        return recipe_name;
    }

    public String getRecipe_id()
    {
        return recipe_id;
    }
}
