"use client"
import React from 'react';

type SliderProps = {
  radius: number;
  setRadius: (value: number) => void;
};

const Slider: React.FC<SliderProps> = ({ radius, setRadius }) => {
  const handleSlideChange = (event) => {
    setRadius(parseInt(event.target.value, 10));
  };

  return (
    <div className="flex flex-col items-center justify-center p-4 bg-gray-100 rounded-lg shadow-md max-w-xs mx-auto space-y-2">
      <label>{radius} km</label>
      <input
        type="range"
        min="1"
        max="50"
        step="1"
        value={radius}
        onChange={handleSlideChange}
        className="w-full h-2 bg-blue-300 rounded-lg appearance-none cursor-pointer accent-blue-600"
      />
    </div>
  );
};

export default Slider;
