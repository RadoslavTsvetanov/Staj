"use client"
import { uploadFileRequest } from "@/utils/api/memory";
import React, { useState, useLayoutEffect } from "react";

type Stringish = string | null;

const FileUploadForm = () => {
  const [file, setFile] = useState(null);
  const [username, setUsername] = useState("");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormState((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const [status, error, response, submit] = uploadFileRequest()

  const handleSubmit = async (event) => {
    event.preventDefault();

    submit(file, username)
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Upload Form</h2>
      <form onSubmit={handleSubmit} style={styles.form}>
        <div style={styles.formGroup}>
          <label htmlFor="file" style={styles.label}>
            Choose a JPEG file:
          </label>
          <input
            type="file"
            id="file"
            name="file"
            onChange={handleFileChange}
            style={styles.input}
          />
        </div>
        <div style={styles.formGroup}>
          <label htmlFor="place" style={styles.label}>
            Place:
          </label>
          <input
            type="text"
            id="place"
            name="place"
            value={formState.place}
            onChange={handleChange}
            style={styles.input}
          />
        </div>
        <button type="submit" disabled={status == 'loading'}>
          {status == 'loading' ? "Submitting..." : "Submit"}
        </button>
      </form>
      {status == 'failed' && <p style={{ color: "red" }}>{error}</p>}
      {status == 'success' && <p style={{ color: "green" }}>{response}</p>}
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "600px",
    margin: "0 auto",
    padding: "20px",
    borderRadius: "8px",
    boxShadow: "0 0 10px rgba(0, 0, 0, 0.1)",
    backgroundColor: "#f9f9f9",
  },
  heading: {
    textAlign: "center",
    marginBottom: "20px",
    color: "#333",
  },
  form: {
    display: "flex",
    flexDirection: "column" as const,
  },
  formGroup: {
    marginBottom: "15px",
  },
  label: {
    marginBottom: "5px",
    fontWeight: "bold",
    color: "#555",
  },
  input: {
    padding: "10px",
    borderRadius: "4px",
    border: "1px solid #ccc",
    width: "100%",
    boxSizing: "border-box" as const,
  },
  submitButton: {
    padding: "10px 15px",
    borderRadius: "4px",
    border: "none",
    backgroundColor: "#007BFF",
    color: "#fff",
    cursor: "pointer",
    transition: "background-color 0.3s",
  },
  error: {
    color: "red",
    marginTop: "10px",
    textAlign: "center",
  },
  success: {
    color: "green",
    marginTop: "10px",
    textAlign: "center",
  },
};

export default FileUploadForm;
