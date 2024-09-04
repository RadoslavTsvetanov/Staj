"use client";

import { useState, useEffect } from "react";
import { Draggable, DropResult, Droppable } from "react-beautiful-dnd";
import { DndContext } from "@/context/DndContext";
import Day from "./Day";
import Place from "./Place";

interface Plan {
  dayNumber: number;
  places: { id: number; name: string }[];
}

const PlanDetails: React.FC = () => {
  const [planData, setPlanData] = useState<Plan[]>([]);

  useEffect(() => {
    // Initial dummy data
    setPlanData([
      { dayNumber: 1, places: [{ id: 1, name: "Aqua park" }, { id: 2, name: "Sushi place" }] },
      { dayNumber: 2, places: [{ id: 3, name: "Museum" }] },
      { dayNumber: 3, places: [{ id: 4, name: "Park" }] },
    ]);
  }, []);

  const onDragEnd = (result: DropResult) => {
    const { source, destination } = result;
    if (!destination) return;

    const newData = [...JSON.parse(JSON.stringify(planData))];

    const sourceDayIndex = newData.findIndex(
      (day) => day.dayNumber === parseInt(source.droppableId.split("day")[1])
    );
    const destinationDayIndex = newData.findIndex(
      (day) => day.dayNumber === parseInt(destination.droppableId.split("day")[1])
    );

    // Move item within the same day
    if (sourceDayIndex === destinationDayIndex) {
      const [movedPlace] = newData[sourceDayIndex].places.splice(source.index, 1);
      newData[destinationDayIndex].places.splice(destination.index, 0, movedPlace);
    } else {
      // Move item to another day
      const [movedPlace] = newData[sourceDayIndex].places.splice(source.index, 1);
      newData[destinationDayIndex].places.splice(destination.index, 0, movedPlace);
    }

    setPlanData(newData);
  };

  // if (!planData.length) {
  //   return <LoadingSkeleton />;
  // }

  return (
    <DndContext onDragEnd={onDragEnd}>
      <div className="h-1/2 bg-blue-100 rounded-xl p-4 m-2 mr-4 w-full border border-gray-200 min-h-[50vh] max-h-[55vh] z-40">
        <h1 className="text-black text-xl font-bold mb-2 text-center">My plan</h1>
        <div className="max-h-[40vh] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
          {planData.map((day, dayIndex) => (
            <Droppable key={dayIndex} droppableId={`day${day.dayNumber}`}>
              {(provided) => (
                <div ref={provided.innerRef} {...provided.droppableProps} className="mb-2 space-y-1">
                  <Day dayNumber={day.dayNumber} />
                  <ul className="list-none list-inside">
                    {day.places.map((place, placeIndex) => (
                      <Draggable key={place.id} draggableId={place.id.toString()} index={placeIndex}>
                        {(provided) => (
                          <li
                            ref={provided.innerRef}
                            {...provided.draggableProps}
                            {...provided.dragHandleProps}
                            className="bg-blue-200 px-4 py-3 my-1 rounded"
                          >
                            <Place text={place.name} />
                          </li>
                        )}
                      </Draggable>
                    ))}
                    {provided.placeholder}
                  </ul>
                </div>
              )}
            </Droppable>
          ))}
        </div>
      </div>
    </DndContext>
  );
};

export default PlanDetails;
