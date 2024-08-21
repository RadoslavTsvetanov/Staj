
import { cookies } from '@/lib/utils';

export const getUserPlans = async () => {
    const headers = new Headers()
    headers.set('Authorization', `Bearer ${cookies.authToken.get()}`)
    const response = await fetch(process.env.NEXT_PUBLIC_API_BASE_URL + '/user-access/plans', {headers});
    return await response.json();
};