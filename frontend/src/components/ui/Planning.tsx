import Plan from "@/components/ui/plan";
import { getUserPlans } from "@/utils/api";
import { useEffect, useState } from "react";

interface DateWindow {
  startDate: string;
  endDate: string;
}

interface Place {
  id: number;
  name: string;
  planId: number;
  dateWindow?: DateWindow | null; // Optional chaining to handle missing values
  placeLocations: any[];
}

interface PlanType {
  id: number;
  estCost: number;
  budget: number;
  name: string;
  places: Place[];
}

interface PlanningProps {
  isPlanning: boolean;
}

const Planning: React.FC<PlanningProps> = ({ isPlanning }) => {
  const [currentPlans, setCurrentPlans] = useState<PlanType[]>([]);
  const [previousPlans, setPreviousPlans] = useState<PlanType[]>([]);

  useEffect(() => {
    const fetchPlans = async () => {
      try {
        const fetchedPlans = await getUserPlans();
        console.log("Fetched Plans:", fetchedPlans);

        const now = new Date(); // Current date and time

        // Helper function to extract the latest end date from the places array
        const getLatestEndDate = (places: Place[]): Date | null => {
          const endDates = places
            .map((place) => {
              // Safely access dateWindow and endDate
              const endDate = place.dateWindow?.endDate;
              return endDate ? new Date(endDate) : null; // Convert to Date object if exists
            })
            .filter((date) => date && !isNaN(date.getTime())); // Filter out invalid dates

          if (endDates.length === 0) return null;

          // Find the latest end date
          return new Date(Math.max(...endDates.map((date) => date!.getTime())));
        };

       
        const current = fetchedPlans.filter((plan: PlanType) => {
          const latestEndDate = getLatestEndDate(plan.places);
          console.log(`Current Plan Latest End Date: ${latestEndDate}, Now: ${now}`);
          return latestEndDate && latestEndDate >= now;
        });

        const previous = fetchedPlans.filter((plan: PlanType) => {
          const latestEndDate = getLatestEndDate(plan.places);
          console.log(`Previous Plan Latest End Date: ${latestEndDate}, Now: ${now}`);
          return latestEndDate && latestEndDate < now;
        });

        setCurrentPlans(current);
        setPreviousPlans(previous);
      } catch (error) {
        console.error("Error fetching plans:", error);
      }
    };

    fetchPlans();
  }, []);

  const plansToDisplay = isPlanning ? currentPlans : previousPlans;

  const backgroundColor = isPlanning ? "bg-customBlue" : "bg-red-200";
  const headerText = isPlanning ? "Currently Planning" : "Previous Trips";

  return (
    <div className={`h-1/2 ${backgroundColor} rounded-xl m-2 p-4`}>
      <h1 className="text-black text-xl font-bold mb-4 text-center">
        {headerText}
      </h1>
      <div className="h-[calc(4*51.5px)] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
        <div className="space-y-2">
          {plansToDisplay.map((plan) => (
            <Plan
              key={plan.id}
              id={plan.id}
              name={plan.name}
              isEditable={isPlanning}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default Planning;
