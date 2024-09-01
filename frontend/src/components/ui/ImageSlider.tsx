import React, { useState } from 'react';

interface Slide {
  id: number;
  src?: string;
  alt: string;
}

const ImageSlider: React.FC = () => {
  const [currentSlide, setCurrentSlide] = useState<number>(0);
  const [slides, setSlides] = useState<Slide[]>([
    { id: 1, src: '@public/images/memory1.png', alt: 'Image 1' },
    { id: 2, src: '/image2.png', alt: 'Image 2' },
    { id: 3, src: '/image3.png', alt: 'Image 3' },
    { id: 4, src: '/image4.png', alt: 'Image 4' },
    { id: 5, src: '/image5.png', alt: 'Image 5' },
    { id: 6, src: '/image6.png', alt: 'Image 6' },
    { id: 7, src: '/image7.png', alt: 'Image 7' },
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
    <div className="flex items-center justify-center space-x-2">
      <button onClick={prevSlide} className="text-3xl p-2">&#x276E;</button>
      <div className="overflow-hidden w-[320px]">
        <div
          className="flex transition-transform duration-500"
          style={{ transform: `translateX(-${currentSlide * 80}px)` }}
        >
          {slides.map((slide) => (
            <div key={slide.id} className="w-20 h-20 flex-shrink-0 flex items-center justify-center bg-gray-300 border border-gray-400">
              {slide.src ? (
                <img src={slide.src} alt={slide.alt} className="w-full h-full object-cover" />
              ) : (
                <div className="flex items-center justify-center w-full h-full bg-gray-300">
                  <p>{slide.alt}</p>
                </div>
              )}
            </div>
          ))}
          <div className="w-20 h-20 flex-shrink-0 flex items-center justify-center bg-gray-300">
            <button onClick={addSlide} className="bg-white border-2 border-gray-700 rounded-full w-12 h-12 text-3xl">+</button>
          </div>
        </div>
      </div>
      <button onClick={nextSlide} className="text-3xl p-2">&#x276F;</button>
    </div>
  );
};

export default ImageSlider;
