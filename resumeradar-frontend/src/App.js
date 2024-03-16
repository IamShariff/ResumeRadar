import React, { useEffect, useState } from "react";
import ResumeForm from "./components/ResumeForm";
import { GiRadarSweep } from "react-icons/gi";
import "../src/App.css";

function App() {
  const [resumeData, setResumeData] = useState([]);
  const [visible, setVisible] = useState(true);
  const [radarDisplay, setRadarDisplay] = useState(true);
  const [showResult, setShowResult] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(false);
    }, 5000); 

    const radarDisplayTime = setTimeout(() => {
      setRadarDisplay(false);
    }, 6000);

    return () => clearTimeout(timer,radarDisplayTime);
  }, []); 

  const handleFormSubmit = (data) => {
    console.log("Form submitted with data:", data);
    // setShowResult
    setResumeData(data);
  };

  return (
    <div className="app">
      <div className={visible ? "loader" : "loader fade-out"}>
        <div className={radarDisplay ?"outer-circle":"display-none"}>
          <div className="inner-circle"></div>
          <ul className="resume-topics">
            <li>Java</li>
            <li>Ruby</li>
            <li>React JS</li>
            <li>Angular</li>
            <li>MySQL</li>
          </ul>
        </div>
      </div>
        <div className={visible?"hidden":"heading"}>
          <h1>RESUME RADAR &nbsp;
          <GiRadarSweep className="radar-icon" />
          </h1> 
        </div>
      <div className={visible?"hidden":"fade-in"}>
          <ResumeForm onSubmit={handleFormSubmit} />
        <div className={resumeData.length>0 ? "result-div": "hidden"}>
          <h2 className="result-div-heading">RESUME DATA</h2>
          <table className="table-box">
            <thead className="table-head">
              <th>Sno.</th>
              <th>File Name</th>
              <th>Analysis Result</th>
            </thead>
            <tbody className="table-body">
            {resumeData.map((resume, index) => (
              <tr key={index} className="table-row">
                <td>{index+1}</td>
                <td>{resume.resumeName}</td>
                <td> {resume.resumeScore}%</td>
              </tr>
            ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
export default App;
