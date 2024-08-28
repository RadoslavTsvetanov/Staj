import Image from 'next/image';
import Link from 'next/link';

type ItemProps = {
    img: string,
    text: string,
    href: string,
};

const DropdownItem: React.FC<ItemProps> =({text,img,href})=>
{ 
    return(
        <Link href={href}>
        <li className="flex m-auto pt-2.5 pb-2.5 border-t border-[rgba(0,0,0,0.05)]">
        <Image className='max-w-[20px] mr-[10px] opacity-50 transition-[var(--speed)]' src={img} alt="" width={40} height={40} /> 
        <h3 className=' max-w-[100px] ml-2 transition-[var(--speed)]'>{text}</h3>
        </li>
        </Link>
    )
}

export default DropdownItem;