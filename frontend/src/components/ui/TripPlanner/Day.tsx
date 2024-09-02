interface DayProps {
    dayNumber: number;
}

function Day({ dayNumber }: DayProps) {
    return (
        <p className="font-semibold">Day {dayNumber}</p>
    );
}

export default Day;
