import DropdownItem from "./DropdownItem";
type DropMenuProps = {
    onMouseLeave: () => void;
};

const DropMenu: React.FC<DropMenuProps>=({onMouseLeave})=>
{ 
    return(
        <div className="absolute slide-in-from-top-1/3 right-0 bg-gray-50 rounded-[8px] p-[10px] px-[20px] w-1/4"   onMouseLeave={onMouseLeave} >
            
            <ul>
                <DropdownItem text={"My profile"} img={"/images/user.png"} href={"/profile"}/>
                <DropdownItem text={"Friends"} img={"/images/friends.png"} href={"#"}/>
                <DropdownItem text={"Log out"} img={"/images/log-out.png"} href={'#'}/>
            </ul>

        </div> 

    )
}

export default DropMenu;