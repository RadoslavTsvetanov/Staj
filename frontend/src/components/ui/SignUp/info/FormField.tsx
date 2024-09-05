import React from 'react';

interface FormFieldProps {
  label: string;
  type: string;
  name: string;
  id: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  required?: boolean;
}

const FormField: React.FC<FormFieldProps> = ({ label, type, name, id, value, onChange, required }) => (
  <div className="mt-4">
    <label className="block text-sm font-medium text-gray-700" htmlFor={id}>
      {label}
    </label>
    <div className="mt-1">
      <input
        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        type={type}
        name={name}
        id={id}
        value={value}
        onChange={onChange}
        required={required}
      />
    </div>
  </div>
);

export default FormField;
