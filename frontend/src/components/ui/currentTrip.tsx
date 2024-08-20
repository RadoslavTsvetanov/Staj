import React from "react";
import TripInfo from "./tripInfo";
import PlanDetails from "./planDetails";

function CurrentTripComp() {
  return (
    <div className="absolute top-0 right-7 w-2/5 flex flex-col justify-between ">
      <TripInfo />
      <PlanDetails />
    </div>
  );
}

export default CurrentTripComp;
