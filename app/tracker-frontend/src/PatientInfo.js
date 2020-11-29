import React from "react";
import PropTypes from "prop-types";

import TrackerContainer from "./TrackerContainer";
import { loadPatientInfoById } from "./apiUtils";
import TrackerLabel from "./TrackerLabel";
import SectionLabel from "./SectionLabel";
import classNames from "classnames";
import ClipLoader from "react-spinners/ClipLoader";
import {BarLoader} from "react-spinners";

const TableRow = ({ title}) => (
    <td className={classNames("table-label")}>
    {title}{" "}
  </td>
);

const HeaderRow = ({ title}) => (
    <td className={classNames("header-label")}>
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
          if (data) {
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
          <div style={{margin:"10px auto",display:"block", width:"100%"}}>
          <BarLoader
              width={"90%"}
              color={"white"}
              loading={isLoading}
          /></div>
          <div className="flex flex-col" style={{ flex: 1 }}>
            <TrackerLabel title="Personal Info" />
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col" style={{display: patientData.firstName !== undefined? 'block':'none'}}>
                  <table>
                    <tbody>
                    <div className="flex">
                      <tr style={{width: "20em", flex: 1}}>
                        <HeaderRow title="Name"/>
                        {patientData.firstName !== undefined ?
                            <TableRow title={patientData.firstName+" "+patientData.lastName}/>: <TableRow title="N/A"/>}
                      </tr>
                    </div>
                    </tbody>
                  </table>
                </div>

                <div className="flex flex-col" style={{display: patientData.address !== undefined? 'block':'none'}}>
                  <table>
                    <tbody>
                          <div className="flex">
                            <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="Address"/>
                              {patientData.address !== undefined ?
                                  <TableRow title={patientData.address}/>: <TableRow title="N/A"/>}
                            </tr>
                          </div>
                    </tbody>
                  </table>
                </div>
              </div>
            <div className="flex flex-col" style={{display: patientData.dateOfBirth !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="Birth Date"/>
                    {patientData.dateOfBirth !== undefined ?
                        <TableRow title={patientData.dateOfBirth}/>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>

            <div className="flex flex-col" style={{display: patientData.age !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="Age"/>
                    {patientData.age !== undefined ?
                        <TableRow title={patientData.age}/>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>
            <div className="flex flex-col" style={{display: patientData.height !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="Height"/>
                    {patientData.height !== undefined ?
                        <TableRow title={patientData.height}/>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>

            <div className="flex flex-col" style={{display: patientData.weight !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="Weight"/>
                    {patientData.weight !== undefined ?
                        <TableRow title={patientData.weight}/>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>

            <div className="flex flex-col" style={{display: patientData.bmi !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="BMI"/>
                    {patientData.bmi !== undefined ?
                        <td className={classNames("table-label")} style={{fontSize: '0.9em', position: 'relative', color:
                              patientData.bmi <= 18.5?'yellow': patientData.bmi < 25?
                                  'green':patientData.bmi < 30?'orange': 'red',
                          visibility: patientData.bmi !== undefined? 'visible':'hidden'}}>{patientData.bmi}</td>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>
            <div className="flex flex-col" style={{display: patientData.hdl !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="HDL"/>
                    {patientData.hdl !== undefined ?
                        <TableRow title={patientData.hdl}/>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>

            <div className="flex flex-col" style={{display: patientData.ldl !== undefined? 'block':'none'}}>
              <table>
                <tbody>
                <div className="flex">
                  <tr style={{width: "20em", flex: 1}}>
                    <HeaderRow title="LDL"/>
                    {patientData.ldl !== undefined ?
                        <TableRow title={patientData.ldl}/>: <TableRow title="N/A"/>}
                  </tr>
                </div>
                </tbody>
              </table>
            </div>

          </div>
          <div className="flex flex-col" style={{ flex: 1, flexWrap: "wrap" }}>
            <div className="flex" style={{ flexWrap: "wrap" }}>
              <TrackerLabel title="Medications" />
              {/*{*/}
              {/*  patientData.medications && patientData.medications.map(med => {*/}
              {/*    return med && (*/}
              {/*        <ul style={{ margin: "0px 0px" }}><TextLabel title={med}/></ul>*/}
              {/*    )*/}
              {/*  })*/}
              {/*}*/}
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col" style={{display: patientData.medications !== undefined? 'block':'none'}}>
                  <table>
                    <tbody>
                    <div className="flex">
                      <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                        <TableRow title='Medication'/>
                        <TableRow title='Dosage Frequency'/>
                      </tr>
                    </div>
                    {patientData.medications && patientData.medications.map(enc => {
                      const e = enc.split("|")
                      return (
                          <div className="flex">
                            <tr style={{width: "20em", flex: 1}}>
                              {e[0] !== undefined ?
                              <TableRow title={e[0]}/>: <TableRow title="N/A"/>}
                              {e[1] !== undefined ?
                              <TableRow title={e[1] +" "+ e[2]}/>: <TableRow title="N/A"/>
                              }
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
              <TrackerLabel title="Allergies" />
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col" style={{display: patientData.allergies !== undefined? 'block':'none'}}>
                  <table>
                    <tbody>
                    <div className="flex">
                      <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                        <TableRow title='Allergy'/>
                        <TableRow title='Reaction'/>
                      </tr>
                    </div>
                    {patientData.allergies && patientData.allergies.map(all => {
                      const a = all.split("|")
                      return (
                          <div className="flex">
                            <tr style={{width: "20em", flex: 1}}>
                              {a[0] !== undefined ?
                                  <TableRow title={a[0]}/>: <TableRow title="N/A"/>}
                              {a[1] !== undefined ?
                                  <TableRow title={a[1]}/>: <TableRow title="N/A"/>
                              }
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
              <TrackerLabel title="Conditions" />
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col" style={{display: patientData.conditions !== undefined? 'block':'none'}}>
                  <table>
                    <tbody>
                    {patientData.conditions && patientData.conditions.map(cond => {
                      return (
                          <div className="flex">
                            <tr style={{width: "20em", flex: 1}}>
                              {cond !== undefined ?
                                  <TableRow title={cond}/>: <TableRow title="N/A"/>}
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
              <TrackerLabel title="Recent Encounters" />
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col" style={{display: patientData.encounters !== undefined? 'block':'none'}}>
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
              <div className="flex flex-col" style={{ marginTop: "1em" }}>
                <div className="flex flex-col" style={{display: patientData.immunizations !== undefined? 'block':'none'}}>
                  <table>
                    <tbody>
                    {patientData.immunizations && patientData.immunizations.map(imm => {
                      return (
                          <div className="flex">
                            <tr style={{width: "20em", flex: 1}}>
                              {imm !== undefined ?
                                  <TableRow title={imm}/>: <TableRow title="N/A"/>}
                            </tr>
                          </div>
                      );
                    })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div>

              {
                patientData.familyMembers && patientData.familyMembers.map(member => {
                  return member && (
                      <div className="flex flex-col" style={{ flex: 1 }}>
                        <ColoredLine color="white"/>
                        <SectionLabel title="Family Member"/>
                        <div className="flex flex-col" style={{ flex: 1 }}>
                          <TrackerLabel title="Personal Info" />
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            <div className="flex flex-col" style={{display: member.firstName !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1}}>
                                    <HeaderRow title="Name"/>
                                    {member.firstName !== undefined ?
                                        <TableRow title={member.firstName+" "+member.familyName}/>: <TableRow title="N/A"/>}
                                  </tr>
                                </div>
                                </tbody>
                              </table>
                            </div>
                            <div className="flex flex-col" style={{display: member.relationship !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1}}>
                                    <HeaderRow title="Relation"/>
                                    {member.relationship !== undefined ?
                                        <TableRow title={member.relationship}/>: <TableRow title="N/A"/>}
                                  </tr>
                                </div>
                                </tbody>
                              </table>
                            </div>
                            <div className="flex flex-col" style={{display: member.address !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1}}>
                                    <HeaderRow title="Address"/>
                                    {member.address !== undefined ?
                                        <TableRow title={member.address}/>: <TableRow title="N/A"/>}
                                  </tr>
                                </div>
                                </tbody>
                              </table>
                            </div>
                          </div>
                          <div className="flex flex-col" style={{display: member.birthDate !== undefined? 'block':'none'}}>
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="Birth Date"/>
                                  {member.birthDate !== undefined ?
                                      <TableRow title={member.birthDate}/>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>

                          <div className="flex flex-col" style={{display: member.age !== undefined? 'block':'none'}}>
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="Age"/>
                                  {member.age !== undefined ?
                                      <TableRow title={member.age}/>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>
                          <div className="flex flex-col"  style={{display: member.relationship !== undefined? 'block':'none'}}>
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="Height"/>
                                  {member.height !== undefined ?
                                      <TableRow title={member.height}/>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>

                          <div className="flex flex-col"  style={{display: member.relationship !== undefined? 'block':'none'}}>
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="Weight"/>
                                  {member.weight !== undefined ?
                                      <TableRow title={member.weight}/>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>

                          <div className="flex flex-col" style={{display: member.bmi !== undefined? 'block':'none'}}>
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="BMI"/>
                                  {member.bmi !== undefined ?
                                      <td className={classNames("table-label")} style={{fontSize: '0.9em', position: 'relative', color:
                                            member.bmi <= 18.5?'yellow': member.bmi < 25?
                                                'green':member.bmi < 30?'orange': 'red',
                                        visibility: member.bmi !== undefined? 'visible':'hidden'}}>{member.bmi}</td>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>
                          <div className="flex flex-col" >
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="HDL"/>
                                  {member.hdl !== undefined ?
                                      <TableRow title={member.hdl}/>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>

                          <div className="flex flex-col" >
                            <table>
                              <tbody>
                              <div className="flex">
                                <tr style={{width: "20em", flex: 1}}>
                                  <HeaderRow title="LDL"/>
                                  {member.ldl !== undefined ?
                                      <TableRow title={member.ldl}/>: <TableRow title="N/A"/>}
                                </tr>
                              </div>
                              </tbody>
                            </table>
                          </div>

                        </div>
                        <div className="flex" style={{ flexWrap: "wrap" }}>
                          <TrackerLabel title="Medications" />
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            {member.medications}
                            <div className="flex flex-col" style={{display: member.medications !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                                    <TableRow title='Medication'/>
                                    <TableRow title='Dosage Frequency'/>
                                  </tr>
                                </div>
                                {member.medications && member.medications.map(med => {
                                  const e = med.split("|")
                                  return (
                                      <div className="flex">
                                        <tr style={{width: "20em", flex: 1}}>
                                          {e[0] !== undefined ?
                                              <TableRow title={e[0]}/>: <TableRow title="N/A"/>}
                                          {e[1] !== undefined ?
                                              <TableRow title={e[1] +" "+ e[2]}/>: <TableRow title="N/A"/>
                                          }
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
                          <TrackerLabel title="Allergies" />
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            <div className="flex flex-col" style={{display: member.allergies !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                                    <TableRow title='Allergy'/>
                                    <TableRow title='Reaction'/>
                                  </tr>
                                </div>
                                {member.allergies && member.allergies.map(aller => {
                                  const e = aller.split("|")
                                  return (
                                      <div className="flex">
                                        <tr style={{width: "20em", flex: 1}}>
                                          {e[0] !== undefined ?
                                              <TableRow title={e[0]}/>: <TableRow title="N/A"/>}
                                          {e[1] !== undefined ?
                                              <TableRow title={e[1]}/>: <TableRow title="N/A"/>
                                          }
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
                          <TrackerLabel title="Conditions" />
                          {/*{*/}
                          {/*  patientData.conditions && patientData.conditions.map(cond => {*/}
                          {/*    return cond && (*/}
                          {/*        <ul style={{ margin: "0px 0px" }}><TextLabel title={cond}/></ul>*/}
                          {/*    )*/}
                          {/*  })*/}
                          {/*}*/}
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            <div className="flex flex-col" style={{display: member.conditions !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                {member.conditions && member.conditions.map(condi => {
                                  return (
                                      <div className="flex">
                                        <tr style={{width: "20em", flex: 1}}>
                                          {condi !== undefined ?
                                              <TableRow title={condi}/>: <TableRow title="N/A"/>}
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
                          <TrackerLabel title="Recent Encounters" />
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            <div className="flex flex-col" style={{display: member.encounters !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                <div className="flex">
                                  <tr style={{width: "20em", flex: 1, fontWeight: 'bold'}}>
                                    <TableRow title='Encounter'/>
                                    <TableRow title='Date Recorded'/>
                                  </tr>
                                </div>
                                {member.encounters && member.encounters.map(enco => {
                                  const e = enco.split("|")
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
                          <div className="flex flex-col" style={{ marginTop: "1em" }}>
                            <div className="flex flex-col" style={{display: member.immunizations !== undefined? 'block':'none'}}>
                              <table>
                                <tbody>
                                {member.immunizations && member.immunizations.map(immu => {
                                  return (
                                      <div className="flex">
                                        <tr style={{width: "20em", flex: 1}}>
                                          {immu !== undefined ?
                                              <TableRow title={immu}/>: <TableRow title="N/A"/>}
                                        </tr>
                                      </div>
                                  );
                                })}
                                </tbody>
                              </table>
                            </div>
                          </div>
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
