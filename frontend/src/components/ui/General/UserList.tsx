import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';
import UserModal from './UserModal'; 

type User = {
  id: string;
  username: string;
  photo: string;
};

interface UserListProps{
    className?: string,
}

const UserList: React.FC<UserListProps> = () => {
  const [users, setUsers] = useState<User[]>([
    { id: uuidv4(), username: 'Alice', photo: 'https://via.placeholder.com/40/FF0000/FFFFFF?text=A' },
    { id: uuidv4(), username: 'Bob', photo: 'https://via.placeholder.com/40/00FF00/FFFFFF?text=B' },
    { id: uuidv4(), username: 'Charlie', photo: 'https://via.placeholder.com/40/800080/FFFFFF?text=C' },
  ]);

  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newUsername, setNewUsername] = useState('');

  
  const openModal = () => {
    setIsModalOpen(true);
  };

  
  const closeModal = () => {
    setIsModalOpen(false);
    setNewUsername('');
  };

  const handleAddUser = () => {
    if (newUsername.trim()) {
      const newUser: User = {
        id: uuidv4(),
        username: newUsername,
        photo: `https://via.placeholder.com/40/808080/FFFFFF?text=${newUsername.charAt(0).toUpperCase()}`,
      };
      setUsers([...users, newUser]);
      closeModal();
    }
  };

  return (
    <div className="flex items-center space-x-[-10px] p-2 rounded-lg">
      {users.map(user => (
        <img
          key={user.id}
          src={user.photo}
          alt={user.username}
          className="w-8 h-8 rounded-full border-2 border-black"
        />
      ))}
      <button
        onClick={openModal}
        className="w-8 h-8 flex items-center justify-center rounded-full bg-gray-600 border-2 border-black text-white text-xl font-bold"
      >
        +
      </button>

      <UserModal
        isOpen={isModalOpen}
        newUsername={newUsername}
        setNewUsername={setNewUsername}
        closeModal={closeModal}
        handleAddUser={handleAddUser}
      />
    </div>
  );
};

export default UserList;
