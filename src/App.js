import React from "react";

import './App.css';
import FamilyTracker from "./FamilyTracker";
import TrackerContainer from "./TrackerContainer";
import TrackerLabel from "./TrackerLabel";

const DEV_MODE = false;

class App extends React.Component {
    constructor(props) {
        super(props);

        this.state = this.getInitialState();

        this.inputRef = React.createRef();
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    getInitialState() {
        if (DEV_MODE) {
            return {
                selectedPatientId: null,
                patientIdText: "9a82c90c-ba2d-4b36-a523-ee13e38b8e39",
            };
        }

        return {
            selectedPatientId: null,
            patientIdText: "",
        };
    }
    componentDidMount() {
        const { patientIdText } = this.state;
        if (patientIdText) {
            this.setState({ selectedPatientId: patientIdText });
        }
    }
    handleSubmit(e) {

        const { patientIdText } = this.state;

        e.preventDefault();
        // alert(patientIdText);
        this.setState({ selectedPatientId: patientIdText });
    }

    render() {
        const { selectedPatientId, patientIdText } = this.state;

        return (
            <div className="app-container">
                <label className="App-header">Family Health Tracker</label>
                    <TrackerContainer
                        title="Enter a PatientId below"
                        trackerComponents={
                            <div className="flex" style={{ flex: 1, flexWrap: "wrap" }}>
                                <div className="flex flex-col" style={{ flex: 1 }}>
                                    <div className="flex" style={{ flexWrap: "wrap" }}>
                                        <TrackerLabel title="Patient ID (Self)" />
                                        <form onSubmit={this.handleSubmit}>
                                            <input
                                                style={{ margin: "0px 10px" }}
                                                className="input-field"
                                                ref={this.inputRef}
                                                value={patientIdText}
                                                onChange={(e) =>
                                                    this.setState({ patientIdText: e.target.value })
                                                }
                                            />
                                            <button
                                                type="submit"
                                                className="submit-button"
                                                style={{ background: "lightcoral", color:"white", margin: "5px 5px" }}
                                                disabled={!patientIdText}
                                            >
                                                Fetch
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        }
                    />
                    <FamilyTracker selectedPatientId={selectedPatientId} />
            </div>
        );
    }
}

export default App;
