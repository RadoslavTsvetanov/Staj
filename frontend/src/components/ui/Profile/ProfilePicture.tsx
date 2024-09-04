import { FC, useRef } from 'react';
import Image from 'next/image';
import DefaultPfp from "../../../../public/images/buffpfp.webp";

interface ProfilePictureProps {
    profilePictureUrl: string;
    handleProfilePictureChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const ProfilePicture: FC<ProfilePictureProps> = ({ profilePictureUrl, handleProfilePictureChange }) => {
    const fileInputRef = useRef<HTMLInputElement | null>(null);

    const handleProfilePictureClick = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    return (
        <div className="flex items-center float-left mb-30">
            <div
                onClick={handleProfilePictureClick}
                className="cursor-pointer"
            >
                <Image
                    src={profilePictureUrl || DefaultPfp}
                    alt="Profile Picture"
                    width={70}
                    height={70}
                    className="rounded-full"
                />
            </div>
            <input
                type='file'
                accept="image/*"
                className='absolute flex left-0 top-0 opacity-0'
                onChange={handleProfilePictureChange}
                ref={fileInputRef}
            />
        </div>
    );
};

export default ProfilePicture;
