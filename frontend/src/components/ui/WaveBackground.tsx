import React from 'react';

export default function WaveBackground(): JSX.Element {
  return (
    <div className="box">
      <div className="wave -one"></div>
      <div className="wave -two"></div>
      <div className="wave -three"></div>
      <style jsx>{`
        .box {
          position: fixed;
          top: 0;
          transform: rotate(80deg);
          left: 0;
        }

        .wave {
          position: fixed;
          top: 0;
          left: 0;
          opacity: 0.4;
          position: absolute;
          top: 3%;
          left: 10%;
          background: #0af;
          width: 1500px;
          height: 1300px;
          margin-left: -150px;
          margin-top: -250px;
          transform-origin: 50% 48%;
          border-radius: 43%;
          animation: drift 7000ms infinite linear;
        }

        .wave.-three {
          animation: drift 7500ms infinite linear;
          position: fixed;
          background-color: #77daff;
        }

        .wave.-two {
          animation: drift 3000ms infinite linear;
          opacity: 0.1;
          background: black;
          position: fixed;
        }

        @keyframes drift {
          from {
            transform: rotate(0deg);
          }
          to {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </div>
  );
}
