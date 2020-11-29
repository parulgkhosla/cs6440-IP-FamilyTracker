import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import "./PatientFields.css";

const TrackerLabel = ({ title, isRequired, small }) => (
  <span className={classNames("field-title", { small })}>
    {title}{" "}
    {isRequired ? (
      <strong style={{ color: "red", fontSize: "1.1em" }}>*</strong>
    ) : null}
  </span>
);

TrackerLabel.propTypes = {
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]).isRequired,
  isRequired: PropTypes.bool,
  small: PropTypes.bool,
};

TrackerLabel.defaultProps = {
  isRequired: false,
  small: false,
};

export default TrackerLabel;
