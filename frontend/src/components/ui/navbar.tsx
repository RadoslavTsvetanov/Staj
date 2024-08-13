"use client";

import Link from "next/link";

const Navbar = () => {
    
        return (
          <div style={{ height: '10vh' }} >
        <nav className="bg-gray-800 text-white fixed top-0 left-0 w-full p-4 sm:p-4 md:flex md:justify-between md:items-center z-20">
            <div className="container mx-auto flex justify-between items-center">
                <a href="" className="text-2xl font-bold">
                    Project
                </a>
                <div>
                    <Link href="../../app/home" className="mx-2 hover:text-grey-300">
                    Home
                    </Link>
                    <Link href="../../app/about" className="mx-2 hover:text-grey-300">
                    About
                    </Link>
                    <Link href="/" className="mx-2 hover:text-grey-300">
                    Contacts
                    </Link>
                </div>
            </div>
        </nav>
        </div>
    );
};

export default Navbar;