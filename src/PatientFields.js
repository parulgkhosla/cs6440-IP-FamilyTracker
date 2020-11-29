import React from "react";
import classNames from "classnames";
import PropTypes from "prop-types";
import _ from "lodash";

import TrackerLabel from "./TrackerLabel";

import "./PatientFields.css";

const PatientFields = ({
  title,
  value,
  isRequired,
  placeholder,
  grow,
  onChange,
  valueDiffers,
  small,
  error,
}) => {
  return (
    <div
      className={classNames("flex flex-col input-field-outer", {
        grow: !!grow,
        different: valueDiffers,
        small,
        error,
      })}
      style={{
        margin: "auto 0 auto 0",
      }}
    >
      <TrackerLabel title={title} isRequired={isRequired} />
      <input className="input-field"
        value={value || ""}
        // onChange={({ target: { value: nextValue } }) => onChange(nextValue)}
        placeholder={placeholder || _.lowerCase(title)}
      />
    </div>
  );
};

PatientFields.propTypes = {
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]).isRequired,
  onChange: PropTypes.func.isRequired,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  grow: PropTypes.bool,
  isRequired: PropTypes.bool,
  valueDiffers: PropTypes.bool,
  small: PropTypes.bool,
  placeholder: PropTypes.string,
};

PatientFields.defaultProps = {
  isRequired: false,
  grow: false,
  placeholder: null,
  value: null,
  valueDiffers: false,
  small: false,
};

export default PatientFields;
