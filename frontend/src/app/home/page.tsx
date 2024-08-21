"use client";

import CurrentlyPlanning from "@/components/ui/CurrentlyPlanning";
import NavbarHome from "@/components/ui/NavbarHome";
import HomePageMap from "@/components/ui/HomePageMap";
import Plan from "@/components/ui/plan";

export default function HomePage() {

//   const handlePlans = async (event) => {
//   try {
//     const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/user-access/plans`, {
//       method: 'GET',
//       headers: {
//         'Content-Type': 'application/json',
//       },
//       body: JSON.stringify({ email, password }),
//     });

//     if (!response.ok) {
//       const errorMessage = await response.text();
//       console.error('Sign-in failed:', errorMessage);
//       alert('Failed to sign in. Please check your credentials.');
//       return;
//     }

//     const token = await response.text();
//     cookies.authToken.set(token);
//     console.log('Sign-in successful:', token);

//     router.push('../../home');
//   } catch (err) {
//     console.error('Network error:', err);
//     alert('Network error. Please try again later.');
//   }
// };

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
