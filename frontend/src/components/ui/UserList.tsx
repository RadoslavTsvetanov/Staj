import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

type User = {
    id: string;
    color: string;
};

const UserList: React.FC = () => {
    const [users, setUsers] = useState<User[]>([
        { id: uuidv4(), color: 'bg-red-400' },
        { id: uuidv4(), color: 'bg-green-500' },
        { id: uuidv4(), color: 'bg-purple-600' },
    ]);

    const addUser = () => {
        const newUser: User = {
            id: uuidv4(),
            color: 'bg-gray-500' 
        };
        setUsers([...users, newUser]);
    };

    return (
        <div className="flex items-center space-x-[-20px] p-4 rounded-lg">
            {users.map(user => (
                <div 
                    key={user.id} 
                    className={`${user.color} w-16 h-16 rounded-full border-2 border-black`}
                />
            ))}
            <button 
                onClick={addUser} 
                className="w-16 h-16 flex items-center justify-center rounded-full bg-gray-600 border-2 border-black text-white text-2xl font-bold"
            >
                +
            </button>
        </div>
    );
};

export default UserList;
