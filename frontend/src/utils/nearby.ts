import { cookies } from '@/lib/utils';

export const fetchNearbyPlaces = async (payload) => {
    const authToken = cookies.authToken.get();

    if (!authToken) {
        console.error('Authorization token is missing.');
        throw new Error('Authorization token is missing.');
    }


    const adjustedPayload = {
        // latitude: payload.location.lat,        
        // longitude: payload.location.lng,     
        latitude: 42.698334,        
        longitude: 23.319941,  
        // radius: payload.radius,    
        radius: 20000,             
        types: ['restaurant', 'cafe'],      
        authTokens: [authToken],           
    };

    try {
        const response = await fetch(process.env.NEXT_PUBLIC_API_BASE_URL + '/maps/nearby', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`,
            },
            body: JSON.stringify(adjustedPayload),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Response data:', data);

        return data;
    } catch (error) {
        console.error('Error fetching nearby places:', error);
        throw error;
    }
};
