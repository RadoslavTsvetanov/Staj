// components/ImageSlider.tsx
import React, { useState } from "react";

interface Slide {
  id: number;
  src?: string;
  alt: string;
}

const ImageSlider: React.FC = () => {
  const [currentSlide, setCurrentSlide] = useState<number>(0);
  const [slides, setSlides] = useState<Slide[]>([
    { id: 1, src: "/images/memory1.png", alt: "Image 1" },
    { id: 2, src: "/images/memory1.png", alt: "Image 2" },
    { id: 3, src: "/images/memory1.png", alt: "Image 3" },
    { id: 4, src: "/images/memory1.png", alt: "Image 4" },
    { id: 5, src: "/images/memory1.png", alt: "Image 5" },
    { id: 6, src: "/images/memory1.png", alt: "Image 6" },
    { id: 7, src: "/images/memory1.png", alt: "Image 7" },
  ]);

  const visibleSlides = 4;

  const nextSlide = () => {
    if (currentSlide < slides.length - visibleSlides) {
      setCurrentSlide(currentSlide + 1);
    }
  };

  const prevSlide = () => {
    if (currentSlide > 0) {
      setCurrentSlide(currentSlide - 1);
    }
  };

  const addSlide = () => {
    const newId = slides.length + 1;
    const newSlide: Slide = { id: newId, alt: `New Image ${newId}` };
    setSlides([...slides, newSlide]);
  };

  return (
    <div className="flex items-center justify-center space-x-6">
      <button
        onClick={prevSlide}
        className="text-4xl p-3 bg-gray-200 rounded-full hover:bg-gray-300"
        aria-label="Previous Slide"
      >
        &#x276E;
      </button>
      <div className="overflow-hidden m-0 w-[800px]">
        <div
          className="flex transition-transform duration-500 ease-in-out m-0 space-x-2"
          style={{ transform: `translateX(-${currentSlide * 200}px)` }}
        >
          {slides.map((slide) => (
            <div
              key={slide.id}
              className="w-48 h-48 flex-shrink-0 flex items-center justify-center bg-gray-200 border border-gray-400 rounded-lg overflow-hidden shadow-md"
            >
              {slide.src ? (
                <img
                  src={slide.src}
                  alt={slide.alt}
                  className="w-full h-full object-cover"
                />
              ) : (
                <div className="flex items-center justify-center w-full h-full bg-gray-300">
                  <p>{slide.alt}</p>
                </div>
              )}
            </div>
          ))}
          {/* Add Slide Button */}
          <div className="w-48 h-48 flex-shrink-0 flex items-center justify-center bg-gray-200 rounded-lg shadow-md">
            <button
              onClick={addSlide}
              className="bg-white border-2 border-gray-700 rounded-full w-16 h-16 text-3xl flex items-center justify-center hover:bg-gray-100"
              aria-label="Add Slide"
            >
              +
            </button>
          </div>
        </div>
      </div>
      <button
        onClick={nextSlide}
        className="text-4xl p-3 bg-gray-200 rounded-full hover:bg-gray-300"
        aria-label="Next Slide"
      >
        &#x276F;
      </button>
    </div>
  );
};

export default ImageSlider;
