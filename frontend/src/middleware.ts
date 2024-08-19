import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(request: NextRequest) {

    if (request.url.includes('auth')) { 
        return
    }
    const cookie = request.cookies.get("authToken")
    console.log(request.cookies)

    const authHeader = cookie 
    if (authHeader === undefined || authHeader === null) {
        console.log(authHeader)
      return NextResponse.redirect(new URL('/auth/signin', request.url))
    }
    console.log("kokoko,kkokokok")
    
    
}
