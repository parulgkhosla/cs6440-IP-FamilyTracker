import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import "./PatientFields.css";

const SectionLabel = ({ title, isRequired, small }) => (
  <span className={classNames("section-title", { small })}>
    {title}{" "}
    {isRequired ? (
      <strong style={{ color: "red", fontSize: "1.1em" }}>*</strong>
    ) : null}
  </span>
);

SectionLabel.propTypes = {
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]).isRequired,
  isRequired: PropTypes.bool,
  small: PropTypes.bool,
};

SectionLabel.defaultProps = {
  isRequired: false,
  small: false,
};

export default SectionLabel;
