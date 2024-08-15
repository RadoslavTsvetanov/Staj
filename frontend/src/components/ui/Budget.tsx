"use client";

import React, { useState, FormEvent, ChangeEvent } from 'react';

const RadioForm: React.FC = () => {
  const [selectedValue, setSelectedValue] = useState<string>('');

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSelectedValue(event.target.value);
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    alert(`Selected language: ${selectedValue}`);
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input
          type="radio"
          id="-"
          name="budget"
          value="-"
          checked={selectedValue === '-'}
          onChange={handleChange}
        />
        <label htmlFor="-">-</label><br />
        <input
          type="radio"
          id="$"
          name="budget"
          value="$"
          checked={selectedValue === '$'}
          onChange={handleChange}
        />
        <label htmlFor="$">$</label><br />
        <input
          type="radio"
          id="$$"
          name="budget"
          value="$$"
          checked={selectedValue === '$$'}
          onChange={handleChange}
        />
        <label htmlFor="$$">$$</label><br />
        <input
          type="radio"
          id="$$$"
          name="budget"
          value="$$$"
          checked={selectedValue === '$$$'}
          onChange={handleChange}
        />
        <label htmlFor="$$$">$$$</label><br /><br />
        <input
          type="radio"
          id="$$$$"
          name="budget"
          value="$$$$"
          checked={selectedValue === '$$$$'}
          onChange={handleChange}
        />
        <label htmlFor="$$$$">$$$$</label><br />
      </form>
    </div>
  );
};

export default RadioForm;
