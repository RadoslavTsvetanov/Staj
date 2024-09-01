import Image from 'next/image';

function Memory() {
  return (
    <div className="bg-gray-200 h-[25vh] w-full rounded overflow-hidden relative">
      <Image 
        src="/images/memory.png" 
        alt="memory" 
        layout="fill" 
        objectFit="cover" 
        className="rounded" 
      />
    </div>
  );
}

export default Memory;
