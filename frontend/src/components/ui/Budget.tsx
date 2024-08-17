import React, { useState } from 'react';
import Image from 'next/image';

const BudgetSelector = () => {
  const [selectedBudget, setSelectedBudget] = useState(4);
  const [isSelectedBudget, selectBudget] = useState(false);

  const handleClick = (index: number) => {
    setSelectedBudget(index);
    selectBudget(true);
    handleBudgetChange(index);
  };

  const handleBudgetChange = (budget: number) => {
    console.log('Selected Budget:', budget);
  };

  return (
    <div className="p-4 bg-blue-100 ">
      <form>
    <div className="flex items-center space-x-2">
      <span className="text-lg">Budget</span>
      <div className="flex">
        {[1, 2, 3, 4].map((level) => (
          <span
            key={level}
            className={`cursor-pointer text-2xl ${
              selectedBudget >= level ? 'text-green-700' : 'text-gray-800'
            }`}
            onClick={() => handleClick(level)}
          >
            $
          </span>
        ))}
      </div>
      <Image src="/images/edit.png" alt="editB" onClick={()=>selectBudget(false)} width={20} height={20} />
    </div>
    </form>
    </div>
  );
};

export default BudgetSelector;
