import { useState } from 'react';
import axios from 'axios';


const YourComponent = () => {
  const [file, setFile] = useState(null);
  const [processingResults, setProcessingResults] = useState({
    employees: [],
    jobSummary: [],
  });

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    try {
      const formData = new FormData();
      formData.append('file', file);
  
      const uploadResponse = await axios.post('http://localhost:8083/employees/uploadEmployees', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
  
      const averageSalariesFormData = new FormData();
      averageSalariesFormData.append('file', file); 
  
      const averageSalariesResponse = await axios.post('http://localhost:8083/employees/average-salaries', averageSalariesFormData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
  
      setProcessingResults({
        employees: uploadResponse.data,
        jobSummary: averageSalariesResponse.data,
      });
    } catch (error) {
      console.error('Error processing file:', error);
    }
  };
  

  return (
    <div className="TableContainer">
      <div className="content">
          <div className="FileInputContainer">
            <label htmlFor="fileInput" className="FileInputLabel">
              Choose a file
            </label>
            <input
              type="file"
              id="fileInput"
              className="FileInput"
              onChange={handleFileChange}
            />
          </div>
          {file && (
            <button className="Button" onClick={handleUpload}>
              Process
            </button>
          )}
        </div>


      <div className="content">
        <div>
          <h2>Employee Information</h2>
          <table className="EmployeeTable">
            <thead>
              <tr className="TableRow">
                <th className="TableHeader">ID</th>
                <th className="TableHeader">Name</th>
                <th className="TableHeader">Job Title</th>
                <th className="TableHeader">Salary</th>
              </tr>
            </thead>
            <tbody>
              {processingResults.employees.map((employee) => (
                <tr className="TableRow" key={employee.id}>
                  <td className="TableCell">{employee.id}</td>
                  <td className="TableCell">{employee.employeeName}</td>
                  <td className="TableCell">{employee.jobTitle}</td>
                  <td className="TableCell">{employee.salary}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div>
          <h2>Jobs Summary</h2>
          <table className="JobSummaryTable">
            <thead>
              <tr className="TableRow">
                <th className="TableHeader">Job Title</th>
                <th className="TableHeader">Average Salary</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(processingResults.jobSummary).map(([jobTitle, averageSalary]) => (
                <tr className="TableRow" key={jobTitle}>
                  <td className="TableCell">{jobTitle}</td>
                  <td className="TableCell">{averageSalary}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default YourComponent;