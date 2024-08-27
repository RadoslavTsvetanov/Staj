"use client";

import React, {useState} from "react";
import Image from "next/image";
import DropMenu from "./DropDownMenu";
import { OrbitControls } from "@react-three/drei";
import Earth from "../../../public/Earth";
import { Canvas } from "@react-three/fiber";
import Link from "next/link";

const Navbar = () => {
    const [isHovered, setIsHovered]=useState(false);

        return (
        <div>
          <nav className="h-[10vh] w-full flex bg-gray-800 text-white p-2 sm:p-2 md:flex md:justify-between md:items-center z-20" >

            <div className="container flex  items-center">
                <Link href="/">
                    <Canvas style={{ height: '50px', width: '150px', marginRight: '16px' }}>
                        <ambientLight intensity={1.5} />
                        <directionalLight position={[10, 10, 5]} intensity={2} />
                        <Earth />
                        <OrbitControls />
                    </Canvas>
                </Link>
                <h1 className="text-2xl font-bold ml-5">
                    PlanPals
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

export default Navbar;
