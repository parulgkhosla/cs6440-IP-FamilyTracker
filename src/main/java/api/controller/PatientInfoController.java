package api.controller;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import api.fhir.FHIRService;
import api.model.PatientInfo;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PatientInfoController {
    FHIRService fhirService = new FHIRService();

    @CrossOrigin
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientInfo> getPatientInfoById(@PathVariable(value = "id") String id)
            throws Exception {
        PatientInfo patientInfo = fhirService.getPatientInfoById(id);
        PatientInfo updatePatientInfo = fhirService.getPatientVitalsByPatientId(patientInfo);
//        List<Observation> weightList = fhirService.getPatientWeightById(patientInfo.getPatientId());

        updatePatientInfo.setMedications(fhirService.getMedicationsByPatientId(id));
//        fhirService.addAllergy(id);
//        fhirService.addFoodAllergy(id);
        updatePatientInfo.setAllergies(fhirService.getAllergiesByPatientId(id));
        updatePatientInfo.setConditions(fhirService.getConditionsByPatientId(id));
        updatePatientInfo.setEncounters(fhirService.getEncountersByPatientId(id));
        updatePatientInfo.setImmunizations(fhirService.getImmunizationByPatientId(id));
//        fhirService.updateFamilyMember(id);

        updatePatientInfo.setFamilyMembers(fhirService.getFamilyMemberForPatient(id));
        return ResponseEntity.ok().body(updatePatientInfo);
    }

    @CrossOrigin
    @GetMapping("/search")
    public ResponseEntity<Patient> getIDByPatientName(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "familyName") String familyName, @RequestParam(value = "dob") String dob) {
        Patient familyMember = null;
        try {
            familyMember = fhirService.getPatientInfoByNameDob(firstName, familyName, dob);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok().body(familyMember);
    }
}
