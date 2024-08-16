import DropdownItem from "./DropdownItem";

function DropMenu()
{ 
    return(
        <div className="bg-pink-500 opacity-100 visible translate-y-0 transition-all ease-[var(--speed)]">
            <ul>
                <DropdownItem text={"profile"} img={"/images/user.png"}/>
                <DropdownItem text={"friends"} img={"/images/friends.png"}/>
                <DropdownItem text={"log out"} img={"/images/log-out.png"}/>
            </ul>

        </div>

    )
}

export default DropMenu;