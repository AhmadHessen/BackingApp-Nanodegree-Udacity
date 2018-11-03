package com.example.coyg.bakingapp.recipeDetails.ingredients;

public class IngredientsItem
{

    String quantity, measure, ingredient;

    public IngredientsItem(String quantity, String measure, String ingredient)
    {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public String getMeasure()
    {
        return measure;
    }

    public String getIngredient()
    {
        return ingredient;
    }
}
