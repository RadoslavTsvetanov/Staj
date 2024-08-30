import { redirect } from 'next/dist/server/api-utils';
import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import path from 'path';

export function middleware(request: NextRequest) {
    // Allow access to specific paths without authentication
    const publicPathsStartingWith = ["/_next", "/static", '/auth/signin'];
    const publicPathsFull = ["/auth/signup", "/"];

    if (publicPathsStartingWith.some(path => request.nextUrl.pathname.startsWith(path)) ||
        publicPathsFull.some(path => request.nextUrl.pathname == path)) {
        return NextResponse.next();
    }
    
    console.log(request.nextUrl.pathname)

    // Check for authentication token
    const authToken = request.cookies.get('authToken');
    console.log("jjjjjj"+authToken)

    if (!authToken) {
        // Redirect to sign-in page if the token is not present
        return NextResponse.redirect(new URL('/auth/signin', request.url));
    }

    // If authenticated, allow the request to continue
    return NextResponse.next();
}
