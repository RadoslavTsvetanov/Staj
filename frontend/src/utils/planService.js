
import { cookies } from '@/lib/utils';

const getAuthHeaders = () => {

    return { Authorization: `Bearer ${cookies.authToken.get()}` };
};

// export const getAllPlans = async () => {
//     return await axios.get(process.env.NEXT_PUBLIC_API_BASE_URL, { headers: getAuthHeaders() });
// };

// export const getPlanById = async (id) => {
//     return await axios.get(`${API_BASE_URL}/${id}`, { headers: getAuthHeaders() });
// };

// export const createPlan = async (plan) => {
//     return await axios.post(API_BASE_URL, plan, { headers: getAuthHeaders() });
// };

// export const addUserToPlan = async (planId, username) => {
//     return await axios.post(`${API_BASE_URL}/${planId}/users`, null, {
//         params: { username },
//         headers: getAuthHeaders(),
//     });
// };

// export const addPlacesToPlan = async (planId, placeNames) => {
//     return await axios.put(`${API_BASE_URL}/${planId}/places`, placeNames, { headers: getAuthHeaders() });
// };

// export const addLocationsToPlace = async (planId, placeId, locationNames) => {
//     return await axios.put(`${API_BASE_URL}/${planId}/places/${placeId}/locations`, locationNames, { headers: getAuthHeaders() });
// };

// export const setDateWindowToPlan = async (planId, dateWindowId, newDateWindow) => {
//     return await axios.put(
//         `${API_BASE_URL}/${planId}/date-window`,
//         { dateWindowId, ...newDateWindow },
//         { headers: getAuthHeaders() }
//     );
// };

export async function createPlan(plan) {
    const url =process.env.NEXT_PUBLIC_API_BASE_URL+'/plans'; // Adjust the URL if needed based on your API base path.
    const token = cookies.authToken.get();
    
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}` // Include the JWT token for authorization
        },
        body: JSON.stringify(plan) // Convert the plan object to JSON string
    });

    if (!response.ok) {
        // Handle different error statuses if needed
        if (response.status === 401) {
            throw new Error("Unauthorized: Please log in.");
        } else if (response.status === 409) {
            throw new Error("Conflict: Plan with this name already exists.");
        } else {
            throw new Error("Failed to create plan.");
        }
    }

    const savedPlan = await response.json(); // Parse the JSON response
    return savedPlan; // Return the created plan object
}


