import Image from 'next/image';

type ItemProps = {
    img: string,
    text: string,
};

const DropdownItem: React.FC<ItemProps> =({text,img})=>
{ 
    return(
        <li className='flex m-auto'>
        <h3>{text}</h3>
        <Image className='max-w-[20px] mr-[10px] opacity-50 transition-[var(--speed)]' src={img} alt="" width={40} height={40} />
        </li>
    )
}

export default DropdownItem;