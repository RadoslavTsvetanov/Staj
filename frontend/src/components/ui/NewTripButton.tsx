import Link from "next/link";

function NewTripButton()
{
    return(
      <Link href="/planning">
        <button className="bottom-5 left-5 bg-blue-600 text-black py-2 px-4 rounded-lg">
        New trip plan
      </button>
      </Link>
    )
};

export default NewTripButton;