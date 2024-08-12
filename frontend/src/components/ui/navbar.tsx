import { useState } from 'react';
import Image from 'next/image';

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => setIsOpen(!isOpen);

  return (
    <nav className="bg-blue-900 p-4">
      <div className="container mx-auto flex items-center justify-between">
        <div className="text-white text-2xl font-bold">
          <p >project name </p>
        </div>
        <div className="block lg:hidden">
          <button onClick={toggleMenu} className="text-white  focus:outline-none">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16m-7 6h7" />
            </svg>
          </button>
        </div>
        <div className={`lg:flex lg:items-center lg:w-auto ${isOpen ? 'block' : 'hidden'}`}>
          <p  className="text-white px-4 py-2 block">Home</p>
          
          
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
