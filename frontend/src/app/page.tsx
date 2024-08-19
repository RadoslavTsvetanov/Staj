"use client";

import React, { useEffect } from 'react';
import './styles.css';
import {Navbar} from "../components/ui/navbar";
import { cookies } from '@/lib/utils';

const Landing = () => {
  useEffect(() => {
  console.log(cookies.authToken.get())
},[])
  return (
    <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-[#0e6cc4] w-full h-screen">
      <Navbar/>
      <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md z-10">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <div style={{ width: '100%', height: '100%', overflow: 'hidden'}}>
            <svg
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              xmlnsXlink="http://www.w3.org/1999/xlink"
              x="0px"
              y="0px"
              viewBox="0 0 1600 900"
              preserveAspectRatio="xMidYMax slice"
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

          <div id="contacts">
            <h2>Contact Us</h2>
            <p>Email: staj@gmail.com</p>
            <p>Phone: (123) 456-7890</p>
            <p>Address: Tsarigradsko shose, Sofia, Bulgaria</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Landing;
