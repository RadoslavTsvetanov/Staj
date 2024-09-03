import { FC , useState} from "react";
import FriendItem from "./FriendItem";

interface Friend {
  id: number;
  name: string;
  img: string; 
}

interface PopupProps {
  friends: Friend[];
  isOpen: boolean;
  onClose: () => void;
}

const FriendsList: FC<PopupProps> = ({ friends, isOpen, onClose }) => {
  const [friendsList, setFriendsList] = useState<Friend[]>(friends);
  const [newFriendName, setNewFriendName] = useState<string>("");

  const handleAddFriend = () => {
    if (newFriendName.trim() === "") return;

    const newFriend: Friend = {
      id: friendsList.length + 1, // Simple id assignment; you may need to handle ids differently in a real app
      name: newFriendName,
      img: "/images/user.png",
    };

    setFriendsList([...friendsList, newFriend]);
    setNewFriendName("");
  };

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
            {friendsList.map((friend) => (
              <li key={friend.id}>
                <FriendItem name={friend.name} img={friend.img} />
              </li>
            ))}
            {/* Input and Button for Adding a New Friend */}
            <li className="flex items-center space-x-2">
              <input
                type="text"
                value={newFriendName}
                onChange={(e) => setNewFriendName(e.target.value)}
                placeholder="Enter username"
                className="flex-grow p-2 border rounded-md outline-none focus:border-blue-500"
              />
              <button
                onClick={handleAddFriend}
                className="bg-blue-500 text-white px-4 py-2 mr-2 rounded-md hover:bg-blue-600"
              >
                Add Friend
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default FriendsList;
