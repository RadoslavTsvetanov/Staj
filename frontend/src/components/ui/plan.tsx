import React, { useState } from 'react';

type PlanProps = {
    name: string;
};

const Plan: React.FC<PlanProps> = ({ name }) => {
    const [isHovered, setIsHovered] = useState(false);

    return (
        <button
            style={styles.button}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <span>{name}</span>
            {isHovered && <span style={styles.arrow}>&#x25B6;</span>}
        </button>
    );
};

const styles = {
    button: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        backgroundColor: '#f28b82', // pink background
        borderRadius: '15px', // rounded corners
        border: '1px solid #000', // black border
        padding: '10px 20px',
        fontSize: '16px',
        cursor: 'pointer',
        width: '100%',
    },
    arrow: {
        marginLeft: 'auto',
    },
};

export default Plan;
