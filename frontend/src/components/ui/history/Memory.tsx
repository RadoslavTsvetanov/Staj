import React, { FC } from "react";
import Image from "next/image";
import Plus from "../../../../public/images/plus.png";

interface MemoryProps {
  src?: string;
  alt: string;
}

const Memory: FC<MemoryProps> = ({ src, alt }) => {
  return (
    <>
      <div
        className="w-48 h-48 flex-shrink-0 flex items-center justify-center bg-gray-200 border border-gray-400 rounded-lg overflow-hidden relative z-10"
      >
        {src ? (
          <img src={src} alt={alt} className="w-full h-full object-cover" />
        ) : (
          <div className="flex items-center justify-center w-full h-full bg-gray-300">
            <p>{alt}</p>
          </div>
        )}
      </div>
    </>
  );
};

export default Memory;
