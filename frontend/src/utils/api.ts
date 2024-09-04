import { cookies } from '@/lib/utils';

const getAuthHeaders = () => {

    return { Authorization: `Bearer ${cookies.authToken.get()}` };
};

export const getUserPlans = async () => {
    const headers = new Headers()
    headers.set('Authorization', `Bearer ${cookies.authToken.get()}`)
    const response = await fetch(process.env.NEXT_PUBLIC_API_BASE_URL + '/user-access/plans', { headers: getAuthHeaders() });
    return await response.json();
};

export const saveDateWindow = async (startDate: string, endDate: string) => {
    const dateWindow = {
        startDate: startDate,
        endDate: endDate,
    };

    const headers = new Headers();
    const authToken = cookies.authToken.get();

    if (!authToken) {
        console.error('Authorization token is missing.');
        throw new Error('Authorization token is missing.');
    }

    headers.set('Authorization', `Bearer ${authToken}`);
    headers.set('Content-Type', 'application/json'); 

    try {
        const response = await fetch(process.env.NEXT_PUBLIC_API_BASE_URL + '/date-window', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(dateWindow),
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error(`Failed to save DateWindow: ${response.status} ${response.statusText} - ${errorText}`);
            throw new Error(`Failed to save DateWindow: ${response.statusText}`);
        }

        const data = await response.json();
        console.log('DateWindow saved:', data);
        
        return data;

    } catch (error) {
        console.error('There was an error saving the DateWindow:', error);
        throw error;
    }
};


