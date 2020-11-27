package api.model;

import java.util.List;

public class FamilyMember {

    private String firstName;
    private String familyName;
    private String birthDate;
    private String memberPatientId;
    private String relationship;
    private int age;
    private String gender;
    private String address;
    private String height;
    private String weight;
    private String ldl;
    private String hdl;
    private String bmi;
    private String systolic;
    private String diatollic;

    //List of allergies, medications, conditions, emcounters and immunizations.
    List<String> medications;
    List<String> allergies;
    List<String> conditions;
    List<String> encounters;
    List<String> immunizations;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMemberPatientId() {
        return memberPatientId;
    }

    public void setMemberPatientId(String memberPatientId) {
        this.memberPatientId = memberPatientId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLdl() {
        return ldl;
    }

    public void setLdl(String ldl) {
        this.ldl = ldl;
    }

    public String getHdl() {
        return hdl;
    }

    public void setHdl(String hdl) {
        this.hdl = hdl;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiatollic() {
        return diatollic;
    }

    public void setDiatollic(String diatollic) {
        this.diatollic = diatollic;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public List<String> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<String> encounters) {
        this.encounters = encounters;
    }

    public List<String> getImmunizations() {
        return immunizations;
    }

    public void setImmunizations(List<String> immunizations) {
        this.immunizations = immunizations;
    }

}
