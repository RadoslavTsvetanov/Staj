import React from 'react';
import { useRouter } from 'next/navigation';
import { handleLogout } from '@/lib/utils';
import DropdownItem from "./DropdownItem";

export default function LogoutButton() {
    const router = useRouter();

    const onLogout = async () => {
        await handleLogout();
        router.push('/auth/signin');
    };

    return (
        <button
            onClick={onLogout}>
            <DropdownItem text={"Log out"} img={"/images/log-out.png"} href={'#'} />
        </button>
    );
}