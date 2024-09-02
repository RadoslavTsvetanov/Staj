"use client"
import { uploadFileRequest } from "@/utils/api/memory";
import React, { useState, useLayoutEffect } from "react";

const FileUploadForm = () => {
  const [file, setFile] = useState(null);
  const [username, setUsername] = useState("");

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
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
        <button type="submit" disabled={status == 'loading'}>
          {status == 'loading' ? "Submitting..." : "Submit"}
        </button>
      </form>
      {status == 'failed' && <p style={{ color: "red" }}>{error}</p>}
      {status == 'success' && <p style={{ color: "green" }}>{response}</p>}
    </div>
  );
};

export default FileUploadForm;
