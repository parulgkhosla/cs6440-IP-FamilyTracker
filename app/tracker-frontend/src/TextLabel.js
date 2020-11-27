import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

import "./TextInput.css";

const TextLabel = ({ title, isRequired, small }) => (
  <span className={classNames("text-label", { small })}>
    {title}{" "}
    {isRequired ? (
      <strong style={{ color: "red", fontSize: "1.1em" }}>*</strong>
    ) : null}
  </span>
);

TextLabel.propTypes = {
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]).isRequired,
  isRequired: PropTypes.bool,
  small: PropTypes.bool,
};

TextLabel.defaultProps = {
  isRequired: false,
  small: false,
};

export default TextLabel;
