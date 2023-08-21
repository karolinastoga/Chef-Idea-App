package com.programmazione_avanzata.final_project.chef_ideas_app.dto;

public class Method {

    private Long methodId;
    private int stepNumber;
    private String stepDescription;

    public Method(){};

    public Method(Long methodId, int stepNumber, String stepDescription) {
        this.methodId = methodId;
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
    }

    public Method(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }
}
