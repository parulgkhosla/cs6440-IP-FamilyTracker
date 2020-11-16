package api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import api.fhir.FHIRService;
import api.model.PatientInfo;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class PatientInfoController{
    FHIRService fhirService = new FHIRService();

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientInfo> getPatientInfoById(@PathVariable(value="id")long id)
            throws Exception {
        PatientInfo patientInfo = fhirService.getPatientInfoById(id);

        patientInfo.setWeight(fhirService.getPatientWeightById(patientInfo.getPatientId()));

        return ResponseEntity.ok().body(patientInfo);
    }

}
