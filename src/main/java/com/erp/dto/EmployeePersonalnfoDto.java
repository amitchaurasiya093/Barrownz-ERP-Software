package com.erp.dto;

public class EmployeePersonalnfoDto {

    private String height;
    private String weight;
    private String passportNumber;
    private String panNumber;
    private String aadharNumber;
    private String religion;
    private String maritalStatus;
    private String bloodGroup;
    private String shift;
    private Integer employeeId;

    public EmployeePersonalnfoDto() {
    }

    public EmployeePersonalnfoDto(String height, String weight, String passportNumber, String panNumber,
            String aadharNumber, String religion, String maritalStatus, String bloodGroup, String shift,
            Integer employeeId) {
        this.height = height;
        this.weight = weight;
        this.passportNumber = passportNumber;
        this.panNumber = panNumber;
        this.aadharNumber = aadharNumber;
        this.religion = religion;
        this.maritalStatus = maritalStatus;
        this.bloodGroup = bloodGroup;
        this.shift = shift;
        this.employeeId = employeeId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

}
