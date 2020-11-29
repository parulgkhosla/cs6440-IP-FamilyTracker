package api.fhir;

import api.model.FamilyMember;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import api.model.PatientInfo;
import ca.uhn.fhir.util.BundleUtil;
import jdk.nashorn.internal.ir.LiteralNode;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.AllergyIntoleranceCategory;
import org.hl7.fhir.r4.model.codesystems.AllergyIntoleranceType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.Enumeration;

public class FHIRService {

//    private static final String baseURLHAPI = "https://hapi.fhir.org/baseR4";
    private static final String baseURLHAPI = "https://launch.smarthealthit.org/v/r4/fhir";
//    2744ab6f-91dd-4e4f-8208-fe52ee2c27d1
    public static FhirContext ctx;
    public static IGenericClient client;

    public FHIRService(){
        ctx = FhirContext.forR4();
        client = ctx.newRestfulGenericClient(baseURLHAPI);
    }

    /**
     *
     * @param patientId
     * @return
     */
    public PatientInfo getPatientInfoById(String patientId) throws ParseException {

        Patient patient = client.read()
                .resource(Patient.class)
                .withId(patientId)
                .execute();
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(patient.getIdElement().getIdPart());
        patientInfo.setDisplayName(patient.getNameFirstRep().getNameAsSingleString());
        patientInfo.setFirstName(patient.getName().get(0).getGiven().get(0).getValue());
        patientInfo.setLastName(patient.getName().get(0).getFamily());

        if(patient.getBirthDate() != null) {
            patientInfo.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(patient.getBirthDate()));
            patientInfo.setAge(Period.between(patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());

        }
        if(patient.getGender() != null)
            patientInfo.setGender(patient.getGender().getDisplay());
        if(patient.getBirthDate() != null)
            patientInfo.setAge(Period.between(patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
        patientInfo.setAddress(patient.getAddressFirstRep().getLine().get(0)+" "+patient.getAddressFirstRep().getCity()+
                " "+patient.getAddressFirstRep().getState()+" "+patient.getAddressFirstRep().getPostalCode());

        return patientInfo;
    }

    public PatientInfo getPatientVitalsByPatientId(PatientInfo patientInfo) {
        PatientInfo updatedPatientInfo = new PatientInfo();
        List<Observation> observations = new ArrayList<>();

        updatedPatientInfo.setPatientId(patientInfo.getPatientId());
        updatedPatientInfo.setFirstName(patientInfo.getFirstName());
        updatedPatientInfo.setLastName(patientInfo.getLastName());
        updatedPatientInfo.setDisplayName(patientInfo.getDisplayName());
        updatedPatientInfo.setAge(patientInfo.getAge());
        updatedPatientInfo.setDateOfBirth(patientInfo.getDateOfBirth());
        updatedPatientInfo.setAddress(patientInfo.getAddress());
        updatedPatientInfo.setGender(patientInfo.getGender());

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().codes("29463-7","8302-2", "2085-9", "2089-1", "39156-5"))
                .and(Observation.PATIENT.hasId(patientInfo.getPatientId()))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        //weight "code": "29463-7"
        //hdl 2085-9
        //ldl 2089-1
        //bmi 39156-5
        observations = getCompleteBundleAsList(bundle, client, Observation.class);
        //TODO add logic to form thw eigths list ofr a graph maybe
        for (Observation observation : observations) {
//            System.out.println("ob code-"+observation.getCode().getCoding().get(0).getDisplay());
//            System.out.println("ob value-"+observation.getValueQuantity().getValue());
//            System.out.println(observation.getIssued());
            if (observation.getCode() != null) {
                switch(observation.getCode().getCoding().get(0).getCode()) {
                    //weight
                    case "29463-7":
                        if (updatedPatientInfo.getWeight() == null)
                            updatedPatientInfo.setWeight(Math.round(observation.getValueQuantity().getValue().doubleValue())
                            + " " + observation.getValueQuantity().getUnit());
                        break;
                    //height
                    case "8302-2":
                        if (updatedPatientInfo.getHeight() == null)
                            updatedPatientInfo.setHeight(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //hdl
                    case "2085-9":
                        if (updatedPatientInfo.getHdl() == null)
                            updatedPatientInfo.setHdl(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //ldl
                    case "2089-1":
                        if (updatedPatientInfo.getLdl() == null)
                            updatedPatientInfo.setLdl(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //bmi
                    case "39156-5":
                        if (updatedPatientInfo.getBmi() == null)
                            updatedPatientInfo.setBmi(String.valueOf(Math.round(observation.getValueQuantity().getValue().doubleValue())));
                        break;
                }
            }
        }
        return updatedPatientInfo;
    }

    public FamilyMember getFamilyMemberVitalsByPatientId(FamilyMember patientInfo) {
        FamilyMember updatedPatientInfo = new FamilyMember();
        List<Observation> observations = new ArrayList<>();

        updatedPatientInfo.setMemberPatientId(patientInfo.getMemberPatientId());
        updatedPatientInfo.setFirstName(patientInfo.getFirstName());
        updatedPatientInfo.setFamilyName(patientInfo.getFamilyName());
        updatedPatientInfo.setAge(patientInfo.getAge());
        updatedPatientInfo.setBirthDate(patientInfo.getBirthDate());
        updatedPatientInfo.setAddress(patientInfo.getAddress());
        updatedPatientInfo.setGender(patientInfo.getGender());
        updatedPatientInfo.setRelationship(patientInfo.getRelationship());

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().codes("29463-7","8302-2", "2085-9", "2089-1", "39156-5"))
                .and(Observation.PATIENT.hasId(patientInfo.getMemberPatientId()))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        //weight "code": "29463-7"
        //hdl 2085-9
        //ldl 2089-1
        //bmi 39156-5
        observations = getCompleteBundleAsList(bundle, client, Observation.class);
        //TODO add logic to form thw eigths list ofr a graph maybe
        for (Observation observation : observations) {
//            System.out.println("ob code-"+observation.getCode().getCoding().get(0).getDisplay());
//            System.out.println("ob value-"+observation.getValueQuantity().getValue());
//            System.out.println(observation.getIssued());
            if (observation.getCode() != null) {
                switch(observation.getCode().getCoding().get(0).getCode()) {
                    //weight
                    case "29463-7":
                        if (updatedPatientInfo.getWeight() == null)
                            updatedPatientInfo.setWeight(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //height
                    case "8302-2":
                        if (updatedPatientInfo.getHeight() == null)
                            updatedPatientInfo.setHeight(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //hdl
                    case "2085-9":
                        if (updatedPatientInfo.getHdl() == null)
                            updatedPatientInfo.setHdl(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //ldl
                    case "2089-1":
                        if (updatedPatientInfo.getLdl() == null)
                            updatedPatientInfo.setLdl(Math.round(observation.getValueQuantity().getValue().doubleValue())
                                    + " " + observation.getValueQuantity().getUnit());
                        break;
                    //bmi
                    case "39156-5":
                        if (updatedPatientInfo.getBmi() == null)
                            updatedPatientInfo.setBmi(String.valueOf(Math.round(observation.getValueQuantity().getValue().doubleValue())));
                        break;
                }
            }
        }
        return updatedPatientInfo;
    }

    /**
     *
     * @param patientId
     * @return
     */
    public List<Observation> getPatientWeightById(String patientId) {
        List<Observation> weightsList = new ArrayList<>();

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().code("29463-7"))
                .and(Observation.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        //weight "code": "29463-7"
        weightsList = getCompleteBundleAsList(bundle, client, Observation.class);

//        System.out.println(observationList.size());
//        String weight = "";
//        if(!observationList.isEmpty())
//            weight = observationList.get(0).getValueQuantity().getValue().toString()+" "+observationList.get(0).getValueQuantity().getUnit();
//        return weight;
        return  weightsList;
    }

    /**
     *
     * @param patientId
     * @return
     */
    public String getPatientHeightById(String patientId) {

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().code("8302-2"))
                .and(Observation.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observationList = getCompleteBundleAsList(bundle, client, Observation.class);
//        System.out.println(observationList.size());
        String height = "";
        if(!observationList.isEmpty())
            height = observationList.get(0).getValueQuantity().getValue().toString()+" "+observationList.get(0).getValueQuantity().getUnit();
        return height;
    }

    public String getPatientHDLById(String patientId) {

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().code("2085-9"))
                .and(Observation.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observationList = getCompleteBundleAsList(bundle, client, Observation.class);
//        System.out.println(observationList.size());
        String hdl = "";
        if(!observationList.isEmpty())
            hdl = observationList.get(0).getValueQuantity().getValue().toString()+" "+observationList.get(0).getValueQuantity().getUnit();
        return hdl;
    }

    public String getPatientLDLById(String patientId) {

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().code("2089-1"))
                .and(Observation.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observationList = getCompleteBundleAsList(bundle, client, Observation.class);
//        System.out.println(observationList.size());
        String ldl = "";
        if(!observationList.isEmpty())
            ldl = observationList.get(0).getValueQuantity().getValue().toString()+" "+observationList.get(0).getValueQuantity().getUnit();
        return ldl;
    }
    //39156-5
    public String getPatientBMIById(String patientId) {

        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().code("39156-5"))
                .and(Observation.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observationList = getCompleteBundleAsList(bundle, client, Observation.class);
//        System.out.println(observationList.size());
        String bmi = "";
        if(!observationList.isEmpty())
            bmi = observationList.get(0).getValueQuantity().getValue().toString();//+" "+observationList.get(0).getValueQuantity().getUnit();
        return bmi;
    }

    //TODO to add systolic and diatolic get methods

    public List<String> getMedicationsByPatientId(String patientId) {
        List<String> medicationList = new ArrayList<>();

        Bundle bundle = client.search().forResource(MedicationRequest.class)
                .where(MedicationRequest.STATUS.exactly().code("active"))
                .and(MedicationRequest.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();
        List<MedicationRequest> medicationRequestList = getCompleteBundleAsList(bundle, client, MedicationRequest.class);

        System.out.println("medications count-"+medicationRequestList.size());
        StringBuilder stringBuilder;
        for (MedicationRequest medicationRequest : medicationRequestList) {
            stringBuilder = new StringBuilder();
            stringBuilder.append((medicationRequest.getMedicationCodeableConcept()
                    .getCoding().get(0).getDisplay()));
            if (medicationRequest.getDosageInstruction() != null ) {

                if (medicationRequest.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue() != null) {
                    stringBuilder.append("|" + medicationRequest.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue())
                            .append("|" + medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriod())
                            .append(" ")
                            .append(medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriodUnit());
                }
            }
//            System.out.println("med value-"+stringBuilder.toString());
            medicationList.add(stringBuilder.toString());
        }

        return medicationList;
    }

    public List<String> getConditionsByPatientId(String patientId) {
        List<String> conditions = new ArrayList<>();

        Bundle bundle = client.search().forResource(Condition.class)
                .where(Condition.SEVERITY.exactly().code("active"))
                .and(Condition.PATIENT.hasId(patientId))
//                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();
        List<Condition> conditionList = getCompleteBundleAsList(bundle, client, Condition.class);

        System.out.println("conditions count-"+conditionList.size());

        for (Condition condition : conditionList) {
            conditions.add(condition.getCode().getCoding().get(0).getDisplay());
        }

        return conditions;
    }

    public List<String> getAllergiesByPatientId(String patientId) {
        List<String> allergies = new ArrayList<>();

        Bundle bundle = client.search().forResource(AllergyIntolerance.class)
//                .where(AllergyIntolerance.CLINICAL_STATUS.exactly().code("active"))
                .where(AllergyIntolerance.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();
        List<AllergyIntolerance> allergyIntoleranceList = getCompleteBundleAsList(bundle, client, AllergyIntolerance.class);

        System.out.println("allergy count - "+allergyIntoleranceList.size());
        StringBuilder stringBuilder;
        for (AllergyIntolerance allergyIntolerance : allergyIntoleranceList) {
            stringBuilder = new StringBuilder();

//            System.out.println("allergy name -"+allergyIntolerance.getCode().getCoding().get(0).getDisplay());
//            System.out.println("allergy id-"+allergyIntolerance.getIdElement().getIdPart());
            stringBuilder.append(allergyIntolerance.getCode().getCoding().get(0).getDisplay());
            if (allergyIntolerance.getReaction() != null && allergyIntolerance.getReactionFirstRep().getManifestationFirstRep().getCodingFirstRep().getDisplay() != null) {
//                System.out.println(allergyIntolerance.getReactionFirstRep().getManifestationFirstRep().getCodingFirstRep().getDisplay());

                stringBuilder.append("|")
                        .append(allergyIntolerance.getReactionFirstRep().getManifestationFirstRep().getCodingFirstRep().getDisplay());
            }
            System.out.println("allergy string-"+stringBuilder.toString());
            allergies.add(stringBuilder.toString());
        }

        return allergies;
    }

    public List<String> getEncountersByPatientId(String patientId) {
        List<String> encounters = new ArrayList<>();
        //lastest encounter
        Bundle bundle = client.search().forResource(Encounter.class)
//                .where(AllergyIntolerance.CLINICAL_STATUS.exactly().code("active"))
                .where(Encounter.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .count(0)
                .returnBundle(Bundle.class)
                .execute();
        List<Encounter> encounterList = getCompleteBundleAsList(bundle, client, Encounter.class);

        System.out.println("encounter count - "+encounterList.size());

        for (Encounter encounter : encounterList) {
            if (encounter.getReasonCode().size() != 0
            && !encounters.contains(encounter.getReasonCode().get(0).getCoding().get(0).getDisplay()))
                encounters.add(encounter.getReasonCode().get(0).getCoding().get(0).getDisplay()
                        +"|"+ encounter.getPeriod().getEnd().toString());
            if (encounters.size() ==2) { break; }
        }

        return encounters;
    }

    public List<String> getImmunizationByPatientId(String patientId) {
        List<String> immunizations = new ArrayList<>();
        //lastest encounter
        Bundle bundle = client.search().forResource(Immunization.class)
                .where(Immunization.PATIENT.hasId(patientId))
                .and(Immunization.STATUS.exactly().code("completed"))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .count(2)
                .returnBundle(Bundle.class)
                .execute();
        List<Immunization> immunizationList = getCompleteBundleAsList(bundle, client, Immunization.class);

        System.out.println("immunization count - "+immunizationList.size());

        for (Immunization immunization : immunizationList) {
            if (!immunizations.contains(immunization.getVaccineCode().getText())) {
                immunizations.add(immunization.getVaccineCode().getText());
            }
        }

        return immunizations;
    }

    public Patient getPatientInfoByNameDob(String firstName, String familyName, String dob) throws ParseException {
        Patient patientId = null;
//        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
        Bundle bundle = (Bundle) client.search().forResource(Patient.class)
                .where(Patient.FAMILY.matches().value(familyName))
                .and(Patient.NAME.matches().value(firstName))
//                .and(Patient.BIRTHDATE.exactly().day(date))
                .returnBundle(Bundle.class)
                .execute();
        List<Patient> patients = getCompleteBundleAsList(bundle, client, Patient.class);
        System.out.println("related person count-"+patients.size());
//        for (Patient patient : patients) {
//            System.out.println(patient.getBirthDate());
//            System.out.println(date);
//            if (patient.getBirthDate().equals(date)) {
//                return patient.getIdElement().getIdPart();
//            }
//            patientId = patient.getIdElement().getIdPart();
//        }
        System.out.println("member patient id -"+patientId);
        if (patients.isEmpty()) return null;
        return patients.get(0);
    }

    public List<FamilyMember> getFamilyMemberForPatient(String patientId) throws ParseException {
        List<FamilyMember> familyMembers = new ArrayList<>();

        Bundle bundle = client.search().forResource(RelatedPerson.class)
                .where(RelatedPerson.PATIENT.hasId(patientId))
//                .and(RelatedPerson.RELATIONSHIP.exactly().code("WIFE"))
                .returnBundle(Bundle.class)
                .execute();
        List<RelatedPerson> relatedPersonList = getCompleteBundleAsList(bundle, client, RelatedPerson.class);

        System.out.println("related count - "+relatedPersonList.size());

        for (RelatedPerson relatedPerson : relatedPersonList) {
            if (relatedPerson.getName() != null) {
                FamilyMember updateFamilyMember = new FamilyMember();
                FamilyMember familyMember = new FamilyMember();
                familyMember.setRelationship(relatedPerson.getRelationship().get(0).getCoding().get(0).getCode());
                familyMember.setFamilyName(relatedPerson.getName().get(0).getFamily());
                familyMember.setFirstName(relatedPerson.getName().get(0).getGiven().get(0).getValue());
                familyMember.setBirthDate(relatedPerson.getBirthDate().toString());
                System.out.println("family member id-" + relatedPerson.getIdElement().getIdPart());
                System.out.println("family member dob-" + relatedPerson.getBirthDate());
                System.out.println("family member last name-" + relatedPerson.getName().get(0).getFamily());
                //fetch member health info
                Patient memberInfo = this.getPatientInfoByNameDob(relatedPerson.getName().get(0).getGiven().get(0).getValue(),
                        relatedPerson.getName().get(0).getFamily(), relatedPerson.getBirthDate().toString());
                if (memberInfo != null && memberInfo.getIdElement() != null) {
                    //set family member info

                    if(memberInfo.getGender() != null)
                        familyMember.setGender(memberInfo.getGender().getDisplay());
                    if(memberInfo.getBirthDate() != null) {
                        familyMember.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").format(memberInfo.getBirthDate()));
                        familyMember.setAge(Period.between(memberInfo.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
                    }
                    familyMember.setAddress(memberInfo.getAddressFirstRep().getLine().get(0)+" "+memberInfo.getAddressFirstRep().getCity()+
                        " "+memberInfo.getAddressFirstRep().getState()+" "+memberInfo.getAddressFirstRep().getPostalCode());

                    familyMember.setMemberPatientId(memberInfo.getIdElement().getIdPart());

                    updateFamilyMember = getFamilyMemberVitalsByPatientId(familyMember);

                    updateFamilyMember.setAllergies(this.getAllergiesByPatientId(familyMember.getMemberPatientId()));
                    updateFamilyMember.setConditions(this.getConditionsByPatientId(familyMember.getMemberPatientId()));
                    updateFamilyMember.setEncounters(this.getEncountersByPatientId(familyMember.getMemberPatientId()));
                    updateFamilyMember.setImmunizations(this.getImmunizationByPatientId(familyMember.getMemberPatientId()));
                    updateFamilyMember.setMedications(this.getMedicationsByPatientId(familyMember.getMemberPatientId()));
                }
                familyMembers.add(updateFamilyMember);
            }
        }
        return familyMembers;
    }

    public String updateFamilyMember(String patientId) throws ParseException {
        //add child
        RelatedPerson relatedPerson = new RelatedPerson();
        relatedPerson.setActive(true);
        HumanName name = new HumanName();
        name.setFamily("Bernhard");
        List<StringType> nameList = new ArrayList<>();
        StringType firstName = new StringType();
        firstName.setValue("Bruno");
        nameList.add(firstName);
        name.setGiven(nameList);
        List<HumanName> names = new ArrayList<>();
        names.add(name);
        relatedPerson.setName(names);

        List<CodeableConcept> relationships = new ArrayList<>();
        CodeableConcept relationship = new CodeableConcept();
        Coding coding = new Coding();
        coding.setCode("CHILD");
        coding.setDisplay("child");
        coding.setSystem("http://terminology.hl7.org/CodeSystem/v3-RoleCode");
        List<Coding> codings = new ArrayList<>();
        codings.add(coding);
        relationship.setCoding(codings);
        relationships.add(relationship);
        relatedPerson.setRelationship(relationships);

        Reference reference = new Reference();
        reference.setReference("Patient/"+patientId);
//        reference.setType("Patient");
        relatedPerson.setPatient(reference);

        Date date = new SimpleDateFormat("yyyy-mm-dd").parse("2017-08-11");
        relatedPerson.setBirthDate(date);
        //calling execute method on updated patient obj
        MethodOutcome methodOutcome =  client.create().resource(relatedPerson).execute();
//        MethodOutcome methodOutcome = client.update().resource(patient).execute();
        System.out.println("related person added-"+methodOutcome.getId().getIdPart());
        return methodOutcome.getId().getIdPart();

        //497570
        //updating patient reference
//        RelatedPerson relatedPerson = client.read()
//                .resource(RelatedPerson.class)
//                .withId("497570")
//                .execute();
//        System.out.println(relatedPerson.getBirthDate());
//        Reference reference = new Reference();
//        reference.setReference("Patient/"+patientId);
//        relatedPerson.setPatient(reference);
//        MethodOutcome methodOutcome =  client.update().resource(relatedPerson).execute();
//        System.out.println("related person added-"+methodOutcome.getId().getIdPart());
//        return methodOutcome.getId().getIdPart();
    }

    public String addAllergy(String patientId) {
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

        allergyIntolerance.setType(AllergyIntolerance.AllergyIntoleranceType.ALLERGY);

        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding();
        coding.setCode("387207008");
        coding.setDisplay("Ibuprofen");
        coding.setSystem("http://snomed.info/sct");
        List<Coding> codings = new ArrayList<>();
        codings.add(coding);
        codeableConcept.setCoding(codings);
        allergyIntolerance.setCode(codeableConcept);

        Reference reference = new Reference();
        reference.setReference("Patient/"+patientId);
        allergyIntolerance.setPatient(reference);

        List<AllergyIntolerance.AllergyIntoleranceReactionComponent> allergyIntoleranceReactionComponents = new ArrayList<>();
        AllergyIntolerance.AllergyIntoleranceReactionComponent allergyIntoleranceReactionComponent = new AllergyIntolerance.AllergyIntoleranceReactionComponent();
        List<CodeableConcept> list = new ArrayList<>();
        CodeableConcept codeableConcept1 = new CodeableConcept();
        Coding coding1 = new Coding();
        coding1.setCode("422400008");
        coding1.setDisplay("Vomiting");
        coding1.setSystem("http://snomed.info/sct");
        List<Coding> codings1 = new ArrayList<>();
        codings1.add(coding1);
        codeableConcept1.setCoding(codings1);
        list.add(codeableConcept1);

        allergyIntoleranceReactionComponent.setManifestation(list);
        allergyIntoleranceReactionComponents.add(allergyIntoleranceReactionComponent);
        allergyIntolerance.setReaction(allergyIntoleranceReactionComponents);

        MethodOutcome methodOutcome =  client.create().resource(allergyIntolerance).execute();
//        MethodOutcome methodOutcome = client.update().resource(patient).execute();
        System.out.println("allergy added-"+methodOutcome.getId().getIdPart());
        return methodOutcome.getId().getIdPart();

    }

    public String addFoodAllergy(String patientId) {
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();

        allergyIntolerance.setType(AllergyIntolerance.AllergyIntoleranceType.ALLERGY);

        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding();
        coding.setCode("420174000");
        coding.setDisplay("Allergy to wheat");
        coding.setSystem("http://snomed.info/sct");
        List<Coding> codings = new ArrayList<>();
        codings.add(coding);
        codeableConcept.setCoding(codings);
        allergyIntolerance.setCode(codeableConcept);

        Reference reference = new Reference();
        reference.setReference("Patient/"+patientId);
        allergyIntolerance.setPatient(reference);

//        List<AllergyIntolerance.AllergyIntoleranceReactionComponent> allergyIntoleranceReactionComponents = new ArrayList<>();
//        AllergyIntolerance.AllergyIntoleranceReactionComponent allergyIntoleranceReactionComponent = new AllergyIntolerance.AllergyIntoleranceReactionComponent();
//        List<CodeableConcept> list = new ArrayList<>();
//        CodeableConcept codeableConcept1 = new CodeableConcept();
//        Coding coding1 = new Coding();
//        coding1.setCode("247472004");
//        coding1.setDisplay("Hives");
//        coding1.setSystem("http://snomed.info/sct");
//        List<Coding> codings1 = new ArrayList<>();
//        codings1.add(coding);
//        codeableConcept1.setCoding(codings1);
//        list.add(codeableConcept1);
//
//        allergyIntoleranceReactionComponent.setManifestation(list);
//        allergyIntoleranceReactionComponents.add(allergyIntoleranceReactionComponent);
//        allergyIntolerance.setReaction(allergyIntoleranceReactionComponents);

        MethodOutcome methodOutcome =  client.create().resource(allergyIntolerance).execute();
//        MethodOutcome methodOutcome = client.update().resource(patient).execute();
        System.out.println("allergy added-"+methodOutcome.getId().getIdPart());
        return methodOutcome.getId().getIdPart();

    }
    /*var systolicbp = getBloodPressureValue(byCodes('55284-4'), '8480-6');
      var diastolicbp = getBloodPressureValue(byCodes('55284-4'), '8462-4');
      var hdl = byCodes('2085-9');
      var ldl = byCodes('2089-1');
      var ht = byCodes('8302-2');
      var wt = byCodes('29463-7');*/
    /**
     *
     * @param bundle
     * @param client
     * @param resourceClass
     * @param <T>
     * @return
     */

    static public <T extends IBaseResource> ArrayList<T> getCompleteBundleAsList(Bundle bundle, IGenericClient client, Class<T> resourceClass) {
        // Create List to hold our resources.
        ArrayList<T> list = new ArrayList<T>();

        // The bundle starts on page 1, so before moving forward add all of those Resources to the list.
        list.addAll(BundleUtil.toListOfResourcesOfType(client.getFhirContext(), bundle, resourceClass));

        // Loop through the Bundle based on the presence of a link element with a relation of next.
        while (bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
            // Load the next page.
            bundle = client.loadPage().next(bundle).execute();
            // Add those resources to the list.
            list.addAll(BundleUtil.toListOfResourcesOfType(client.getFhirContext(), bundle, resourceClass));
        }
        return list;
    }
}