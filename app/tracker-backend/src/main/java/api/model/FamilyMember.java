package api.model;

public class FamilyMember {

    private String firstName;
    private String familyName;
    private String birthDate;
    private String memberPatientId;
    private String relationship;

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

}
