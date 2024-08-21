import Plan from "@/components/ui/plan";
import { getUserPlans } from "@/utils/api";
import { useEffect, useState } from "react";

function CurrentlyPlanning()
{
    const [plans, setPlans] = useState([]);

    useEffect(() => {
        getUserPlans().then(setPlans);
        // getUserPlans().then((newPlans) => setPlans(newPlans));
    }, [])

    console.log(plans);

    return(
          <div className="h-1/2 bg-customBlue rounded-xl m-2 p-4">
            <h1 className="text-black text-xl font-bold mb-4 text-center">
              Currently planning
            </h1>
            <div className="h-[calc(4*51.5px)] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
            <div className="space-y-2">
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
            </div>
            </div>
          </div>

    );
}

export default CurrentlyPlanning;