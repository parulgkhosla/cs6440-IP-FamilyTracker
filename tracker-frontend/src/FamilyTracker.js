import React from "react";
import PropTypes from "prop-types";

import PatientInfo from "./PatientInfo";

const FamilyTracker = ({ selectedPatientId, selectedMemberId }) => {
  return (
    <>
      <PatientInfo
        selectedPatientId={selectedPatientId}
        handleSubmit={() => {}}
      />
    </>
  );
};

FamilyTracker.propTypes = {
  selectedPatientId: PropTypes.string,
    selectedMemberId: PropTypes.string
};

FamilyTracker.defaultProps = {
  selectedPatientId: null,
    selectedMemberId: null
};

export default FamilyTracker;
