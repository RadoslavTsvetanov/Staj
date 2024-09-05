"use client";
import { cookies } from "@/lib/utils";
import React, { useState } from "react";

type Stringish = string | null;

interface FileUploadFormProps {
    onClose: (newSlide?: { id: number; src: string; alt: string }) => void;
}

const FileUploadForm: React.FC<FileUploadFormProps> = ({ onClose }) => {
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

    const { file, place, description } = formState;

    if (!file || !place || !description) {
      setError("Please provide all required fields.");
      setIsSubmitting(false);
      return;
    }

    const res = await uploadMemoryPicture({ file: file, keyPrefix: place });
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
          //planid: planid,
          description: description,
        }),
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + cookies.authToken.get(),
        },
      });

    const newSlide = {
        id: Date.now(), // Temporary ID, could be replaced by response ID
        src: URL.createObjectURL(file),
        alt: place,
      };

      onClose(newSlide);
    } catch (err) {
      console.error("Failed to parse response body as JSON:", err);
    }
  };

  return (
    <div className="max-w-lg mx-auto p-6 bg-white rounded-lg shadow-lg">
        <h2 className="text-center text-2xl font-bold mb-4 text-gray-800">Add photo</h2>
        <form onSubmit={handleSubmit} className="flex flex-col">
            <div className="mb-4">
                <label htmlFor="file" className="block text-sm font-bold text-gray-700 mb-2">
                    Choose a JPEG file:
                </label>
                <input
                    type="file"
                    id="file"
                    name="file"
                    onChange={handleFileChange}
                    className="w-full px-4 py-2 border border-gray-300 rounded-md"
                />
            </div>
            <div className="mb-4">
                <label htmlFor="place" className="block text-sm font-bold text-gray-700 mb-2">
                    Place:
                </label>
                <input
                    type="text"
                    id="place"
                    name="place"
                    value={formState.place}
                    onChange={handleChange}
                    className="w-full px-4 py-2 border border-gray-300 rounded-md"
                />
            </div>
            <button
                type="submit"
                disabled={isSubmitting}
                className={`w-full py-2 px-4 text-white font-bold rounded-md transition ${
                    isSubmitting ? "bg-gray-500" : "bg-blue-500 hover:bg-blue-600"
                }`}
            >
                {isSubmitting ? "Submitting..." : "Submit"}
            </button>
            <button
                type="button"
                onClick={() => onClose()}
                className="w-full mt-4 py-2 px-4 bg-gray-500 text-white font-bold rounded-md hover:bg-gray-600"
            >
                Cancel
            </button>
        </form>
        {error && <p className="mt-4 text-red-600 text-center">{error}</p>}
        {success && <p className="mt-4 text-green-600 text-center">{success}</p>}
    </div>
);
};

export default FileUploadForm;
