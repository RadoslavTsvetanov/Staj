// components/Popup.tsx
import { FC } from "react";
import FriendItem from "./FriendItem"; // Import the FriendItem component

interface Friend {
  id: number;
  name: string;
  img: string; // Add imgSrc to the interface to handle profile pictures
}

interface PopupProps {
  friends: Friend[];
  isOpen: boolean;
  onClose: () => void;
}

const FriendsList: FC<PopupProps> = ({ friends, isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="relative bg-white rounded-lg p-8 w-full max-w-2xl h-3/4 overflow-y-hidden">
        <button
          className="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-2xl"
          onClick={onClose}
          aria-label="Close"
        >
          ✕
        </button>
        <h2 className="text-2xl font-bold mb-6 text-center">Friends List</h2>
        <div className="h-[calc(8*51px)] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
          <ul className="space-y-4">
            {friends.map((friend) => (
              <li key={friend.id}>
                <FriendItem name={friend.name} img={friend.img} />
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default FriendsList;
