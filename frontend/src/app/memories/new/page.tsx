"use client";
import { cookies } from "@/lib/utils";
import React, { useState } from "react";

type Stringish = string | null;

const FileUploadForm = () => {
  const [formState, setFormState] = useState({
    file: null as File | null,
    place: "",
    description: "",
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<Stringish>(null);
  const [success, setSuccess] = useState<Stringish>(null);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormState((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      setFormState((prev) => ({
        ...prev,
        file: event.target.files[0],
      }));
    }
  };

  const uploadMemoryPicture = async (data: {
    file: File;
    keyPrefix: string;
  }): Promise<Response | null> => {
    let res = null;
    const formData = new FormData();
    formData.append("file", data.file);
    formData.append("keyPrefix", data.keyPrefix);

    const authToken = cookies.authToken.get();
    console.log(authToken);

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/maps/upload`,
        {
          method: "POST",
          body: formData,
          headers: {
            Authorization: "Bearer " + authToken,
          },
        }
      );

      if (response.ok) {
        setSuccess("File and details uploaded successfully!");
        res = response;
      } else {
        setError("Failed to upload file.");
      }
    } catch (err) {
      setError("An error occurred during upload.");
    } finally {
      setIsSubmitting(false);
      return res;
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);
    setSuccess(null);

    const { file, username, place, planid, description } = formState;

    if (!file || !username || !place || !planid || !description) {
      setError("Please provide all required fields.");
      setIsSubmitting(false);
      return;
    }

    const res = await uploadMemoryPicture({ file: file, keyPrefix: username });
    if (res == null) {
      throw new Error("Could not upload");
    }
    try {
      const resBody = await res.json();
      console.log(resBody);

      await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/memories/upload`, {
        method: "POST",
        body: JSON.stringify({
          date: new Date().toISOString(),
          place: place,
          planid: planid,
          description: description,
        }),
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + cookies.authToken.get(),
        },
      });
    } catch (err) {
      console.error("Failed to parse response body as JSON:", err);
    }
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
        <div style={styles.formGroup}>
          <label htmlFor="description" style={styles.label}>
            Description:
          </label>
          <input
            type="text"
            id="description"
            name="description"
            value={formState.description}
            onChange={handleChange}
            style={styles.input}
          />
        </div>
        <button
          type="submit"
          disabled={isSubmitting}
          style={styles.submitButton}
        >
          {isSubmitting ? "Submitting..." : "Submit"}
        </button>
      </form>
      {error && <p style={styles.error}>{error}</p>}
      {success && <p style={styles.success}>{success}</p>}
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
