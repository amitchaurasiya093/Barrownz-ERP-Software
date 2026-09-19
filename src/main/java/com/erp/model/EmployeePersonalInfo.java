package com.erp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_personal_info")
public class EmployeePersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_info_id")
    private int personalInfoId;

    private String height;
    private String weight;
    @Column(name = "passport_number")
    private String passportNumber;
    @Column(name = "pan_number")
    private String panNumber;
    @Column(name = "aadhar_number")
    private String aadharNumber;
    private String religion;
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "blood_group")
    private String bloodGroup;

    private String shift;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    public EmployeePersonalInfo() {
    }

    public EmployeePersonalInfo(int personalInfoId, String height, String weight, String passportNumber,
            String panNumber, String aadharNumber, String religion, String maritalStatus, String bloodGroup,
            String shift, Employee employee) {
        this.personalInfoId = personalInfoId;
        this.height = height;
        this.weight = weight;
        this.passportNumber = passportNumber;
        this.panNumber = panNumber;
        this.aadharNumber = aadharNumber;
        this.religion = religion;
        this.maritalStatus = maritalStatus;
        this.bloodGroup = bloodGroup;
        this.shift = shift;
        this.employee = employee;
    }

    public int getPersonalInfoId() {
        return personalInfoId;
    }

    public void setPersonalInfoId(int personalInfoId) {
        this.personalInfoId = personalInfoId;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
