import React, { useState } from "react";
import styled from "styled-components";

const FormContainer = styled.div`
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  padding-right: 40px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: -5px 5px 10px rgba(0, 0, 0, 0.1);
`;

const FormTitle = styled.h2`
  font-size: 24px;
  margin-bottom: 20px;
  text-align:center;
  color:black;
`;

const FormGroup = styled.div`
  margin-bottom: 20px;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 5px;
  color:black;
  `;

const Input = styled.input`
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const FileInput = styled.input`
  display: block;
  margin-top: 15px;
  font-size: 15px;
`;

const SubmitButton = styled.button`
  background-color: #007bff;
  font-size: 20px;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #0056b3;
  }
`;

const ResumeForm = ({ onSubmit }) => {
  const [keywords, setKeywords] = useState("");
  const [count, setCount] = useState(0);
  const [resumeFiles, setResumeFiles] = useState([]);

  const handleFileChange = (e) => {
    const files = [...resumeFiles, ...Array.from(e.target.files)];
    setResumeFiles(files);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("count", count);
    formData.append("keywords", keywords);

    resumeFiles.forEach((file) => {
      formData.append("resumeFiles", file);
    });

    try {
      const response = await fetch("http://localhost:7070/topResumes", {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        onSubmit(data);
        setKeywords('');
        setCount(0);
        setResumeFiles([]);
      } else {
        console.error("Error:", response);
      }
    } catch (error) {
      console.error("Error:", error.message);
    }
  };

  return (
    <FormContainer>
      <FormTitle>RESUME FORM</FormTitle>
      <form onSubmit={handleSubmit} encType="multipart/form-data">
        <FormGroup>
          <Label>Keywords (comma-separated):</Label>
          <Input
            type="text"
            value={keywords}
            onChange={(e) => setKeywords(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label>Count:</Label>
          <Input
            type="number"
            value={count}
            onChange={(e) => setCount(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Label>Upload Resumes:</Label>
          <FileInput type="file" multiple onChange={handleFileChange} />
        </FormGroup>
        <div style={{textAlign:"center"}}>
          <SubmitButton type="submit">Submit</SubmitButton>
        </div>
      </form>
    </FormContainer>
  );
};

export default ResumeForm;
