import React, { useState } from "react";
import Memory from "./Memory";
import FileUploadForm from "./Popup";
import Image from "next/image";
import Plus from "../../../../public/images/plus.png";

interface Slide {
  id: number;
  src?: string;
  alt: string;
}

const ImageSlider: React.FC = () => {
  const [currentSlide, setCurrentSlide] = useState<number>(0);
  const [slides, setSlides] = useState<Slide[]>([
    { id: 1, src: "/images/memory1.png", alt: "Image 1" },
    { id: 2, src: "/images/memory2.png", alt: "Image 2" },
    { id: 3, src: "/images/memory3.png", alt: "Image 3" },
    { id: 4, src: "/images/memory1.png", alt: "Image 4" },
    { id: 5, src: "/images/memory.png", alt: "Image 5" },
    { id: 6, src: "/images/memory1.png", alt: "Image 6" },
    { id: 7, src: "/images/memory1.png", alt: "Image 7" },
  ]);
  const [showPopup, setShowPopup] = useState(false);

  const visibleSlides = 4;

  const nextSlide = () => {
    if (currentSlide < slides.length - visibleSlides + 1) {
      setCurrentSlide(currentSlide + 1);
    }
  };

  const prevSlide = () => {
    if (currentSlide > 0) {
      setCurrentSlide(currentSlide - 1);
    }
  };

  // const addSlide = () => {
  //   const newId = slides.length + 1;
  //   const newSlide: Slide = { id: newId, alt: `New Image ${newId}` };
  //   setSlides([...slides, newSlide]);
  // };
  const addSlide = (newSlide: Slide) => {
    setSlides((prevSlides) => [...prevSlides, newSlide]);
  };

  const handlePopup = () => {
    setShowPopup(true);
  };

  const handleFormClose = (newSlide?: Slide) => {
    setShowPopup(false);
    if (newSlide) {
      addSlide(newSlide);
    }
  };

  return (
    <div className="relative flex items-center justify-center space-x-6">
      <button
        onClick={prevSlide}
        className="text-4xl p-3 rounded-full hover:bg-gray-300 z-10"
        aria-label="Previous Slide"
      >
        &#x276E;
      </button>
      <div className="overflow-hidden m-0 w-[800px] relative z-10">
        <div
          className="flex transition-transform duration-500 ease-in-out m-0 space-x-2"
          style={{ transform: `translateX(-${currentSlide * 200}px)` }}
        >
          {slides.map((slide) => (
            <Memory key={slide.id} src={slide.src} alt={slide.alt} />
          ))}
          {/* "+" Button for adding new slides */}
          <div className="cursor-pointer" onClick={handlePopup}>
            <Image
              src="/./frontend/public/images/plus.png"
              alt="+"
              width={200}
              height={200}
              className="border border-gray-300 rounded-lg" // Added styling for better visibility
            />
          </div>
        </div>
      </div>
      <button
        onClick={nextSlide}
        className="text-4xl p-3 rounded-full hover:bg-gray-300 z-10"
        aria-label="Next Slide"
      >
        &#x276F;
      </button>
      {showPopup && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-20">
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <FileUploadForm onClose={handleFormClose} />
          </div>
        </div>
      )}
    </div>
  );
};

export default ImageSlider;
