import React, { useState } from 'react';
import Link from "next/link";

type PlanProps = {
    name: string;
    isEditable: boolean;
};

const Plan: React.FC<PlanProps> = ({ name, isEditable }) => {
    const [isHovered, setIsHovered] = useState(false);

    const styles = {
        button: {
            backgroundColor: isEditable ? '#C2E6F4' : '#F58F92', 
            color: 'black',
            padding: '10px 20px',
            borderRadius: '8px',
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            border: '1px solid black',
            cursor: 'pointer',
            width: '100%',
            boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)',
        },
        arrow: {
            marginLeft: '10px',
            color: 'black',
        },
        editIcon: {
            marginLeft: '10px',
            color: 'white',
        },
    };

    return (
        <button
            style={styles.button}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <span>{name}</span>
            {isHovered && (
                isEditable ? (
                    <span style={styles.editIcon}>✏️</span>
                ) : (
                    <span style={styles.arrow}>&#x25B6;</span>
                )
            )}
        </button>
    );
};

export default Plan;

