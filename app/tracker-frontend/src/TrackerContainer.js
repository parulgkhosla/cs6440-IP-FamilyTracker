import React from "react";
import PropTypes from "prop-types";
import classnames from "classnames";

import "./TrackerContainer.css";

const TrackerContainer = ({ title, trackerComponents }) => {
  const isError = false;

  return (
    <div
      className={classnames("flex flex-col tracker-container", {
        error: isError,
        complete: false,
      })}
    >
      <span className="container-title">{title}</span>
      <div className="flex">{trackerComponents}</div>
    </div>
  );
};

TrackerContainer.propTypes = {
  title: PropTypes.oneOfType([PropTypes.node, PropTypes.string]).isRequired,
  trackerComponents: PropTypes.node.isRequired,
};

export default TrackerContainer;
