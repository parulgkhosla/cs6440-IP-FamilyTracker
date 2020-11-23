package api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import api.fhir.FHIRService;
import api.model.PatientInfo;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@RestController
@RequestMapping("/api")
public class PatientInfoController {
    FHIRService fhirService = new FHIRService();

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientInfo> getPatientInfoById(@PathVariable(value = "id") String id)
            throws Exception {
        PatientInfo patientInfo = fhirService.getPatientInfoById(id);

        patientInfo.setWeight(fhirService.getPatientWeightById(patientInfo.getPatientId()));
        patientInfo.setHeight(fhirService.getPatientHeightById(patientInfo.getPatientId()));
        patientInfo.setLdl(fhirService.getPatientLDLById(patientInfo.getPatientId()));
        patientInfo.setHdl(fhirService.getPatientHDLById(patientInfo.getPatientId()));
        patientInfo.setBmi(fhirService.getPatientBMIById(patientInfo.getPatientId()));

        patientInfo.setMedications(fhirService.getMedicationsByPatientId(patientInfo.getPatientId()));
        patientInfo.setAllergies(fhirService.getAllergiesByPatientId(patientInfo.getPatientId()));
        patientInfo.setConditions(fhirService.getConditionsByPatientId(patientInfo.getPatientId()));
        patientInfo.setEncounters(fhirService.getEncountersByPatientId(patientInfo.getPatientId()));
        patientInfo.setImmunizations(fhirService.getImmunizationByPatientId(patientInfo.getPatientId()));
//        fhirService.updateFamilyMember(patientInfo.getPatientId());
        patientInfo.setFamilyMembers(fhirService.getFamilyMemberForPatient(patientInfo.getPatientId()));
        return ResponseEntity.ok().body(patientInfo);
    }

    @GetMapping("/search")
    public ResponseEntity<String> getIDByPatientName(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "familyName") String familyName, @RequestParam(value = "dob") String dob) {
        String patientId="";
        try {
            patientId = fhirService.getPatientIdByNameDob(firstName, familyName, dob);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok().body(patientId);
    }
}
