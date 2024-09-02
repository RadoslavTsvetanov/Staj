import React from "react";
import TripInfo from "./tripInfo";
import PlanDetails from "./planDetails";

type Props = {
  location: string,
};

const CurrentTripComp: React.FC<Props>=({location})=> {
  return (
    <div className="absolute top-0 right-7 w-2/5 flex flex-col justify-between ">
      <TripInfo location={location} />
      <PlanDetails />
    </div>
  );
}

export default CurrentTripComp;
