import React, { FC } from 'react';

interface UserModalProps {
  isOpen: boolean;
  newUsername: string;
  setNewUsername: (value: string) => void;
  closeModal: () => void;
  handleAddUser: () => void;
}

const UserModal: FC<UserModalProps> = ({
  isOpen,
  newUsername,
  setNewUsername,
  closeModal,
  handleAddUser,
}) => {
  if (!isOpen) return null; // Don't render anything if the modal is not open

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-80">
        <h2 className="text-xl font-semibold mb-4">Add New User</h2>
        <input
          type="text"
          value={newUsername}
          onChange={(e) => setNewUsername(e.target.value)}
          placeholder="Enter username"
          className="w-full p-2 border border-gray-300 rounded mb-4"
        />
        <div className="flex justify-end space-x-2">
          <button
            onClick={closeModal}
            className="px-4 py-2 bg-gray-300 text-gray-700 rounded"
          >
            Cancel
          </button>
          <button
            onClick={handleAddUser}
            className="px-4 py-2 bg-blue-600 text-white rounded"
          >
            Add User
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserModal;
