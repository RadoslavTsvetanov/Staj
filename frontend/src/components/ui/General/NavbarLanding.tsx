"use client";

import Link from "next/link";
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import Earth from '../Earth';

export const NavbarLanding = () => {
    return (
        <nav className="bg-gray-800 text-white fixed top-0 left-0 h-[10vh] w-full p-2 sm:p-2 md:flex md:justify-between md:items-center z-20">
            <div className="container mx-auto flex justify-between items-center">
                <div className="flex items-center">
                    <a href="" className="text-2xl font-bold flex items-center">
                        <Canvas style={{ height: '50px', width: '150px', marginRight: '16px' }}>
                            <ambientLight intensity={1.5} />
                            <directionalLight position={[10, 10, 5]} intensity={2} />
                            <Earth />
                            <OrbitControls />
                        </Canvas>
                        PlanPals
                    </a>
                </div>
                <div>
                    {/* <Link href="#contacts" className="ml-8 hover:text-gray-300">
                        Contacts
                    </Link> */}
                    <input 
                        type="button" 
                        onClick={() => location.href='../../../auth/signin'} 
                        value="Sign in" 
                        className="ml-auto py-2 px-4 bg-[rgb(141,186,119)] border-transparent text-2sm font-medium rounded-md text-white hover:bg-[rgb(141,178,119)]"
                        style={{ cursor: 'pointer' }}
                    />
                </div>
            </div>
        </nav>
    );
};