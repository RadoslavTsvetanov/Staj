import styled from 'styled-components';

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const PopupWindow = styled.div`
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  max-width: 500px;
  width: 100%;
  display: flex;
  flex-direction: column; /* Stack content vertically */
  justify-content: space-between; /* Space between content and buttons */
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end; /* Align buttons to the right */
  gap: 10px; /* Space between buttons */
`;

const Button = styled.button`
  background-color: #0070f3;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  height: 40px;          /* Set a fixed height */
  width: 70px;           /* Set a fixed width */
  line-height: 40px;     /* Match line-height to height for vertical centering */
  text-align: center;    /* Center the text horizontally */
  margin-top: 20px;
`;

const CloseButton = styled(Button)`
  background-color: #d1cfcf;

  &:hover {
    background-color: #807e7e;
  }
`;

const YesButton = styled(Button)`
  background-color: #ff5f5f;

  &:hover {
    background-color: #b03f3f;
  }
`;

// Popup component
interface PopupProps {
  togglePopup: () => void;
}

export const Popup: React.FC<PopupProps> = ({ togglePopup }) => {
  return (
    <Overlay>
      <PopupWindow>
        <div>
          <h2>Account Deletion</h2>
          <p>Are you sure you want to delete your account?</p>
        </div>
        <ButtonContainer>
          <CloseButton onClick={togglePopup}>Close</CloseButton>
          <YesButton onClick={togglePopup}>Yes</YesButton>
        </ButtonContainer>
      </PopupWindow>
    </Overlay>
  );
};
