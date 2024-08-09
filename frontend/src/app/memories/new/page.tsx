"use client"
import React, { useState } from "react";

const FileUploadForm = () => {
  const [file, setFile] = useState(null);
  const [username, setUsername] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!file || !username) {
      setError("Please provide both a file and a username.");
      return;
    }

    
    setIsSubmitting(true);
    setError(null);
    setSuccess(null);

    const formData = new FormData();
    formData.append("file", file);
    formData.append("username", username);

    try {
      const response = await fetch("http://localhost:4550/api/files/upload", {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        setSuccess("File and username uploaded successfully!");
      } else {
        setError("Failed to upload file.");
      }
    } catch (err) {
      setError("An error occurred during upload.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <h2>Upload Form</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="file">Choose a JPEG file:</label>
          <input
            type="file"
            id="file"
            onChange={handleFileChange}
          />
        </div>
        <div>
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={handleUsernameChange}
          />
        </div>
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Submitting..." : "Submit"}
        </button>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}
    </div>
  );
};

export default FileUploadForm;
