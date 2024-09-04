import React, { useState } from 'react';

interface BudgetProps {
  id?: number;
}

const BudgetSelector: React.FC<BudgetProps> = ({ id }) => {
  const [selectedBudget, setSelectedBudget] = useState(0);

  const handleClick = (index: number) => {
    // Toggle the budget level to 0 if clicking the same level again
    const newBudget = selectedBudget === index ? 0 : index;
    setSelectedBudget(newBudget);
    handleBudgetChange(newBudget);
  };

  const handleBudgetChange = (budget: number) => {
    console.log('Selected Budget:', budget);
    // Save the budget here; possibly using `id` to associate with a specific item
  };

  return (
    <div className="p-4 bg-blue-100 rounded-lg shadow-md flex items-center space-x-2">
      <form>
        <div className="flex items-center space-x-2">
          <span className="text-lg">Budget</span>
          <div className="flex">
            {[1, 2, 3, 4].map((level) => (
              <span
                key={level}
                className={`cursor-pointer text-2xl ${
                  selectedBudget >= level ? 'text-yellow-500' : 'text-gray-800'
                }`} 
                onClick={() => handleClick(level)}
              >
                $
              </span>
            ))}
          </div>
        </div>
      </form>
    </div>
  );
};

export default BudgetSelector;
