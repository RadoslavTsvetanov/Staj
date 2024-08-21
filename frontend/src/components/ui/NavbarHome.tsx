"use client";

import React, {useState} from "react";
import Image from "next/image";
import Logo from './Logo';
import DropMenu from "./DropDownMenu";

const NavbarHome = () => {
    const [isHovered, setIsHovered]=useState(false);

        return (
            <div>
          <nav className="h-[10vh] w-full flex bg-gray-800 text-white p-2 sm:p-2 md:flex md:justify-between md:items-center z-20" >

            <div className=" container flex  items-center">
               <Logo/>
                <h1 className="text-2xl font-bold ml-5">
                    Project
                </h1>
            </div>
            <div className="flex pr-3">
                <button onMouseOver={() => setIsHovered(true)} >
                    <Image src="/images/menu.png" alt="Logo" width={32} height={32} />
                    </button>
                </div>
        
        </nav>
        {isHovered && (
                        <DropMenu onMouseLeave={() => setIsHovered(false)}/>
                    )

                    }
        </div>
    );
};

export default NavbarHome;