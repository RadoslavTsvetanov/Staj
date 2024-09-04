import { FC } from 'react';
import { Popup } from "./Popup";

interface DeleteAccountButtonProps {
    handleDelete: () => void;
    isPopupVisible: boolean;
    togglePopup: () => void;
    handleConfirmDelete: () => void;
}

const DeleteAccountButton: FC<DeleteAccountButtonProps> = ({
    handleDelete,
    isPopupVisible,
    togglePopup,
    handleConfirmDelete,
}) => {
    return (
        <div className='flex float-right'>
            <button
                onClick={handleDelete}
                className="mt-4 bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600"
            >
                Delete Account
            </button>
            {isPopupVisible && (
            <Popup
                visible={isPopupVisible}
                onConfirm={() => new Promise((resolve) => {
                    handleConfirmDelete();
                    resolve();
                })}
                togglePopup={togglePopup}
                message="Are you sure you want to delete your account?"
            />
)}
        </div>
    );
};

export default DeleteAccountButton;
