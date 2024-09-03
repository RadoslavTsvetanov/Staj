import { useState } from "react";

export function uploadFileRequest() {
    const [status, setStatus] = useState<string | null>(null)
    const [error, setError] = useState<string | null>(null)
    const [response, setResponse] = useState<string | null>(null)
  
    const submit = async (file: File, username: string) => {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("username", username);

      if (!file || !username) {
        setError("Please provide both a file and a username.");
        return;
      }
  
      setStatus('loading')
      try {
        const response = await fetch("http://localhost:4550/files/upload", {
          method: "POST",
          body: formData,
        });
  
        if (response.ok) {
          setStatus('success')
          setResponse("File and username uploaded successfully!");
        } else {
          setStatus('failed')
          setError("Failed to upload file.");
        }
      } catch (err) {
        setStatus('failed')
        setError("An error occurred during upload.");
      }
    }
  
    return [status, error, response, submit]
  }