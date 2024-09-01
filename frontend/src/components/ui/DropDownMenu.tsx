import { useState } from "react";
import Link from "next/link";
import DropdownItem from "./DropdownItem";
import LogoutButton from "./LogoutButton";
import FriendsList from "./Friends"; 

type DropMenuProps = {
  onMouseLeave: () => void;
};

const friends = [
  { id: 1, name: "Alice", img: "/images/user.png"},
  { id: 2, name: "Bob", img: "/images/user.png" },
  { id: 3, name: "Charlie", img: "/images/user.png" },
  { id: 1, name: "Alice", img: "/images/user.png" },
  { id: 2, name: "Bob", img: "/images/user.png" },
  { id: 3, name: "Charlie", img: "/images/user.png" },
  { id: 1, name: "Alice", img: "/images/user.png" },
  { id: 2, name: "Bob", img: "/images/user.png" },
  { id: 3, name: "Charlie", img: "/images/user.png" },
  { id: 1, name: "Alice", img: "/images/user.png" },
  { id: 2, name: "Bob", img: "/images/user.png" },
  { id: 3, name: "Charlie", img: "/images/user.png" },
];

const DropMenu: React.FC<DropMenuProps> = ({ onMouseLeave }) => {
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  const handlePopUp = () => {
    setIsPopupOpen(true);
  };

  return (
    <div
      className="absolute slide-in-from-top-1/3 right-0 bg-gray-50 rounded-[8px] p-[10px] px-[20px] w-1/4"
      onMouseLeave={onMouseLeave}
    >
      <ul>
        <li>
          <Link href={"/profile"}>
            <DropdownItem text={"My profile"} img={"/images/user.png"} />
          </Link>
        </li>
        <li>
          <DropdownItem
            text={"Friends"}
            img={"/images/friends.png"}
            onClick={handlePopUp}
          />
        </li>
        <li>
          <LogoutButton />
        </li>
      </ul>

      <FriendsList
        friends={friends}
        isOpen={isPopupOpen}
        onClose={() => setIsPopupOpen(false)}
      />
    </div>
  );
};

export default DropMenu;
