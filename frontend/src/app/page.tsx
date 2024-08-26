"use client";

import React, { useEffect } from 'react';
import './styles.css';
import { NavbarLanding } from "../components/ui/NavbarLanding";
import { OrbitControls } from '@react-three/drei';
import { cookies } from '@/lib/utils';

import { Model } from '../../public/Landing';
import { Canvas } from "@react-three/fiber";


const Landing = () => {
  useEffect(() => {
  console.log("AAA", cookies.authToken.get())
},[])
  return (
    <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-[#0e6cc4] w-full h-screen overflow-hidden">
      <NavbarLanding />
      <div className="absolute inset-0 z-0">
        <svg
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          xmlnsXlink="http://www.w3.org/1999/xlink"
          x="0px"
          y="0px"
          viewBox="0 0 1600 900"
          preserveAspectRatio="xMidYMax slice"
          className="w-full h-full"
        >
          <defs>
            <linearGradient id="bg">
              <stop offset="0%" style={{ stopColor: 'rgba(130, 158, 249, 0.06)' }}></stop>
              <stop offset="50%" style={{ stopColor: 'rgba(76, 190, 255, 0.6)' }}></stop>
              <stop offset="100%" style={{ stopColor: 'rgba(115, 209, 72, 0.2)' }}></stop>
            </linearGradient>
            <path
              id="wave"
              fill="url(#bg)"
              d="M-363.852,502.589c0,0,236.988-41.997,505.475,0 s371.981,38.998,575.971,0s293.985-39.278,505.474,5.859s493.475,48.368,716.963-4.995v560.106H-363.852V502.589z"
            />
          </defs>
          <g>
            <use xlinkHref="#wave" opacity="0.3">
              <animateTransform
                attributeName="transform"
                attributeType="XML"
                type="translate"
                dur="10s"
                calcMode="spline"
                values="270 230; -334 180; 270 230"
                keyTimes="0; .5; 1"
                keySplines="0.42, 0, 0.58, 1.0;0.42, 0, 0.58, 1.0"
                repeatCount="indefinite"
              />
            </use>
            <use xlinkHref="#wave" opacity="0.6">
              <animateTransform
                attributeName="transform"
                attributeType="XML"
                type="translate"
                dur="8s"
                calcMode="spline"
                values="-270 230;243 220;-270 230"
                keyTimes="0; .6; 1"
                keySplines="0.42, 0, 0.58, 1.0;0.42, 0, 0.58, 1.0"
                repeatCount="indefinite"
              />
            </use>
            <use xlinkHref="#wave" opacity="0.9">
              <animateTransform
                attributeName="transform"
                attributeType="XML"
                type="translate"
                dur="6s"
                calcMode="spline"
                values="0 230;-140 200;0 230"
                keyTimes="0; .4; 1"
                keySplines="0.42, 0, 0.58, 1.0;0.42, 0, 0.58, 1.0"
                repeatCount="indefinite"
              />
            </use>
          </g>
        </svg>
      </div>

      <div className="relative z-10 flex justify-between items-center h-full px-8">
        {/* <Canvas camera={{ fov: 64, position: [-2, 2, 0] }} style={{ height: '300px', width: '800px' }}>
          <ambientLight intensity={5} />
          <OrbitControls enableZoom={true} />
          <Model />
        </Canvas> */}

        <input
          type="button"
          onClick={() => location.href='../../../auth/signup'}
          value="Get Started!"
          className='ml-auto py-3 px-20 bg-[rgb(141,186,119)] border-transparent text-2sm font-medium rounded-2xl text-white hover:bg-[rgb(141,178,119)]'
          style={{ cursor: 'pointer' }}
        />

        <div className="bg-white bg-opacity-70 rounded-xl p-6 max-h-[60%] w-[52%] overflow-y-auto shadow-lg ml-auto">
          <h1 className="text-3xl font-bold mb-4 text-[#0e6cc4]">
            🌍 Discover Your Next Adventure & Capture Every Moment with Staj 🚀
          </h1>
          <p className="text-lg text-[#0e6cc4] mb-4">
            Tired of one-size-fits-all travel plans? Ready to explore the world your way and keep every memory alive? Meet Staj—the ultimate travel companion that not only curates your perfect trip but also lets you relive every unforgettable moment!
          </p>
          <h2 className="text-xl font-semibold text-[#0e6cc4] mb-4">✨ How It Works:</h2>
          <ul className="text-[#0e6cc4] mb-4">
            <li>- Pick Your Spot: Choose any location on the map—whether it’s a hidden gem nearby or a far-off dream destination.</li>
            <li>- Set Your Range: Define your travel radius, from local day trips to epic road journeys.</li>
            <li>- Match Your Interests: Passionate about food, history, nature, or nightlife? Tailor your adventure to fit what you love.</li>
          </ul>
          <h2 className="text-xl font-semibold text-[#0e6cc4] mb-4">🎒 What’s In It for You?</h2>
          <ul className="text-[#0e6cc4] mb-4">
            <li>- Tailored Experiences: Get personalized recommendations based on your unique interests—no more generic itineraries.</li>
            <li>- Discover Hidden Gems: Uncover secret spots and local favorites that you won’t find in any guidebook.</li>
            <li>- Seamless Planning: Our intuitive app handles all the details, so you can focus on enjoying the journey.</li>
          </ul>
          <h2 className="text-xl font-semibold text-[#0e6cc4] mb-4">📸 Capture & Relive Every Moment:</h2>
          <ul className="text-[#0e6cc4] mb-4">
            <li>- Save Your Memories: Snap photos during your trip and store them as “memories” right in the app.</li>
            <li>- Create a Travel Diary: Build a visual story of your adventures, complete with locations and notes.</li>
            <li>- Share & Cherish: Relive your favorite moments anytime, or share them with friends and family.</li>
          </ul>
          <h2 className="text-xl font-semibold text-[#0e6cc4] mb-4">🌟 Why Choose Staj?</h2>
          <p className="text-[#0e6cc4] mb-4">
            Because your travel story deserves to be told—over and over again. Staj not only crafts the perfect adventure but also helps you treasure it forever.
          </p>
          <h2 className="text-xl font-semibold text-[#0e6cc4] mb-4">🚀 Explore. Capture. Remember.</h2>
          <p className="text-[#0e6cc4] mb-4">
            Download Staj now and start planning your next trip, where every adventure is tailored to you, and every memory is a keepsake!
          </p>
        </div>
      </div>
    </div>
  );
};

export default Landing;
