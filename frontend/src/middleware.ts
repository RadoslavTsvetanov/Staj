import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
    const url = new URL(request.url);

    // Allow access to specific paths without authentication
    const publicPaths = ['/auth/signin', '/auth/signup', '/', '/auth/signup/info']; // The root path for the landing page

    // Check if the request URL is in the list of public paths
    if (publicPaths.some(path => url.pathname.startsWith(path))) {
        return NextResponse.next();
    }

    // Check for authentication token
    const authToken = request.cookies.get('authToken');

    if (!authToken) {
        // Redirect to sign-in page if the token is not present
        return NextResponse.redirect(new URL('/auth/signin', request.url));
    }

    // If authenticated, allow the request to continue
    return NextResponse.next();
}
