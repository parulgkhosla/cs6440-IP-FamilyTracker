package api.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import api.model.PatientInfo;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class FHIRService {

    private static final String baseURLHAPI = "https://hapi.fhir.org/baseR4";

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
    public PatientInfo getPatientInfoById(long patientId) {

        Patient patient = client.read()
                .resource(Patient.class)
                .withId(patientId)
                .execute();
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(patient.getIdElement().getIdPart());
        patientInfo.setDisplayName(patient.getNameFirstRep().getNameAsSingleString());
        patientInfo.setFirstName(patient.getName().get(0).getGiven().get(0).getValue());
        patientInfo.setLastName(patient.getName().get(0).getFamily());

        patientInfo.setDateOfBirth(patient.getBirthDate());
        if(patient.getGender() != null)
            patientInfo.setGender(patient.getGender().getDisplay());
        if(patient.getBirthDate() != null)
            patientInfo.setAge(Period.between(patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
        patientInfo.setAddress(patient.getAddressFirstRep().getLine()+" "+patient.getAddressFirstRep().getCity()+
                " "+patient.getAddressFirstRep().getState()+" "+patient.getAddressFirstRep().getPostalCode());

        return patientInfo;
    }

    /**
     *
     * @param patientId
     * @return
     */
    public String getPatientWeightById(String patientId) {
//        response = client.search()
//                .forResource("Observation")
//                .where(Observation.CODE_VALUE_DATE
//                        .withLeft(Observation.CODE.exactly().code("FOO$BAR"))
//                        .withRight(Observation.VALUE_DATE.exactly().day("2001-01-01")))
//                .returnBundle(Bundle.class)
//                .execute();
        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.CODE.exactly().code("29463-7"))
                .and(Observation.PATIENT.hasId(patientId))
                .sort(new SortSpec("date", SortOrderEnum.DESC))
                .returnBundle(Bundle.class)
                .execute();

        //weight
//        "system": "http ://loinc.org",
//                "code": "29463-7"
        List<Observation> observationList = getCompleteBundleAsList(bundle, client, Observation.class);
//        System.out.println(observationList.size());
        String weight = "";
        if(!observationList.isEmpty())
            weight = observationList.get(0).getValueQuantity().getValue().toString()+" "+observationList.get(0).getValueQuantity().getUnit();
        return weight;
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