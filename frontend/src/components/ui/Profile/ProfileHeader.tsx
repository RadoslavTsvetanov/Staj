import { FC } from 'react';
import Image from 'next/image';
import LArrow from "../../../../public/images/left.png";
import { useRouter } from 'next/navigation';

interface ProfileHeaderProps {
    isDirty: boolean;
    handleSave: () => void;
}

const ProfileHeader: FC<ProfileHeaderProps> = ({ isDirty, handleSave }) => {
    const router = useRouter();

    return (
        <div className="flex items-center justify-center mb-4 relative">
            <div className="absolute left-0 max-w-7">
                <Image
                    src={LArrow}
                    alt="Left arrow"
                    onClick={() => router.back()}
                    className='cursor-pointer'
                />
            </div>
            <h1 className="text-2xl font-semibold text-gray-700">
                My profile
            </h1>
            <div className='absolute right-0'>
                <button
                    className={`text-white-500 rounded-md px-3 py-1 ${isDirty ? 'font-bold' : 'bg-transparent'}`}
                    onClick={handleSave}
                    type="button"
                    disabled={!isDirty}
                >
                    Save
                </button>
            </div>
        </div>
    );
};

export default ProfileHeader;
