"use client";

import Link from "next/link";
import Logo from './Logo';

const Navbar = () => {
    
        return (
          <div style={{ height: '10vh' }} >
        <nav className="bg-gray-800 text-white fixed top-0 left-0 w-full p-4 sm:p-4 md:flex md:justify-between md:items-center z-20">
            <div className="container mx-auto flex justify-between items-center">
               <Logo/>
                <a href="" className="text-2xl font-bold">
                    Project
                </a>
                <div>
                    <Link href="../../app/about" className="ml-8 hover:text-gray-300">
                        About
                    </Link>
                    <Link href="#contacts" className="ml-8 hover:text-gray-300">
                        Contacts
                    </Link>
                    {/* <input 
                        type="button" 
                        onClick={() => location.href='../../../auth/signup'} 
                        value="Sign up" 
                        className="ml-16 py-2 px-4 bg-[rgba(115,209,72,0.2)] border-transparent text-2sm font-medium rounded-md text-white hover:bg-[rgba(115,209,72,0.3)]" 
                    /> */}
                </div>
            </div>
        </nav>
        </div>
    );
};

export default Navbar;
