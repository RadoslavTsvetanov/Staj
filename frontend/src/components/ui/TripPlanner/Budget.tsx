import React, { useState } from 'react';

interface BudgetProps{
  id?:number,
}

const BudgetSelector: React.FC<BudgetProps> = ({id}) => {
  const [selectedBudget, setSelectedBudget] = useState(0);


  const handleClick = (index: number) => {
    setSelectedBudget(index);
    handleBudgetChange(index);
  };

  const handleBudgetChange = (budget: number) => {
    console.log('Selected Budget:', budget);
    //saveBudget?? id za tova
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
