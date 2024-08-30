"use client";

import CurrentlyPlanning from "@/components/ui/CurrentlyPlanning";
import NavbarHome from "@/components/ui/NavbarHome";
import HomePageMap from "@/components/ui/HomePageMap";
import Plan from "@/components/ui/plan";

export default function HomePage() {
  return (
    <div>
      <NavbarHome />

      <div className="flex h-[90vh]">
        <div className="w-1/2 rounded-lg m-2 relative overflow-hidden">
          <HomePageMap/>
        </div>

        <div className="w-1/2 flex flex-col justify-between">
          <CurrentlyPlanning/>

          <div className="relative h-1/2 bg-red-200 rounded-xl m-2 p-4 overflow-hidden">
            <h1 className=" top-0 left-0 right-0 text-black text-xl font-bold mb-4 text-center ">
              Previous trips
            </h1>
            <div className="h-[calc(4*51.5px)] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
              <div className="space-y-2 ">
                <Plan name="Mountains" isEditable={false} />
                <Plan name="Our trip to Greece" isEditable={false} />
                <Plan name="Christmas holiday" isEditable={false} />
                <Plan name="Paris" isEditable={false} />
                <Plan name="New York" isEditable={false} />
                <Plan name="New York" isEditable={false} />
                <Plan name="New York" isEditable={false} />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
