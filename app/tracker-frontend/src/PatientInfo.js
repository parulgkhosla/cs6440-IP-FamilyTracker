import React from "react";
import PropTypes from "prop-types";

import TrackerContainer from "./TrackerContainer";
import { loadPatientInfoById } from "./apiHelpers";
import TrackerLabel from "./TrackerLabel";
import TextLabel from "./TextLabel";
import SectionLabel from "./SectionLabel";
import classNames from "classnames";

const Cell = ({ value, onChange }) => (
    <div style={{ flex: 1, display: "flex", margin: "0.25em" }}>
      <label
          style={{ width: "1em", flex: 1 }}
          className="text-label"
      />
    </div>
);

const TableRow = ({ title}) => (
    <td className={classNames("table-label")}>
    {title}{" "}
  </td>
);

const PatientInfo = ({ selectedPatientId }) => {
  // const [patientAnswers, setAnswers] = React.useState({}); // hold patient/user responses
  const [patientData, setPatientData] = React.useState({}); // "immutable" copy of fhir data

  const [error, setError] = React.useState(null);
  const [isLoading, setLoading] = React.useState(false);
  const ColoredLine = ({ color }) => (
      <hr
          style={{
            color: color,
            backgroundColor: color,
            height: "0.01em"
          }}
      />
  );

  React.useEffect(() => {
    // alert("selectedPatientId");
    // alert(selectedPatientId);
    if (selectedPatientId) {
      loadPatientInfoById({
        patientId: selectedPatientId,
        setData: (data) => {
          // const [result] = data || [];
          if (data) {
            // setAnswers({ ...data }); // shallow copy
            setPatientData({ ...data }); // shallow copy
          }
        },
        setError,
        setLoading,
      });
    }
  }, [selectedPatientId]);

  const setFieldValue = (key, value) =>
    setPatientData({ ...patientData, [key]: value });

  return (
    <TrackerContainer
      title="Health Info (Self)"
      key="form-group-0"
      trackerComponents={
        <div className="flex" style={{ flex: 1, flexWrap: "wrap" }}>
          <div className="flex flex-col" style={{ flex: 1 }}>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Name" />
              <TextLabel title={patientData.firstName} />
              <TextLabel title={patientData.lastName} />
            </div>
            <div className="flex">
              <TrackerLabel title="Address" />
              <TextLabel title={patientData.address} />
            </div>
            <div className="flex">
              <TrackerLabel title="Birth Date" />
              <TextLabel title={patientData.dateOfBirth} />
            </div>
            <div className="flex">
              <TrackerLabel title="Age" />
              <TextLabel title={patientData.age} />
            </div>
          </div>
          <div className="flex flex-col" style={{ flex: 1, flexWrap: "wrap" }}>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <div className="flex" style={{ flexWrap: "wrap" }}>
                <TrackerLabel title="Height" />
                <TextLabel title={patientData.height} />
              </div>
              <TrackerLabel title="Weight" />
              <TextLabel title={patientData.weight} />
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="BMI" />
              {/*<TextLabel title={patientData.bmi} />*/}

              <span style={{ padding: '50px', fontSize: '0.9em', position: 'relative', color:
                    patientData.bmi <= 18.5?'yellow': patientData.bmi < 25?
                        'green':patientData.bmi >= 25?'orange': 'red'}}>{patientData.bmi} kg/m2</span>
            </div>
            <div>
              <TrackerLabel title="HDL" />
              <TextLabel title={patientData.hdl} />
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="LDL" />
              <TextLabel title={patientData.ldl} />
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Medications" />
              {
                patientData.medications && patientData.medications.map(med => {
                  return med && (
                      <ul style={{ margin: "0px 0px" }}><TextLabel title={med}/></ul>
                  )
                })
              }
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Allergies" />
              {
                patientData.allergies && patientData.allergies.map(allergy => {
                  return allergy && (
                      <ul style={{ margin: "0px 0px" }}><TextLabel title={allergy}/></ul>
                  )
                })
              }
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Conditions" />
              {
                patientData.conditions && patientData.conditions.map(cond => {
                  return cond && (
                      <ul style={{ margin: "0px 0px" }}><TextLabel title={cond}/></ul>
                  )
                })
              }
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Recent Encounters" />
              {/*{*/}
              {/*  patientData.encounters && patientData.encounters.map(encounter => {*/}
              {/*    return encounter && (*/}
              {/*        <ul style={{ margin: "0px 0px" }}><TextLabel title={encounter}/></ul>*/}
              {/*    )*/}
              {/*  })*/}
              {/*}*/}
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col">
                  <table>
                    <tbody>
                    <div className="flex">
                      <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                        <TableRow title='Encounter'/>
                        <TableRow title='Date Recorded'/>
                      </tr>
                    </div>
                    {patientData.encounters && patientData.encounters.map(enc => {
                      const e = enc.split("|")
                      return (
                          <div className="flex">
                            <tr style={{width: "20em", flex: 1}}>
                              <TableRow title={e[0]}/>
                              <TableRow title={e[1]}/>
                            </tr>
                          </div>
                      );
                    })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Immunizations (completed)" />
              {/*<TextLabel title={patientData.immunizations} />*/}
              {
                patientData.immunizations && patientData.immunizations.map(immunization => {
                  return immunization && (
                      <ul style={{ margin: "0px 0px" }}><TextLabel title={immunization}/></ul>
                  )
                })
              }
            </div>
            <div>

              {
                patientData.familyMembers && patientData.familyMembers.map(member => {
                  return member && (
                      <div className="flex flex-col" style={{ flex: 1 }}>
                        <ColoredLine color="white"/>
                        <SectionLabel title="Family Member"/>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Name" />
                          <TextLabel title={member.firstName}/>
                          <TextLabel title={member.familyName} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="Relation" />
                          <TextLabel title={member.relationship} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="Birth Date" />
                          <TextLabel title={member.dateOfBirth} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="Age" />
                          <TextLabel title={member.age} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="Address" />
                          <TextLabel title={member.address} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="Height" />
                          <TextLabel title={member.height} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="Weight" />
                          <TextLabel title={member.weight} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="BMI" />
                          {/*<TextLabel title={member.bmi} />*/}
                          <span style={{ padding: '50px', fontSize: '0.9em', position: 'relative', color:
                                member.bmi <= 18.5?'yellow': member.bmi < 25?
                                    'green':member.bmi >= 25?'orange': 'red'}}>{member.bmi} kg/m2</span>
                        </div>
                        <div className="flex">
                          <TrackerLabel title="HDL" />
                          <TextLabel title={member.hdl} />
                        </div>
                        <div className="flex">
                          <TrackerLabel title="LDL" />
                          <TextLabel title={member.ldl} />
                        </div>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Medications" />
                          {
                            member.medications && member.medications.map(med => {
                              return med && (
                                  <ul style={{ margin: "0px 0px" }}><TextLabel title={med}/></ul>
                              )
                            })
                          }
                        </div>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Allergies" />
                          {
                            member.allergies && member.allergies.map(allergy => {
                              return allergy && (
                                  <ul style={{ margin: "0px 0px" }}><TextLabel title={allergy}/></ul>
                              )
                            })
                          }
                        </div>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Conditions" />
                          {
                            member.conditions && member.conditions.map(cond => {
                              return cond && (
                                  <ul style={{ margin: "0px 0px" }}><TextLabel title={cond}/></ul>
                              )
                            })
                          }
                        </div>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Recent Encounters" />
                          {/*{*/}
                          {/*  member.encounters && member.encounters.map(encounter => {*/}
                          {/*    return encounter && (*/}
                          {/*        <ul style={{ margin: "0px 0px" }}><TextLabel title={encounter}/></ul>*/}
                          {/*    )*/}
                          {/*  })*/}
                          {/*}*/}
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            <div className="flex flex-col">
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                                    <TableRow title='Encounter'/>
                                    <TableRow title='Date Recorded'/>
                                  </tr>
                                </div>
                                {member.encounters && member.encounters.map(enc => {
                                  const e = enc.split("|")
                                  return (
                                      <div className="flex">
                                        <tr style={{width: "20em", flex: 1}}>
                                          <TableRow title={e[0]}/>
                                          <TableRow title={e[1]}/>
                                        </tr>
                                      </div>
                                  );
                                })}
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Immunizations" />
                          {
                            patientData.immunizations && patientData.immunizations.map(immunization => {
                              return immunization && (
                                  <ul style={{ margin: "0px 0px" }}><TextLabel title={immunization}/></ul>
                              )
                            })
                          }
                        </div>
                      </div>

                  )
                })
              }
            </div>
          </div>
        </div>
      }
    />
  );
};

PatientInfo.propTypes = {
  selectedPatientId: PropTypes.string.isRequired,
};


export default PatientInfo;
