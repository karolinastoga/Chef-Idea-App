package com.programmazione_avanzata.final_project.chef_ideas.server.exception;

public final class ExceptionInfo {

    private ExceptionInfo() {}

    public static final String RECIPE_NOT_FOUND = "There was no recipe found with given id";

    public final static String RECIPE_CREATION_VALIDATION_ERROR = "Error while validating recipe";

    public final static String ILLEGAL_FOOD_ENTITY_LIST = "List of food entities cannot be empty or null";

    public final static String ILLEGAL_RECIPE_ID = "Recipe id cannot be null";

    public final static String ERROR_WHILE_FETCHING_DATA = "There was an error while reading data from a file";

    public static final String ERROR_WHILE_MAPPING = "There was an error during mapping values";
}
