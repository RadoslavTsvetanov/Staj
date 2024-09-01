"use client";

import Planning from "@/components/ui/Planning";
import NavbarHome from "@/components/ui/NavbarHome";
import HomePageMap from "@/components/ui/HomePageMap";

export default function HomePage() {
  return (
    <div>
      <NavbarHome />

      <div className="flex h-[90vh]">
        <div className="w-1/2 rounded-lg m-2 relative overflow-hidden">
          <HomePageMap/>
        </div>

        <div className="w-1/2 flex flex-col justify-between">
          <Planning isPlanning={true}/>
          <Planning isPlanning={false}/>
        </div>
      </div>
    </div>
  );
}
