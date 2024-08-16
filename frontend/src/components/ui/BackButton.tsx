import Image from 'next/image';
import Link from "next/link";

function BackButton() {
  return (
    <Link href="/home">
       <div className="absolute top-0 left-0 flex ">
      <Image src="/images/left.png" alt="back" width={40} height={40} />
      </div>
    </Link>
  );
}

export default BackButton;