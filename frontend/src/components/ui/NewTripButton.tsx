import { useRouter } from 'next/navigation';

type NewTripButtonProps = {
  position: {
    lat: number;
    lng: number;
  };
};

const NewTripButton: React.FC<NewTripButtonProps> = ({ position }) => {
  const router = useRouter();
  console.log(position);
  
  const handleClick = () => {
    const query = new URLSearchParams({
      lat: position.lat.toString(),
      lng: position.lng.toString()
    }).toString();

    router.push(`/planning?${query}`);
  };

  return (
    <button onClick={handleClick} className="bottom-5 left-5 bg-blue-600 text-black py-2 px-4 rounded-lg">
      New trip plan
    </button>
  );
};

export default NewTripButton;
