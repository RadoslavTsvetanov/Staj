import React, { useState } from 'react';
import { EyeIcon, EyeSlashIcon } from '@heroicons/react/24/outline';

interface PasswordFieldProps {
  label: string;
  name: string;
  id: string;
  value: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function PasswordField({
  label,
  name,
  id,
  value,
  onChange,
}: PasswordFieldProps): JSX.Element {
  const [showPassword, setShowPassword] = useState<boolean>(false);

  return (
    <div className="mt-4">
      <label className="block text-sm font-medium text-gray-700" htmlFor={id}>
        {label}
      </label>
      <div className="mt-1 relative">
        <input
          className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
          required
          autoComplete="current-password"
          type={showPassword ? 'text' : 'password'}
          name={name}
          id={id}
          value={value}
          onChange={onChange}
        />
        <button
          type="button"
          className="absolute inset-y-0 right-0 flex items-center px-2"
          onClick={() => setShowPassword(!showPassword)}
        >
          {showPassword ? (
            <EyeIcon className="h-5 w-5 text-gray-500" />
          ) : (
            <EyeSlashIcon className="h-5 w-5 text-gray-500" />
          )}
        </button>
      </div>
    </div>
  );
}
