import React, { useState, useEffect } from "react";
import { useSearchParams } from "next/navigation";
import BudgetSelector from "./Budget";
import DateRangeSelector from "./Dates";
import Location from "./Location";
import UserList from "../General/UserList";

import { createPlan } from '@/utils/planService';

type TripInfoProps = {
  location: string,
};

const TripInfo: React.FC<TripInfoProps> = ({ location }) => {
  const searchParams = useSearchParams();
  const [name, setName] = useState(searchParams.get('name') ?? 'Current trip');
  const [isEditing, setIsEditing] = useState(false);
  const [budget, setBudget] = useState(1);

  const handleDoubleClick = () => {
    setIsEditing(true);
  };

  const handleBlur = () => {
    setIsEditing(false);
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setName(event.target.value);
  };

  const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      setIsEditing(false);
    }
  };

  const newPlan = {
    estCost: 3, // ??
    budget: budget,
    name: name,
    dateWindow: {
      startDate: "2024-09-01",
      endDate: "2024-09-10"
    },
    history: {
      createdBy: "user1",
      createdDate: "2024-08-20",
      lastUpdatedBy: "user1",
      lastUpdatedDate: "2024-08-20"
    },
    usernames: ["user1", "user2"],
    places: [
      {
        name: "Place 1",
        locations: ["Location 1", "Location 2"]
      },
      {
        name: "Place 2",
        locations: ["Location 3"]
      }
    ]
  };

  useEffect(() => {
    console.log(JSON.stringify(newPlan));

    createPlan(newPlan)
      .then(savedPlan => {
        console.log("Plan created successfully:", savedPlan);
        // Further actions, e.g., redirecting or updating UI
      })
      .catch(error => {
        console.error("Error creating plan:", error.message);
        // Display error message to the user if needed
      });
  }, [name, budget, location]); // Adding `name` as a dependency so it runs when `name` changes

  return (
    <div className="h-1/2 max-h-[45vh] w-full bg-blue-100 rounded-xl m-2 mt-4 mr-4 p-4 z-50 space-y-1 overflow-hidden">
      <div className="flex items-center justify-between">
        {isEditing ? (
          <input
            type="text"
            value={name}
            onChange={handleChange}
            onBlur={handleBlur}
            onKeyDown={handleKeyPress}
            autoFocus
            className="text-black text-xl font-bold ml-2 bg-transparent border-none outline-none"
          />
        ) : (
          <h1
            onDoubleClick={handleDoubleClick}
            className="text-black text-xl font-bold ml-2 cursor-pointer"
          >
            {name}
          </h1>
        )}

        <UserList className="flex-shrink-0 mr-2" />
      </div>

      <DateRangeSelector />
      <Location location={location} />
      <BudgetSelector />
    </div>
  );
};

export default TripInfo;
